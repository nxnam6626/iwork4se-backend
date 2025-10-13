package iuh.fit.se.iwork4se.service.impl;

import iuh.fit.se.iwork4se.dto.AuthResponse;
import iuh.fit.se.iwork4se.dto.LoginRequest;
import iuh.fit.se.iwork4se.dto.RegisterRequest;
import iuh.fit.se.iwork4se.model.Role;
import iuh.fit.se.iwork4se.model.User;
import iuh.fit.se.iwork4se.repository.UserRepository;
import iuh.fit.se.iwork4se.service.AuthService;
import iuh.fit.se.iwork4se.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;
    private final AuthenticationManager authManager;

    public AuthServiceImpl(UserRepository userRepo, PasswordEncoder encoder, JwtUtil jwt,
                           AuthenticationManager authManager) {
        this.userRepo = userRepo; this.encoder = encoder; this.jwt = jwt; this.authManager = authManager;
    }

    @Override
    public AuthResponse register(RegisterRequest req) {
        if (userRepo.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        User u = new User();
        u.setFullName(req.getFullName());
        u.setEmail(req.getEmail().toLowerCase().trim());
        u.setPasswordHash(encoder.encode(req.getPassword()));
        u.setRole(req.getRole() == null ? Role.JOBSEEKER : req.getRole());
        userRepo.save(u);

        String token = jwt.generateToken(u.getEmail());
        return new AuthResponse(token, u.getUserId(), u.getFullName(), u.getEmail(), u.getRole());
    }

    @Override
    public AuthResponse login(LoginRequest req) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid email or password");
        }
        User u = userRepo.findByEmail(req.getEmail()).orElseThrow();
        String token = jwt.generateToken(u.getEmail());
        return new AuthResponse(token, u.getUserId(), u.getFullName(), u.getEmail(), u.getRole());
    }
}
