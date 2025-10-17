package iuh.fit.se.iwork4se.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemStatisticsDTO {
    private long totalUsers;
    private long totalJobSeekers;
    private long totalEmployers;
    private long totalJobs;
    private long totalCompanies;
    private long totalApplications;
    private long totalReviews;
    private double averageJobRating;
    private Map<String, Long> jobsByEmploymentType;
    private Map<String, Long> usersByRole;
    private Map<String, Long> applicationsByStatus;
    private Map<String, Long> jobsByMonth;
    private Map<String, Long> applicationsByMonth;
}
