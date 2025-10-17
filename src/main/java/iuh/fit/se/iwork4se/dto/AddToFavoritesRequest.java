package iuh.fit.se.iwork4se.dto;

import jakarta.validation.constraints.Size;

public record AddToFavoritesRequest(
    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    String notes
) {}
