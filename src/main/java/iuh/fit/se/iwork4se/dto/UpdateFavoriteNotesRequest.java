package iuh.fit.se.iwork4se.dto;

import jakarta.validation.constraints.Size;

public record UpdateFavoriteNotesRequest(
    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    String notes
) {}
