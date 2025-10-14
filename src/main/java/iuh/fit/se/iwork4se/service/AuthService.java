
package iuh.fit.se.iwork4se.service;

import iuh.fit.se.iwork4se.dto.auth.*;

public interface AuthService {
    RegisterResponse register(RegisterRequest req);
    AuthTokens login(LoginRequest req);
    AuthTokens refresh(RefreshRequest req);
}
