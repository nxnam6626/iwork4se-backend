package iuh.fit.se.iwork4se.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class PublicJobSearchRequest {
    @Size(max = 150, message = "Keyword cannot exceed 150 characters")
    private String keyword; // match title/description

    @Size(max = 120, message = "Location cannot exceed 120 characters")
    private String location;

    @Size(max = 40, message = "Employment type cannot exceed 40 characters")
    private String employmentType; // FULL_TIME, PART_TIME, etc.

    private Integer salaryMin;
    private Integer salaryMax;
    private String currency;
    private Boolean remote;

    @Size(max = 100, message = "Company name cannot exceed 100 characters")
    private String companyName;

    @Size(max = 50, message = "Industry cannot exceed 50 characters")
    private String industry;

    private List<String> skills; // Required skills

    @Min(value = 0, message = "Page number cannot be negative")
    private int page = 0;

    @Min(value = 1, message = "Page size must be at least 1")
    private int size = 10;

    private String sortBy = "createdAt"; // createdAt, salary, title
    private String sortDirection = "DESC"; // ASC, DESC
}
