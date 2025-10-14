package iuh.fit.se.iwork4se.repository;

import iuh.fit.se.iwork4se.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResumeRepository extends JpaRepository<Resume, UUID> {
    List<Resume> findByJobSeekerProfile_JobSeekerProfileId(UUID jobSeekerProfileId);
}
