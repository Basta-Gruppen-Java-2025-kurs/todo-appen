package se.bastagruppen.todo_appen.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import se.bastagruppen.todo_appen.dto.ToDoListCatalogResponse;
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
        ToDoListCatalogResponse catalog1 = new ToDoListCatalogResponse();
        catalog1.setName("Work Tasks");
        catalog1.setUserId(1L);

        ToDoListCatalogResponse catalog2 = new ToDoListCatalogResponse();
        catalog2.setName("Personal Tasks");
        catalog2.setUserId(1L);

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

        ToDoListCatalogResponse catalog = new ToDoListCatalogResponse();
        catalog.setName("Work Tasks");

        when(catalogService.createCatalog(anyLong(), anyString())).thenReturn(catalog);

        mockMvc.perform(post("/api/catalogs")
                        .param("userId", "1")
                        .param("name", "Work Tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Work Tasks"));
    }
}

