package se.bastagruppen.todo_appen.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import se.bastagruppen.todo_appen.model.ToDoListCatalog;
import se.bastagruppen.todo_appen.model.User;
import se.bastagruppen.todo_appen.service.ToDoListCatalogService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ToDoListCatalogController.class)
@AutoConfigureMockMvc(addFilters = false)
class ToDoListCatalogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ToDoListCatalogService catalogService;

    @Test
    @DisplayName("GET /api/catalogs should return user's catalogs")
    void getCatalogsForUser() throws Exception {
        User user = new User();
        user.setUsername("user1");

        ToDoListCatalog catalog1 = new ToDoListCatalog();
        catalog1.setName("Work Tasks");
        catalog1.setUser(user);

        ToDoListCatalog catalog2 = new ToDoListCatalog();
        catalog2.setName("Personal Tasks");
        catalog2.setUser(user);

        when(catalogService.getCatalogsForUser(anyLong()))
                .thenReturn(List.of(catalog1, catalog2));

        mockMvc.perform(get("/api/catalogs")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Work Tasks"))
                .andExpect(jsonPath("$[1].name").value("Personal Tasks"));
    }

    @Test
    @DisplayName("POST /api/catalogs should create a new catalog")
    void createCatalog() throws Exception {
        User user = new User();
        user.setUsername("user1");

        ToDoListCatalog catalog = new ToDoListCatalog();
        catalog.setName("Work Tasks");
        catalog.setUser(user);

        when(catalogService.createCatalog(anyLong(), anyString())).thenReturn(catalog);

        mockMvc.perform(post("/api/catalogs")
                        .param("userId", "1")
                        .param("name", "Work Tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Work Tasks"))
                .andExpect(jsonPath("$.user.username").value("user1"));
    }
}

