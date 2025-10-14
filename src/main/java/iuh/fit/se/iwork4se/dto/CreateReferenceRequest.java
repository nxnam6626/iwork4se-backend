package iuh.fit.se.iwork4se.dto;

import jakarta.validation.constraints.*;

public record CreateReferenceRequest(
        @NotBlank String name,
        String company,
        @Email String email,
        String relation
) {}
