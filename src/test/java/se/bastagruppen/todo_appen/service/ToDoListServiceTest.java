package se.bastagruppen.todo_appen.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import se.bastagruppen.todo_appen.dto.ToDoListResponseDto;
import se.bastagruppen.todo_appen.mapper.ToDoListMapper;
import se.bastagruppen.todo_appen.model.Tag;
import se.bastagruppen.todo_appen.model.ToDoList;
import se.bastagruppen.todo_appen.model.ToDoListCatalog;
import se.bastagruppen.todo_appen.model.User;
import se.bastagruppen.todo_appen.repository.ToDoListRepository;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ToDoListServiceTest {

    @Mock
    private ToDoListRepository repository;

    @InjectMocks
    private ToDoListService service;

    @Spy
    private ToDoListMapper mapper = Mappers.getMapper(ToDoListMapper.class);

    private User testUser;
    private ToDoListCatalog testCatalog;
    private ToDoList testToDoList;
    private Tag testTag;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "Tester Joe", "qiuow1111", List.of(), List.of());
        testTag = new Tag(1L, "test tag", testUser);
        testCatalog = new ToDoListCatalog(1L, "Test Catalog", testUser);
        testToDoList = new ToDoList(1L, "Test TODO list", Set.of(testTag), testCatalog, testUser);
    }

    @Test
    @DisplayName("Getting a TODO list by Id from repository should yield the correct list")
    void getToDoListByIdTest() {
        when(repository.findById(1L)).thenReturn(java.util.Optional.of(testToDoList));
        ToDoListResponseDto responseDto = service.getById(1L);
        assertEquals(testToDoList.getId(), responseDto.getId());
        assertEquals(testToDoList.getName(), responseDto.getName());
        assertEquals(testToDoList.getCatalog().getName(), responseDto.getCatalogName());
        assertEquals(testToDoList.getOwner().getUsername(), responseDto.getUsername());
    }

    @Test
    @DisplayName("Searching a TODO list by sets of parameters should call repository with correct parameters")
    void searchTest() {
        // all parameters are null
        when(repository.search(null, null, null, null)).thenReturn(List.of(testToDoList));
        List<ToDoListResponseDto> tls = service.search(null, null, null, null);
        assertNotNull(tls);
        assertFalse(tls.isEmpty());
        assertEquals(testToDoList.getId(), tls.getFirst().getId());
        // filter not empty

        // all parameters are set
        // each of the parameters is set
    }
}
