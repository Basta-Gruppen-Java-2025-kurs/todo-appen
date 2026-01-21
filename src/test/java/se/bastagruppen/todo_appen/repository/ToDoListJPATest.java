package se.bastagruppen.todo_appen.repository;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import se.bastagruppen.todo_appen.model.ToDoList;
import se.bastagruppen.todo_appen.model.ToDoListCatalog;
import se.bastagruppen.todo_appen.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ExtendWith(MockitoExtension.class)
public class ToDoListJPATest {

    @Autowired
    private ToDoListRepository repository;

    @Autowired
    private ToDoListCatalogRepository catalogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    private User user;
    private ToDoListCatalog catalog;

    static AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        catalogRepository.deleteAll();
        userRepository.deleteAll();
        tagRepository.deleteAll();

        User userToSave = new User();
        userToSave.setUsername("TestUser");
        user = userRepository.save(userToSave);

        ToDoListCatalog catalogToSave = new ToDoListCatalog();
        catalogToSave.setName("Catalog 1");
        catalogToSave.setUser(user);
        catalog = catalogRepository.save(catalogToSave);

        ToDoList toDoList = new ToDoList();
        toDoList.setCatalog(catalog);
        toDoList.setOwner(user);
        toDoList.setName("Test List 1");
        repository.save(toDoList);
    }

    @Test
    @DisplayName("Repository contains the initially created list")
    void findAllTest() {
        List<ToDoList> list = repository.findAll();
        assertNotNull(list);
        assertFalse(list.isEmpty());
        List<String> todoListNames = list.stream().map(ToDoList::getName).toList();
        assertTrue(todoListNames.contains("Test List 1"));
    }

    @Test
    @DisplayName("Repository saves and retrieves a new list")
    void basicPersistenceTest() {
        ToDoList toDoList = new ToDoList();
        toDoList.setName("Abracadabra");
        toDoList.setOwner(user);
        toDoList.setCatalog(catalog);
        ToDoList savedList = repository.save(toDoList);
        assertNotNull(savedList);
        assertEquals(savedList.getName(), toDoList.getName());
    }

}
