package se.bastagruppen.todo_appen.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import se.bastagruppen.todo_appen.repository.RevokedTokenRepository;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final RevokedTokenRepository revokedTokenRepository;

    public JwtAuthFilter(JwtService jwtService, RevokedTokenRepository revokedTokenRepository) {
        this.jwtService = jwtService;
        this.revokedTokenRepository = revokedTokenRepository;
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/auth/") || path.startsWith("/h2/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring("Bearer ".length());

        try {
            Claims claims = jwtService.parseClaims(token);

            String jti = claims.getId();
            if (jti != null && revokedTokenRepository.existsByJtiAndExpiresAtAfter(jti, Instant.now())) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            Long userId = Long.valueOf(claims.getSubject());
            String username = claims.get("username", String.class);

            CustomPrincipal principal = new CustomPrincipal(userId, username);

            var auth = new UsernamePasswordAuthenticationToken(
                    principal,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );

            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}