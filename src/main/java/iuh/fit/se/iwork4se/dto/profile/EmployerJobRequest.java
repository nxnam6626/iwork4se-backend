package iuh.fit.se.iwork4se.dto.profile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployerJobRequest {
    @NotBlank
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

    @Min(0)
    private int page = 0; // reused for list endpoints
    @Min(1)
    private int size = 10;
}


