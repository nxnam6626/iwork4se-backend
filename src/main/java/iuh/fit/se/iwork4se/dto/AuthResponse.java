package iuh.fit.se.iwork4se.dto;

import iuh.fit.se.iwork4se.model.Role;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private UUID userId;
    private String fullName;
    private String email;
    private Role role;

    public AuthResponse(String token, UUID userId, String fullName, String email, Role role) {
        this.token = token; this.userId = userId; this.fullName = fullName; this.email = email; this.role = role;
    }
    // getters
}
