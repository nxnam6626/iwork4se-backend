package iuh.fit.se.iwork4se.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class JobFavoriteDTO {
    private UUID id;
    private UUID jobId;
    private String jobTitle;
    private String companyName;
    private String location;
    private String employmentType;
    private Integer salaryMin;
    private Integer salaryMax;
    private String currency;
    private Instant favoritedAt;
    private String notes;
    private String jobDescription;
    private String companyIndustry;
}
