package se.bastagruppen.todo_appen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.bastagruppen.todo_appen.model.RevokedToken;

import java.time.Instant;

public interface RevokedTokenRepository extends JpaRepository<RevokedToken, Long> {
    boolean existsByJtiAndExpiresAtAfter(String jti, Instant now);
}