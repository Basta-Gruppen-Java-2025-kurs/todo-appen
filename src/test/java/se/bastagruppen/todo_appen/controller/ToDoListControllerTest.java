package se.bastagruppen.todo_appen.controller;

import org.mapstruct.factory.Mappers;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import se.bastagruppen.todo_appen.mapper.ToDoListMapper;
import se.bastagruppen.todo_appen.repository.ToDoListRepository;
import se.bastagruppen.todo_appen.service.ToDoListService;
import tools.jackson.databind.ObjectMapper;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ToDoListControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ToDoListRepository repository;
    @MockitoBean
    private ToDoListService service;

    @Spy
    ToDoListMapper mapper = Mappers.getMapper(ToDoListMapper.class);

    @Autowired
    private ObjectMapper objectMapper;

    //@Test
    //@DisplayName("")

}
