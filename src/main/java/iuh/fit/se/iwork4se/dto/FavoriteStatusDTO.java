package iuh.fit.se.iwork4se.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class FavoriteStatusDTO {
    private boolean isFavorited;
    private UUID favoriteId;
    private Instant favoritedAt;
    private String notes;
}
