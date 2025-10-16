package iuh.fit.se.iwork4se.dto;

import jakarta.validation.constraints.*;

public record AddSeekerSkillRequest(
        @NotBlank String skillName,
        @NotBlank String level
) {}
