package iuh.fit.se.iwork4se.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record SetCurrentFileRequest(
        @NotNull UUID cvFileId
) {}
