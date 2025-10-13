package iuh.fit.se.iwork4se.service;

import iuh.fit.se.iwork4se.dto.AuthResponse;
import iuh.fit.se.iwork4se.dto.LoginRequest;
import iuh.fit.se.iwork4se.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest req);
    AuthResponse login(LoginRequest req);
}
