package iuh.fit.se.iwork4se.service.impl;

import iuh.fit.se.iwork4se.dto.profile.SkillDTO;
import iuh.fit.se.iwork4se.exception.ResourceNotFoundException;
import iuh.fit.se.iwork4se.model.Skill;
import iuh.fit.se.iwork4se.repository.SkillRepository;
import iuh.fit.se.iwork4se.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepo;

    private SkillDTO toDTO(Skill s) {
        return SkillDTO.builder().id(s.getSkillId()).name(s.getName()).description(s.getDescription()).build();
    }

    @Override
    public SkillDTO create(String name, String description) {
        Skill s = Skill.builder().name(name).description(description).build();
        return toDTO(skillRepo.save(s));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SkillDTO> findByName(String name) {
        return skillRepo.findByNameIgnoreCase(name).map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SkillDTO> listAll() {
        return skillRepo.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SkillDTO get(UUID id) {
        return skillRepo.findById(id).map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));
    }
}
