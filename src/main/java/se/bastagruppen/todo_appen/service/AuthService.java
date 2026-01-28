package se.bastagruppen.todo_appen.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.bastagruppen.todo_appen.security.JwtService;
import se.bastagruppen.todo_appen.model.RevokedToken;
import se.bastagruppen.todo_appen.repository.RevokedTokenRepository;
import se.bastagruppen.todo_appen.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RevokedTokenRepository revokedTokenRepository;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RevokedTokenRepository revokedTokenRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.revokedTokenRepository = revokedTokenRepository;
    }

    public JwtService.TokenResult login(String username, String password) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return jwtService.generateToken(user.getId(), user.getUsername());
    }

    public void logout(String token) {
        var claims = jwtService.parseClaims(token);
        String jti = claims.getId();
        var expiresAt = claims.getExpiration().toInstant();
        revokedTokenRepository.save(new RevokedToken(jti, expiresAt));
    }
}