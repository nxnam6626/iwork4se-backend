package iuh.fit.se.iwork4se.model;

import jakarta.persistence.*; import lombok.*;
import java.time.Instant; import java.util.UUID;

@Entity @Table(name="refresh_tokens")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RefreshToken {
    @Id @Column(length=40) private String jti;
    @Column(nullable=false) private UUID userId;
    @Column(nullable=false) private Instant issuedAt;
    @Column(nullable=false) private Instant expiresAt;
    @Column(nullable=false) private boolean revoked = false;
    private Instant revokedAt;
    private String deviceInfo;
}
