package iuh.fit.se.iwork4se.config;

import iuh.fit.se.iwork4se.model.User;
import iuh.fit.se.iwork4se.repository.UserRepository;
import org.springframework.context.annotation.Bean; import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class SecurityBeans {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    // Login bằng email hoặc phone, chỉ bắt buộc verified theo kênh đăng nhập
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return (String rawIdentifier) -> {
            String trimmed = rawIdentifier == null ? "" : rawIdentifier.trim();
            boolean loginWithEmail = trimmed.contains("@");

            User u = loginWithEmail
                    ? userRepository.findByEmail(trimmed.toLowerCase())
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"))
                    : userRepository.findByPhone(normalizePhone(trimmed))
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Dev: tạm thời không enforce xác minh để tránh 403 khi demo/login

            var auths = List.of(new SimpleGrantedAuthority("ROLE_" + u.getRole().name()));
            String username = loginWithEmail ? u.getEmail() : u.getPhone();
            return new org.springframework.security.core.userdetails.User(username, u.getPasswordHash(), auths);
        };
    }

    // Chuẩn hoá phone kiểu đơn giản: 090... -> +84...
    private static String normalizePhone(String raw) {
        if (raw == null) return null;
        String s = raw.trim().replaceAll("\\s+", "");
        if (s.contains("@")) return null; // không phải phone
        if (s.startsWith("0")) return "+84" + s.substring(1);
        return s;
    }
}
