// dto/auth/AuthTokens.java
package iuh.fit.se.iwork4se.dto.auth;

public record AuthTokens(
        String accessToken,
        String refreshToken,
        long   expiresIn // seconds cho access token
) {}
