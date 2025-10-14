package iuh.fit.se.iwork4se.repository;

import iuh.fit.se.iwork4se.model.Experience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExperienceRepository extends JpaRepository<Experience, UUID> {
    List<Experience> findByJobSeekerProfile_JobSeekerProfileId(UUID jobSeekerProfileId);
}
