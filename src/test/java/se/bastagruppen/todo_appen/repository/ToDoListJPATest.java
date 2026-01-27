package se.bastagruppen.todo_appen.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import se.bastagruppen.todo_appen.model.ToDoList;
import se.bastagruppen.todo_appen.model.Tag;
import se.bastagruppen.todo_appen.model.ToDoListCatalog;
import se.bastagruppen.todo_appen.model.User;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
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
    private Tag tag;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        catalogRepository.deleteAll();
        userRepository.deleteAll();
        tagRepository.deleteAll();

        User userToSave = new User();
        userToSave.setUsername("TestUser");
        userToSave.setPassword("qiuow1111");
        user = userRepository.save(userToSave);

        ToDoListCatalog catalogToSave = new ToDoListCatalog();
        catalogToSave.setName("Catalog 1");
        catalogToSave.setUser(user);
        catalog = catalogRepository.save(catalogToSave);

        Tag tagToSave = new Tag();
        tagToSave.setName("Test Tag");
        tagToSave.setOwner(user);
        tag = tagRepository.save(tagToSave);

        ToDoList toDoList = new ToDoList();
        toDoList.setCatalog(catalog);
        toDoList.setOwner(user);
        toDoList.setName("Test List 1");
        toDoList.setTags(Set.of(tag));
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

    @Test
    @DisplayName("Repository can search by parameters")
    void searchByParametersTest() {
        List<ToDoList> tl = repository.search(null, null, null, null);
        log.info(tl.toString());
        assertNotNull(tl);
        assertFalse(tl.isEmpty());
    }

    @Test
    @DisplayName("Repository can search by one tag")
    void searchByOneTagTest() {
        List<ToDoList> tl = repository.searchByTag(null);
        log.info(tl.toString());
        assertNotNull(tl);
        assertFalse(tl.isEmpty());

        List<ToDoList> tl2 = repository.searchByTag("%");
        log.info(tl2.toString());
        assertNotNull(tl2);
        assertFalse(tl2.isEmpty());

        List<ToDoList> tl3 = repository.searchByTag("%est%");
        log.info(tl3.toString());
        assertNotNull(tl3);
        assertFalse(tl3.isEmpty());

        List<ToDoList> tl4 = repository.searchByTag("%qog%");
        log.info(tl4.toString());
        assertNotNull(tl4);
        assertTrue(tl4.isEmpty());

        List<ToDoList> tl5 = repository.searchByTag(tag.getName());
        log.info(tl5.toString());
        assertNotNull(tl5);
        assertFalse(tl5.isEmpty());
    }

}
