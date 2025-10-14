package iuh.fit.se.iwork4se.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.*; import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component @RequiredArgsConstructor
public class JwtAuthFilter extends GenericFilter {

    private final JwtService jwt;
    private final UserDetailsService userDetailsService;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        String header = request.getHeader("Authorization");

        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                var jws = jwt.parseAndValidate(token);
                Claims claims = jws.getPayload();
                String principal = claims.get("email", String.class);
                if (principal == null) principal = claims.get("phone", String.class);
                // expose current user id for downstream usage
                String subjectUserId = claims.getSubject();
                if (subjectUserId != null) {
                    request.setAttribute("CURRENT_USER_ID", subjectUserId);
                }

                if (principal != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails user = userDetailsService.loadUserByUsername(principal);
                    var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception ignored) {}
        }
        chain.doFilter(req, res);
    }
}
