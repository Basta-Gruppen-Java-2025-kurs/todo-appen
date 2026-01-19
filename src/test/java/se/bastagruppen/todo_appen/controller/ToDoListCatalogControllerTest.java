package se.bastagruppen.todo_appen.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.bastagruppen.todo_appen.dto.ToDoListCatalogResponse;
import se.bastagruppen.todo_appen.service.ToDoListCatalogService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ToDoListCatalogControllerTest {

    @Test
    @DisplayName("GET /api/catalogs should return user's catalogs")
    void getCatalogsForUser() {
        ToDoListCatalogService catalogService = mock(ToDoListCatalogService.class);

        ToDoListCatalogResponse catalog1 = new ToDoListCatalogResponse();
        catalog1.setName("Work Tasks");
        catalog1.setUserId(1L);

        ToDoListCatalogResponse catalog2 = new ToDoListCatalogResponse();
        catalog2.setName("Personal Tasks");
        catalog2.setUserId(1L);

        when(catalogService.getCatalogsForUser(anyLong()))
                .thenReturn(List.of(catalog1, catalog2));

        ToDoListCatalogController controller = new ToDoListCatalogController(catalogService);

        List<ToDoListCatalogResponse> result = controller.getCatalogs(1L);

        assertEquals(2, result.size());
        assertEquals("Work Tasks", result.get(0).getName());
        assertEquals("Personal Tasks", result.get(1).getName());
    }

    @Test
    @DisplayName("POST /api/catalogs should create a new catalog")
    void createCatalog() {
        ToDoListCatalogService catalogService = mock(ToDoListCatalogService.class);

        ToDoListCatalogResponse catalog = new ToDoListCatalogResponse();
        catalog.setName("Work Tasks");

        when(catalogService.createCatalog(anyLong(), anyString()))
                .thenReturn(catalog);

        ToDoListCatalogController controller = new ToDoListCatalogController(catalogService);

        ToDoListCatalogResponse result = controller.createCatalog(1L, "Work Tasks");

        assertEquals("Work Tasks", result.getName());
    }
}

