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

    private User user, user2;
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
        toDoList.setCatalogId(catalog.getId());
        toDoList.setOwner(user);
        toDoList.setOwnerId(user.getId());
        toDoList.setName("Test List 1");
        toDoList.setTags(Set.of(tag));
        repository.save(toDoList);

        // add a second user
        User user2ToSave = new User();
        user2ToSave.setUsername("Bullwinkle");
        user2ToSave.setPassword("somepassword123");
        user2 = userRepository.save(user2ToSave);
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
        toDoList.setOwnerId(user.getId());
        toDoList.setCatalogId(catalog.getId());
        ToDoList savedList = repository.save(toDoList);
        assertNotNull(savedList);
        assertEquals(savedList.getName(), toDoList.getName());
    }

    @Test
    @DisplayName("Repository can search by parameters")
    void searchByAllParametersTest() {
        final Long  user1Id = user.getId(),
                user2Id = user2.getId(),
                catalog1Id = catalog.getId();

        List<ToDoList> tl = repository.search(null, null, null, null);
        log.info(tl.toString());
        assertNotNull(tl);
        assertFalse(tl.isEmpty());

        // search with existing userId
        List<ToDoList> tl1 = repository.search(user1Id, null, null, null);
        assertNotNull(tl1);
        assertFalse(tl1.isEmpty());

        // search with wrong userId
        List<ToDoList> tl2 = repository.search(777L, null, null, null);
        assertNotNull(tl2);
        assertTrue(tl2.isEmpty());

        // search with existing catalogId
        List<ToDoList> tl3 = repository.search(null, catalog1Id, null, null);
        assertNotNull(tl3);
        assertFalse(tl3.isEmpty());

        // search with wrong catalogId
        List<ToDoList> tl4 = repository.search(null, 777L, null, null);
        assertNotNull(tl4);
        assertTrue(tl4.isEmpty());

        // search with mismatching userId and catalogId
        List<ToDoList> tl5 = repository.search(user2Id, catalog1Id, null, null);
        assertNotNull(tl5);
        assertTrue(tl5.isEmpty());

        // search with matching userId and catalogId
        List<ToDoList> tl6 = repository.search(user1Id, catalog1Id, null, null);
        assertNotNull(tl6);
        assertFalse(tl6.isEmpty());

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

    @Test
    @DisplayName("Repository can search by a list of tags")
    void searchByListOfTagsTest() {
        // search with empty criteria
        List<ToDoList> tl = repository.searchByTags(null);
        assertNotNull(tl);
        assertFalse(tl.isEmpty());

        // search with an empty list of tags
        List<ToDoList> tl2 = repository.searchByTags(List.of());
        assertNotNull(tl2);
        assertTrue(tl2.isEmpty());

        // search with a tag name that exists
        List<ToDoList> tl3 = repository.searchByTags(List.of(tag.getName()));
        assertNotNull(tl3);
        assertFalse(tl3.isEmpty());
        assertTrue(tl3.stream().map(ToDoList::getTags).allMatch(tags -> tags.stream().map(Tag::getName).anyMatch(tag.getName()::equals)));

        // search with a tag name that doesn't exist
        List<ToDoList> tlDNE = repository.searchByTags(List.of("%sjnj%"));
        assertNotNull(tlDNE);
        assertTrue(tlDNE.isEmpty());
    }

    @Test
    @DisplayName("Repository can search by other parameters than tags")
    void searchByOtherParametersTest() {
        final Long  user1Id = user.getId(),
                    user2Id = user2.getId(),
                    catalog1Id = catalog.getId();

        // search with all nulls
        List<ToDoList> tl = repository.searchByParams(null, null, null);
        assertNotNull(tl);
        assertFalse(tl.isEmpty());
        log.info(tl.toString());

        // search with existing userId
        List<ToDoList> tl1 = repository.searchByParams(user1Id, null, null);
        assertNotNull(tl1);
        assertFalse(tl1.isEmpty());

        // search with wrong userId
        List<ToDoList> tl2 = repository.searchByParams(777L, null, null);
        assertNotNull(tl2);
        assertTrue(tl2.isEmpty());

        // search with existing catalogId
        List<ToDoList> tl3 = repository.searchByParams(null, catalog1Id, null);
        assertNotNull(tl3);
        assertFalse(tl3.isEmpty());

        // search with wrong catalogId
        List<ToDoList> tl4 = repository.searchByParams(null, 777L, null);
        assertNotNull(tl4);
        assertTrue(tl4.isEmpty());

        // search with mismatching userId and catalogId
        List<ToDoList> tl5 = repository.searchByParams(user2Id, catalog1Id, null);
        assertNotNull(tl5);
        assertTrue(tl5.isEmpty());

        // search with matching userId and catalogId
        List<ToDoList> tl6 = repository.searchByParams(user1Id, catalog1Id, null);
        assertNotNull(tl6);
        assertFalse(tl6.isEmpty());

        // search with filter

        // search with filter and userId

        // search with filter and catalogId

        // search with all params
    }

}
