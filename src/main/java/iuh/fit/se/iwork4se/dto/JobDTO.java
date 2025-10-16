package iuh.fit.se.iwork4se.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobDTO {
    private UUID jobId;
    private UUID companyId;
    private String companyName;
    private String title;
    private String description;
    private String location;
    private String employmentType;
    private Integer salaryMin;
    private Integer salaryMax;
    private String currency;
    private boolean remote;
    private Integer openings;
    private LocalDate expireAt;
}


