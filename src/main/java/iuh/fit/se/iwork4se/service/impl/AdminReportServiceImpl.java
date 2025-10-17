package iuh.fit.se.iwork4se.service.impl;

import iuh.fit.se.iwork4se.dto.*;
import iuh.fit.se.iwork4se.model.Role;
import iuh.fit.se.iwork4se.repository.*;
import iuh.fit.se.iwork4se.service.AdminReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminReportServiceImpl implements AdminReportService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final ApplicationRepository applicationRepository;
    private final CompanyReviewRepository companyReviewRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final EmployerProfileRepository employerProfileRepository;

    @Override
    public SystemStatisticsDTO getSystemStatistics() {
        long totalUsers = userRepository.count();
        long totalJobSeekers = userRepository.countByRole(Role.JOBSEEKER);
        long totalEmployers = userRepository.countByRole(Role.EMPLOYER);
        long totalJobs = jobRepository.count();
        long totalCompanies = companyRepository.count();
        long totalApplications = applicationRepository.count();
        long totalReviews = companyReviewRepository.count();

        return SystemStatisticsDTO.builder()
                .totalUsers(totalUsers)
                .totalJobSeekers(totalJobSeekers)
                .totalEmployers(totalEmployers)
                .totalJobs(totalJobs)
                .totalCompanies(totalCompanies)
                .totalApplications(totalApplications)
                .totalReviews(totalReviews)
                .averageJobRating(companyReviewRepository.getAverageRating() != null ? 
                    companyReviewRepository.getAverageRating() : 0.0)
                .build();
    }

    @Override
    public JobStatisticsDTO getJobStatistics() {
        long totalJobs = jobRepository.count();
        long totalApplications = applicationRepository.count();
        double averageApplicationsPerJob = totalJobs > 0 ? (double) totalApplications / totalJobs : 0.0;

        return JobStatisticsDTO.builder()
                .totalJobs(totalJobs)
                .activeJobs(totalJobs) // Temporary - assume all jobs are active
                .expiredJobs(0) // Temporary - no expired jobs logic yet
                .totalApplications(totalApplications)
                .averageApplicationsPerJob(averageApplicationsPerJob)
                .build();
    }

    @Override
    public UserStatisticsDTO getUserStatistics() {
        long totalUsers = userRepository.count();
        long totalJobSeekers = userRepository.countByRole(Role.JOBSEEKER);
        long totalEmployers = userRepository.countByRole(Role.EMPLOYER);
        long totalAdmins = userRepository.countByRole(Role.ADMIN);

        return UserStatisticsDTO.builder()
                .totalUsers(totalUsers)
                .totalJobSeekers(totalJobSeekers)
                .totalEmployers(totalEmployers)
                .totalAdmins(totalAdmins)
                .newUsersThisMonth(0) // TODO: Implement date-based filtering
                .activeUsersThisMonth(0) // TODO: Implement activity tracking
                .build();
    }

    @Override
    public SystemStatisticsDTO getStatisticsByDateRange(LocalDate startDate, LocalDate endDate) {
        // TODO: Implement date range filtering
        return getSystemStatistics();
    }

    @Override
    public Map<String, Object> getTopCompaniesByJobCount(int limit) {
        // TODO: Implement top companies by job count
        Map<String, Object> result = new HashMap<>();
        result.put("message", "Feature not implemented yet");
        return result;
    }

    @Override
    public Map<String, Object> getTopJobsByApplicationCount(int limit) {
        // TODO: Implement top jobs by application count
        Map<String, Object> result = new HashMap<>();
        result.put("message", "Feature not implemented yet");
        return result;
    }

    @Override
    public Map<String, Object> getApplicationTrends() {
        // TODO: Implement application trends
        Map<String, Object> result = new HashMap<>();
        result.put("message", "Feature not implemented yet");
        return result;
    }

    @Override
    public Map<String, Object> getCompanyReviewStatistics() {
        Map<String, Object> result = new HashMap<>();
        result.put("totalReviews", companyReviewRepository.count());
        result.put("averageRating", companyReviewRepository.getAverageRating());
        result.put("approvedReviews", companyReviewRepository.countByStatus("APPROVED"));
        result.put("pendingReviews", companyReviewRepository.countByStatus("PENDING"));
        result.put("rejectedReviews", companyReviewRepository.countByStatus("REJECTED"));
        return result;
    }
}
