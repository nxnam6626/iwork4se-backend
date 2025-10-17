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
public class JobStatisticsDTO {
    private long totalJobs;
    private long activeJobs;
    private long expiredJobs;
    private long totalApplications;
    private double averageApplicationsPerJob;
    private Map<String, Long> jobsByEmploymentType;
    private Map<String, Long> jobsByLocation;
    private Map<String, Long> jobsBySalaryRange;
    private Map<String, Long> jobsByCompany;
    private Map<String, Long> applicationsByMonth;
    private Map<String, Long> jobsByMonth;
}
