package se.bastagruppen.todo_appen.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.bastagruppen.todo_appen.dto.ToDoListCatalogResponse;
import se.bastagruppen.todo_appen.model.ToDoListCatalog;
import se.bastagruppen.todo_appen.model.User;
import se.bastagruppen.todo_appen.repository.ToDoListCatalogRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ToDoListCatalogServiceTest {

    @Mock
    private ToDoListCatalogRepository catalogRepository;

    @InjectMocks
    private ToDoListCatalogService catalogService;

    private User user;
    private ToDoListCatalog catalog;

    @BeforeEach
    void setup() {
        user = new User();
        user.setUsername("user1");
        user.setId(1L);

        catalog = new ToDoListCatalog();
        catalog.setName("Work Tasks");
        catalog.setUser(user);
    }

    @Test
    @DisplayName("Creating a catalog calls repository save")
    void createCatalogCallsRepositorySave() {
        //TODO: remove comment when existByUserAndName is used
        /*when(catalogRepository.existsByUserIdAndName(user.getId(), "Work Tasks"))
                .thenReturn(false);*/

        when(catalogRepository.save(any(ToDoListCatalog.class)))
                .thenReturn(catalog);

        ToDoListCatalogResponse result = catalogService.createCatalog(user.getId(), "Work Tasks");

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Work Tasks");
        verify(catalogRepository, times(1)).save(any(ToDoListCatalog.class));
    }


    //TODO: remove comment when existByUserAndName is used
    /*@Test
    @DisplayName("Creating a catalog with duplicate name throws exception")
    void createDuplicateCatalogThrowsException() {
        when(catalogRepository.existsByUserIdAndName(user.getId(), "Work Tasks"))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> catalogService.createCatalog(user.getId(), "Work Tasks"));

        verify(catalogRepository, never()).save(any());
    }
*/
    @Test
    @DisplayName("Get all catalogs for a user")
    void getAllCatalogsForUser() {
        ToDoListCatalog catalog2 = new ToDoListCatalog();
        catalog2.setName("Personal Tasks");
        catalog2.setUser(user);

        when(catalogRepository.findAllByUserId(user.getId()))
                .thenReturn(Arrays.asList(catalog, catalog2));

        List<ToDoListCatalogResponse> result = catalogService.getCatalogsForUser(user.getId());

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Work Tasks");
        assertThat(result.get(1).getName()).isEqualTo("Personal Tasks");
    }
}
