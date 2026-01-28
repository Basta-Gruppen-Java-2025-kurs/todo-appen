package se.bastagruppen.todo_appen.Auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import se.bastagruppen.todo_appen.model.User;
import se.bastagruppen.todo_appen.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthLoginIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired UserRepository userRepository;
    @Autowired PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        // Seed a real user with a BCrypt password
        var user = new User();
        user.setUsername("alice");
        user.setPassword(passwordEncoder.encode("password123"));
        userRepository.save(user);
    }

    @Test
    void login_withCorrectCredentials_returnsJwt() throws Exception {
        var result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"username":"alice","password":"password123"}
                        """))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.expiresAt").exists())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        String token = body.split("\"token\"\\s*:\\s*\"")[1].split("\"")[0];

        // basic "looks like JWT" check: header.payload.signature => 2 dots
        assertThat(token.chars().filter(c -> c == '.').count()).isEqualTo(2);
        assertThat(token).isNotBlank();
    }
}
