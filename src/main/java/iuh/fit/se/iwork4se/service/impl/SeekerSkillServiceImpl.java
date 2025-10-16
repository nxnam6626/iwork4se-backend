package iuh.fit.se.iwork4se.service.impl;

import iuh.fit.se.iwork4se.dto.profile.SeekerSkillDTO;
import iuh.fit.se.iwork4se.exception.ForbiddenException;
import iuh.fit.se.iwork4se.exception.ResourceNotFoundException;
import iuh.fit.se.iwork4se.model.JobSeekerProfile;
import iuh.fit.se.iwork4se.model.SeekerSkill;
import iuh.fit.se.iwork4se.model.Skill;
import iuh.fit.se.iwork4se.repository.JobSeekerProfileRepository;
import iuh.fit.se.iwork4se.repository.SeekerSkillRepository;
import iuh.fit.se.iwork4se.repository.SkillRepository;
import iuh.fit.se.iwork4se.service.SeekerSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SeekerSkillServiceImpl implements SeekerSkillService {

    private final SeekerSkillRepository seekerSkillRepo;
    private final JobSeekerProfileRepository profileRepo;
    private final SkillRepository skillRepo;

    private JobSeekerProfile requireMyProfile(UUID currentUserId) {
        return profileRepo.findByUser_UserId(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
    }

    private void assertOwnedBy(UUID currentUserId, SeekerSkill ss) {
        if (!ss.getJobSeekerProfile().getUser().getUserId().equals(currentUserId))
            throw new ForbiddenException("Not your skill");
    }

    private SeekerSkillDTO toDTO(SeekerSkill ss) {
        return SeekerSkillDTO.builder()
                .id(ss.getSeekerSkillId())
                .jobSeekerProfileId(ss.getJobSeekerProfile().getJobSeekerProfileId())
                .skillId(ss.getSkill().getSkillId())
                .skillName(ss.getSkill().getName())
                .level(ss.getLevel())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeekerSkillDTO> listMySeekerSkills(UUID currentUserId) {
        JobSeekerProfile p = requireMyProfile(currentUserId);
        return seekerSkillRepo.findByJobSeekerProfile_JobSeekerProfileId(p.getJobSeekerProfileId()).stream().map(this::toDTO).toList();
    }

    @Override
    public SeekerSkillDTO addMySkill(UUID currentUserId, String skillName, String level) {
        JobSeekerProfile p = requireMyProfile(currentUserId);
        
        // Tìm skill theo tên (case-insensitive)
        Skill skill = skillRepo.findByNameIgnoreCase(skillName.trim())
                .orElseGet(() -> {
                    // Nếu chưa có skill, tạo mới
                    Skill newSkill = Skill.builder()
                            .name(skillName.trim())
                            .build();
                    return skillRepo.save(newSkill);
                });
        
        // Kiểm tra xem skill này đã được thêm vào profile chưa
        boolean alreadyExists = seekerSkillRepo.existsByJobSeekerProfile_JobSeekerProfileIdAndSkill_SkillId(
                p.getJobSeekerProfileId(), skill.getSkillId());
        
        if (alreadyExists) {
            throw new IllegalArgumentException("Skill '" + skillName + "' already exists in your profile");
        }
        
        SeekerSkill ss = SeekerSkill.builder()
                .jobSeekerProfile(p)
                .skill(skill)
                .level(level)
                .build();
        return toDTO(seekerSkillRepo.save(ss));
    }

    @Override
    public SeekerSkillDTO updateMySkillLevel(UUID currentUserId, UUID seekerSkillId, String level) {
        SeekerSkill ss = seekerSkillRepo.findById(seekerSkillId)
                .orElseThrow(() -> new ResourceNotFoundException("SeekerSkill not found"));
        assertOwnedBy(currentUserId, ss);
        ss.setLevel(level);
        return toDTO(seekerSkillRepo.save(ss));
    }

    @Override
    public void removeMySkill(UUID currentUserId, UUID seekerSkillId) {
        SeekerSkill ss = seekerSkillRepo.findById(seekerSkillId)
                .orElseThrow(() -> new ResourceNotFoundException("SeekerSkill not found"));
        assertOwnedBy(currentUserId, ss);
        seekerSkillRepo.delete(ss);
    }
}
