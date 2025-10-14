package iuh.fit.se.iwork4se.dto;

import jakarta.validation.constraints.Email;

public record UpdateReferenceRequest(
        String name,
        String company,
        @Email String email,
        String relation
) {}
