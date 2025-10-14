package iuh.fit.se.iwork4se.repository;

import iuh.fit.se.iwork4se.model.JobSeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JobSeekerProfileRepository extends JpaRepository<JobSeekerProfile, UUID> {
    Optional<JobSeekerProfile> findByUser_UserId(UUID userId);
}