package se.bastagruppen.todo_appen.Auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import se.bastagruppen.todo_appen.model.User;
import se.bastagruppen.todo_appen.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthLoginWrongPasswordIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired UserRepository userRepository;
    @Autowired PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        var user = new User();
        user.setUsername("alice");
        user.setPassword(passwordEncoder.encode("password123"));
        userRepository.save(user);
    }

    @Test
    void login_withWrongPassword_returnsUnauthorized() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"username":"alice","password":"WRONG"}
                        """))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));
    }
}
