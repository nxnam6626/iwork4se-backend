package iuh.fit.se.iwork4se.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record CreateEducationRequest(
        @NotBlank String school,
        @NotBlank String degree,
        @NotBlank String major,
        @NotNull LocalDate startYear,
        LocalDate endYear,
        String description
) {}
