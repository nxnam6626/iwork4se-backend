package iuh.fit.se.iwork4se.dto;

import java.time.LocalDate;

public record UpdateExperienceRequest(
        String companyName,
        String title,
        LocalDate startDate,
        LocalDate endDate,
        Boolean isCurrent,
        String description
) {}
