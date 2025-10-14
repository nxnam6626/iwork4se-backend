package iuh.fit.se.iwork4se.service.impl;

import iuh.fit.se.iwork4se.dto.profile.ReferencePersonDTO;
import iuh.fit.se.iwork4se.exception.ForbiddenException;
import iuh.fit.se.iwork4se.exception.ResourceNotFoundException;
import iuh.fit.se.iwork4se.model.JobSeekerProfile;
import iuh.fit.se.iwork4se.model.ReferencePerson;
import iuh.fit.se.iwork4se.repository.JobSeekerProfileRepository;
import iuh.fit.se.iwork4se.repository.ReferencePersonRepository;
import iuh.fit.se.iwork4se.service.ReferencePersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ReferencePersonServiceImpl implements ReferencePersonService {

    private final ReferencePersonRepository refRepo;
    private final JobSeekerProfileRepository profileRepo;

    private JobSeekerProfile requireMyProfile(UUID currentUserId) {
        return profileRepo.findByUser_UserId(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
    }

    private void assertOwnedBy(UUID currentUserId, ReferencePerson r) {
        if (!r.getJobSeekerProfile().getUser().getUserId().equals(currentUserId))
            throw new ForbiddenException("Not your reference");
    }

    private ReferencePersonDTO toDTO(ReferencePerson r) {
        return ReferencePersonDTO.builder()
                .id(r.getReferencePersonId())
                .name(r.getName())
                .company(r.getCompany())
                .email(r.getEmail())
                .relation(r.getRelation())
                .jobSeekerProfileId(r.getJobSeekerProfile().getJobSeekerProfileId())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReferencePersonDTO> listMyReferences(UUID currentUserId) {
        JobSeekerProfile p = requireMyProfile(currentUserId);
        return refRepo.findByJobSeekerProfile_JobSeekerProfileId(p.getJobSeekerProfileId()).stream().map(this::toDTO).toList();
    }

    @Override
    public ReferencePersonDTO createMyReference(UUID currentUserId, String name, String company, String email, String relation) {
        JobSeekerProfile p = requireMyProfile(currentUserId);
        
        // Kiểm tra trùng lặp: cùng name + email
        if (refRepo.existsByJobSeekerProfile_JobSeekerProfileIdAndNameAndEmail(
                p.getJobSeekerProfileId(), name, email)) {
            throw new IllegalArgumentException("Reference person already exists with same name and email");
        }
        
        ReferencePerson r = ReferencePerson.builder()
                .jobSeekerProfile(p).name(name).company(company).email(email).relation(relation).build();
        return toDTO(refRepo.save(r));
    }

    @Override
    public ReferencePersonDTO updateMyReference(UUID currentUserId, UUID referenceId, String name, String company, String email, String relation) {
        ReferencePerson r = refRepo.findById(referenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Reference not found"));
        assertOwnedBy(currentUserId, r);
        if (name != null) r.setName(name);
        if (company != null) r.setCompany(company);
        if (email != null) r.setEmail(email);
        if (relation != null) r.setRelation(relation);
        return toDTO(refRepo.save(r));
    }

    @Override
    public void deleteMyReference(UUID currentUserId, UUID referenceId) {
        ReferencePerson r = refRepo.findById(referenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Reference not found"));
        assertOwnedBy(currentUserId, r);
        refRepo.delete(r);
    }
}
