package se.bastagruppen.todo_appen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.bastagruppen.todo_appen.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
