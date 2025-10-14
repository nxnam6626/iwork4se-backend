
package iuh.fit.se.iwork4se.dto.auth;

import iuh.fit.se.iwork4se.model.Role;

import java.util.UUID;

public record RegisterResponse(
        UUID userId,
        String email,
        String phone,
        String fullName,
        Role role
) {}
