package iuh.fit.se.iwork4se.dto;

import jakarta.validation.constraints.NotBlank;

public record RenameResumeRequest(
        @NotBlank String newTitle
) {}
