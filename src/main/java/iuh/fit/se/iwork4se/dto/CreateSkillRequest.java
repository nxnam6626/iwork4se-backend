package iuh.fit.se.iwork4se.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateSkillRequest(
        @NotBlank String name,
        String description
) {}
