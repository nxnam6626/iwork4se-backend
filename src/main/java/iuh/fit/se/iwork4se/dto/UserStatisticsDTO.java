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
public class UserStatisticsDTO {
    private long totalUsers;
    private long totalJobSeekers;
    private long totalEmployers;
    private long totalAdmins;
    private long newUsersThisMonth;
    private long activeUsersThisMonth;
    private Map<String, Long> usersByRole;
    private Map<String, Long> usersByMonth;
    private Map<String, Long> jobSeekersByExperience;
    private Map<String, Long> employersByCompanySize;
}
