package iuh.fit.se.iwork4se.repository;

import iuh.fit.se.iwork4se.model.JobSeekerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.UUID;

public interface JobSeekerProfileRepository extends JpaRepository<JobSeekerProfile, UUID> {

    @Query(value = """
        SELECT DISTINCT p.job_seeker_profile_id, p.headline, p.location, p.summary, p.user_id, p.years_exp
        FROM job_seeker_profiles p
        LEFT JOIN seeker_skills ss ON p.job_seeker_profile_id = ss.job_seeker_profile_id
        LEFT JOIN skills s ON s.skill_id = ss.skill_id
        LEFT JOIN experiences e ON p.job_seeker_profile_id = e.job_seeker_profile_id
        WHERE (
               :keyword IS NULL
               OR LOWER(p.headline) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(p.summary) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(p.location) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
          AND (:minYearsExp IS NULL OR p.years_exp >= :minYearsExp)
          AND (:location IS NULL OR LOWER(p.location) LIKE LOWER(CONCAT('%', :location, '%')))
          AND (:titlesEmpty = true OR LOWER(e.title) IN (:titles))
          AND (:skillsEmpty = true OR LOWER(s.name) IN (:skills))
    """, nativeQuery = true)
    Page<JobSeekerProfile> search(
            @Param("keyword") String keyword,
            @Param("minYearsExp") Integer minYearsExp,
            @Param("location") String location,
            @Param("titles") Collection<String> titles,
            @Param("titlesEmpty") boolean titlesEmpty,
            @Param("skills") Collection<String> skills,
            @Param("skillsEmpty") boolean skillsEmpty,
            Pageable pageable
    );

    java.util.Optional<JobSeekerProfile> findByUser_UserId(UUID userId);
}