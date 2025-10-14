package iuh.fit.se.iwork4se.dto;

import jakarta.validation.constraints.*;
import java.util.UUID;

public record AddSeekerSkillRequest(
        @NotNull UUID skillId,
        @NotBlank String level
) {}
