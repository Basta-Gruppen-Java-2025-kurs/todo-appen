package se.bastagruppen.todo_appen.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import se.bastagruppen.todo_appen.dto.ToDoListResponseDto;
import se.bastagruppen.todo_appen.mapper.ToDoListMapper;
import se.bastagruppen.todo_appen.model.ToDoList;
import se.bastagruppen.todo_appen.model.ToDoListCatalog;
import se.bastagruppen.todo_appen.model.User;
import se.bastagruppen.todo_appen.repository.ToDoListCatalogRepository;
import se.bastagruppen.todo_appen.repository.ToDoListRepository;
import se.bastagruppen.todo_appen.repository.UserRepository;
import se.bastagruppen.todo_appen.service.ToDoListService;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ToDoListControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ToDoListRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ToDoListCatalogRepository catalogRepository;
    @MockitoBean
    private ToDoListService service;

    @Spy
    ToDoListMapper mapper = Mappers.getMapper(ToDoListMapper.class);

    @Autowired
    private ObjectMapper objectMapper;

    private List<ToDoList> toDoLists;
    private ToDoListCatalog catalog;
    private User user;

    @BeforeEach
    public void setUp() {
        user = userRepository.save(new User(null, "Test User", "password1234", null, null));
        catalog = catalogRepository.save(new ToDoListCatalog(null, "Test Catalog", user));
        toDoLists = List.of(new ToDoList(1L, "List 1", Set.of(), catalog, user),
                            new ToDoList(2L, "List 2", Set.of(), catalog, user),
                            new ToDoList(3L, "List 3", Set.of(), catalog, user));
    }

    @AfterEach
    public void tearDown() {
        toDoLists = null;
        catalog = null;
        user = null;
    }

    @Test
    @DisplayName("GET /list returns a list of all ToDo lists")
    void getListShouldReturnAllYourListsTest() throws Exception {
        List<ToDoListResponseDto> responseDtos = toDoLists.stream().map(mapper::toDto).toList();
        when(service.getAllToDoLists()).thenReturn(responseDtos);
        mockMvc.perform(get("/list"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDtos)));
    }

}
