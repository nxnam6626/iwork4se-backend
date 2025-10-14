package iuh.fit.se.iwork4se.dto;

import jakarta.validation.constraints.*;

public record UpdateProfileRequest(
        @Size(max = 150) String headline,
        @Size(max = 500) String summary,
        @Size(max = 100) String location,
        @Min(0) @Max(60) Integer yearsExp
) {}
