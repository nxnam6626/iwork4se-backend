package iuh.fit.se.iwork4se.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JobSearchRequest {
    @Size(max = 150)
    private String keyword; // match title/description

    @Size(max = 120)
    private String location;

    @Size(max = 40)
    private String employmentType; // FULL_TIME, PART_TIME, etc.

    private Integer salaryMin;
    private Integer salaryMax;
    private String currency;
    private Boolean remote;

    @Min(0)
    private int page = 0;

    @Min(1)
    private int size = 10;
}
