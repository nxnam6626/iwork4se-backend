package iuh.fit.se.iwork4se.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CompanySearchRequest {
    @Size(max = 150)
    private String keyword; // match name/industry

    @Min(0)
    private int page = 0;

    @Min(1)
    private int size = 10;
}


