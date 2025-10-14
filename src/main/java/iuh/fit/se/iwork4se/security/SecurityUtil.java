package iuh.fit.se.iwork4se.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public final class SecurityUtil {
    private SecurityUtil() {}

    public static UUID currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        // Read from request attributes set by JwtAuthFilter
        var attrs = org.springframework.web.context.request.RequestContextHolder.getRequestAttributes();
        if (attrs instanceof org.springframework.web.context.request.ServletRequestAttributes sra) {
            String id = (String) sra.getRequest().getAttribute("CURRENT_USER_ID");
            if (id != null) return UUID.fromString(id);
        }
        return null;
    }
}


