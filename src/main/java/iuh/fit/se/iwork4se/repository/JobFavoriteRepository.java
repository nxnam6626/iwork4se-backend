package iuh.fit.se.iwork4se.repository;

import iuh.fit.se.iwork4se.model.JobFavorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JobFavoriteRepository extends JpaRepository<JobFavorite, UUID> {

    /**
     * Kiểm tra xem job đã được job seeker yêu thích chưa
     */
    boolean existsByJobSeekerProfile_JobSeekerProfileIdAndJob_JobId(UUID jobSeekerProfileId, UUID jobId);

    /**
     * Lấy danh sách job favorites của một job seeker
     */
    Page<JobFavorite> findByJobSeekerProfile_JobSeekerProfileIdOrderByFavoritedAtDesc(UUID jobSeekerProfileId, Pageable pageable);

    /**
     * Tìm job favorite theo job seeker và job
     */
    Optional<JobFavorite> findByJobSeekerProfile_JobSeekerProfileIdAndJob_JobId(UUID jobSeekerProfileId, UUID jobId);

    /**
     * Đếm số lượng job favorites của một job seeker
     */
    long countByJobSeekerProfile_JobSeekerProfileId(UUID jobSeekerProfileId);

    /**
     * Xóa job favorite theo job seeker và job
     */
    void deleteByJobSeekerProfile_JobSeekerProfileIdAndJob_JobId(UUID jobSeekerProfileId, UUID jobId);

    /**
     * Lấy danh sách job favorites với thông tin job và company
     */
    @Query("SELECT jf FROM JobFavorite jf " +
           "JOIN FETCH jf.job j " +
           "JOIN FETCH j.company c " +
           "WHERE jf.jobSeekerProfile.jobSeekerProfileId = :jobSeekerProfileId " +
           "ORDER BY jf.favoritedAt DESC")
    Page<JobFavorite> findWithJobAndCompanyByJobSeekerProfileId(@Param("jobSeekerProfileId") UUID jobSeekerProfileId, Pageable pageable);
}
