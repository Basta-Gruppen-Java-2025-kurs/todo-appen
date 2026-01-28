package se.bastagruppen.todo_appen.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.bastagruppen.todo_appen.service.AuthService;
import se.bastagruppen.todo_appen.dto.LoginRequest;
import se.bastagruppen.todo_appen.dto.LoginResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        try {
            var result = authService.login(req.username(), req.password());
            return ResponseEntity.ok(new LoginResponse(result.token(), result.expiresAt()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }
        String token = authHeader.substring("Bearer ".length());
        authService.logout(token);
        return ResponseEntity.noContent().build();
    }

    // Helpful protected endpoint for testing JWT quickly
    @GetMapping("/me")
    public ResponseEntity<String> me() {
        return ResponseEntity.ok("OK");
    }
}