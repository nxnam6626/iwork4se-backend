// src/main/java/iuh/fit/se/iwork4se/service/impl/AuthServiceImpl.java
package iuh.fit.se.iwork4se.service.impl;

import iuh.fit.se.iwork4se.dto.auth.*;
import iuh.fit.se.iwork4se.model.JobSeekerProfile;
import iuh.fit.se.iwork4se.model.RefreshToken;
import iuh.fit.se.iwork4se.model.Role;
import iuh.fit.se.iwork4se.model.User;
import iuh.fit.se.iwork4se.repository.JobSeekerProfileRepository;
import iuh.fit.se.iwork4se.repository.RefreshTokenRepository;
import iuh.fit.se.iwork4se.repository.UserRepository;
import iuh.fit.se.iwork4se.security.JwtService;
import iuh.fit.se.iwork4se.service.AuthService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwt;

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest req) {
        // 1) Validate bắt buộc
        if (!StringUtils.hasText(req.password()) || !StringUtils.hasText(req.fullName())) {
            throw new IllegalArgumentException("Password and fullName are required");
        }

        // 2) Ít nhất email hoặc phone
        if (!StringUtils.hasText(req.email()) && !StringUtils.hasText(req.phone())) {
            throw new IllegalArgumentException("Either email or phone must be provided");
        }

        // 3) Chuẩn hoá phone (E.164 đơn giản - tuỳ bạn thay thế bằng lib)
        String normalizedPhone = normalizePhone(req.phone());

        // 4) Check trùng
        if (StringUtils.hasText(req.email()) && userRepository.existsByEmail(req.email().trim().toLowerCase())) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (StringUtils.hasText(normalizedPhone) && userRepository.existsByPhone(normalizedPhone)) {
            throw new IllegalArgumentException("Phone already in use");
        }

        // 5) Quy tắc chọn role
        Role role = req.role() == null ? Role.JOBSEEKER : req.role();
        if (role == Role.ADMIN) {
            // Chặn đăng ký admin ở public endpoint
            throw new IllegalArgumentException("Role ADMIN cannot be self-registered");
        }

        // 6) Tạo user
		User user = User.builder()
				.email(StringUtils.hasText(req.email()) ? req.email().trim().toLowerCase() : null)
				.phone(StringUtils.hasText(normalizedPhone) ? normalizedPhone : null)
				.passwordHash(passwordEncoder.encode(req.password()))
				.fullName(req.fullName().trim())
				.role(role)
				// Dev mode: tự xác minh nếu có cung cấp email/phone
				.emailVerified(StringUtils.hasText(req.email()))
				.phoneVerified(StringUtils.hasText(normalizedPhone))
				.build();

        user = userRepository.save(user);

        // 7) Tự tạo JobSeekerProfile khi role=JOBSEEKER (nếu bạn muốn)
        if (role == Role.JOBSEEKER) {
            JobSeekerProfile profile = JobSeekerProfile.builder()
                    .user(user)
                    .headline(null)
                    .summary(null)
                    .location(null)
                    .yearsExp(0)
                    .build();
            jobSeekerProfileRepository.save(profile);
        }

        return new RegisterResponse(
                user.getUserId(),
                user.getEmail(),
                user.getPhone(),
                user.getFullName(),
                user.getRole()
        );
    }

    @Override @Transactional
    public AuthTokens login(LoginRequest req) {
        String raw = req.identifier() == null ? "" : req.identifier().trim();
        boolean loginWithEmail = raw.contains("@");
        String normalizedIdentifier = loginWithEmail ? raw.toLowerCase() : normalizePhone(raw);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(normalizedIdentifier, req.password())
            );
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Invalid credentials");
        }

        User user = loginWithEmail
                ? userRepository.findByEmail(normalizedIdentifier)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"))
                : userRepository.findByPhone(normalizedIdentifier)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Đã enforced verified trong UserDetailsService, tới đây coi như OK

        String access = jwt.generateAccessToken(
                user.getUserId(), user.getEmail(), user.getPhone(), user.getRole().name()
        );
        var rt = jwt.generateRefreshToken(user.getUserId());

        refreshTokenRepository.save(RefreshToken.builder()
                .jti(rt.jti())
                .userId(user.getUserId())
                .issuedAt(Instant.now())
                .expiresAt(jwt.parseAndValidate(rt.token()).getPayload().getExpiration().toInstant())
                .revoked(false)
                .build());

        return new AuthTokens(access, rt.token(), 900);
    }

    @Override @Transactional
    public AuthTokens refresh(RefreshRequest req) {
        var jws = jwt.parseAndValidate(req.refreshToken());
        String jti = jws.getPayload().getId();
        UUID userId = UUID.fromString(jws.getPayload().getSubject());

        RefreshToken record = refreshTokenRepository.findById(jti)
                .orElseThrow(() -> new BadCredentialsException("Refresh token not recognized"));

        if (record.isRevoked() || record.getExpiresAt().isBefore(Instant.now())) {
            throw new BadCredentialsException("Refresh token expired or revoked");
        }

        // rotate
        record.setRevoked(true); record.setRevokedAt(Instant.now());
        refreshTokenRepository.save(record);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String newAccess = jwt.generateAccessToken(
                user.getUserId(), user.getEmail(), user.getPhone(), user.getRole().name()
        );
        var newRt = jwt.generateRefreshToken(user.getUserId());
        refreshTokenRepository.save(RefreshToken.builder()
                .jti(newRt.jti())
                .userId(user.getUserId())
                .issuedAt(Instant.now())
                .expiresAt(jwt.parseAndValidate(newRt.token()).getPayload().getExpiration().toInstant())
                .revoked(false)
                .build());

        return new AuthTokens(newAccess, newRt.token(), 900);
    }



    // Ví dụ normalize rất đơn giản: +84… (tuỳ bạn thay thế bằng lib Google libphonenumber)
    private String normalizePhone(String raw) {
        if (!StringUtils.hasText(raw)) return null;
        String s = raw.trim().replaceAll("\\s+", "");
        // ví dụ: nếu bắt đầu bằng 0 và là số VN -> đổi 0 -> +84
        if (s.startsWith("0")) {
            return "+84" + s.substring(1);
        }
        // nếu đã có +... thì để nguyên
        return s;
    }
}
