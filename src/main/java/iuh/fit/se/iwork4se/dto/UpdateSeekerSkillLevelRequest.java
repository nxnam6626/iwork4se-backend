package iuh.fit.se.iwork4se.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateSeekerSkillLevelRequest(
        @NotBlank String level
) {}
