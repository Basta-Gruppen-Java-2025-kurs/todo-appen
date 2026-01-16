package se.bastagruppen.todo_appen.repository;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import se.bastagruppen.todo_appen.model.ToDoListCatalog;
import se.bastagruppen.todo_appen.model.User;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ToDoListCatalogRepositoryTest {

    @Autowired
    private ToDoListCatalogRepository catalogRepository;

    private User user1;
    private User user2;
    private ToDoListCatalog savedCatalog;

    @BeforeEach
    void setup() {
        user1 = new User();
        user1.setUsername("user1");

        user2 = new User();
        user2.setUsername("user2");

        ToDoListCatalog catalog1 = new ToDoListCatalog();
        catalog1.setUser(user1);
        catalog1.setName("Work Tasks");

        ToDoListCatalog catalog2 = new ToDoListCatalog();
        catalog2.setUser(user2);
        catalog2.setName("Work Tasks");

        savedCatalog =catalogRepository.save(catalog1);
        catalogRepository.save(catalog2);
    }

    @Test
    @DisplayName("Find catalog by id")
    void findCatalogById() {
        Optional<ToDoListCatalog> found = catalogRepository.findById(savedCatalog.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Work Tasks");
        assertThat(found.get().getUser().getId()).isEqualTo(1L);
    }


}
