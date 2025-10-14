package iuh.fit.se.iwork4se.dto;

import java.time.LocalDate;

public record UpdateEducationRequest(
        String school,
        String degree,
        String major,
        LocalDate startYear,
        LocalDate endYear,
        String description
) {}
