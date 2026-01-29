package se.bastagruppen.todo_appen.dev;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import se.bastagruppen.todo_appen.model.User;
import se.bastagruppen.todo_appen.repository.UserRepository;

// Temporary for testing login and logout on a local db
/*@Profile("dev")
@Component
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        userRepository.findByUsername("test").orElseGet(() -> {
            User u = new User();
            u.setUsername("test");
            u.setPassword(passwordEncoder.encode("password"));
            return userRepository.save(u);
        });
    }
}
*/