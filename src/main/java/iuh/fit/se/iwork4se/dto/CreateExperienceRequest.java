package iuh.fit.se.iwork4se.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record CreateExperienceRequest(
        @NotBlank String companyName,
        @NotBlank String title,
        @NotNull LocalDate startDate,
        LocalDate endDate,
        boolean isCurrent,
        String description
) {}
