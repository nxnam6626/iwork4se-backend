package iuh.fit.se.iwork4se.repository;

import iuh.fit.se.iwork4se.model.Education;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EducationRepository extends JpaRepository<Education, UUID> {
    List<Education> findByJobSeekerProfile_JobSeekerProfileId(UUID jobSeekerProfileId);
    
    // Kiểm tra trùng lặp: cùng school + degree + major + startYear
    boolean existsByJobSeekerProfile_JobSeekerProfileIdAndSchoolAndDegreeAndMajorAndStartYear(
            UUID jobSeekerProfileId, String school, String degree, String major, java.time.LocalDate startYear);
}
