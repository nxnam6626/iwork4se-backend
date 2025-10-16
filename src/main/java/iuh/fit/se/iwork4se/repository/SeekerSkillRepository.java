package iuh.fit.se.iwork4se.repository;

import iuh.fit.se.iwork4se.model.SeekerSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SeekerSkillRepository extends JpaRepository<SeekerSkill, UUID> {
    List<SeekerSkill> findByJobSeekerProfile_JobSeekerProfileId(UUID jobSeekerProfileId);
    boolean existsByJobSeekerProfile_JobSeekerProfileIdAndSkill_SkillId(UUID jobSeekerProfileId, UUID skillId);
}
