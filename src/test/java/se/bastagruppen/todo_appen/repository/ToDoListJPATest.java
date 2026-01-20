package se.bastagruppen.todo_appen.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.data.jpa.repository.JpaRepository;
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

    @Mock
    private JpaRepository<ToDoListCatalog, Long> catalogRepository;

    @Mock
    private JpaRepository<User, Long> userRepository;

    private User user;
    private ToDoListCatalog catalog;

    static AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        repository.deleteAll();

        ToDoList toDoList = new ToDoList();
        ToDoListCatalog catalog = new ToDoListCatalog();
        catalog.setId(1L);
        User user = new User();
        user.setId(1L);
        Mockito.when(catalogRepository.findById(1L)).thenReturn(Optional.of(catalog));
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
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
        ToDoList savedList = repository.save(toDoList);
        assertNotNull(savedList);
    }

    @AfterAll
    static void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }
}
