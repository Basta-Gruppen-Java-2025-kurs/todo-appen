package se.bastagruppen.todo_appen.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import se.bastagruppen.todo_appen.dto.ToDoListCatalogResponse;
import se.bastagruppen.todo_appen.model.ToDoListCatalog;
import se.bastagruppen.todo_appen.model.User;
import se.bastagruppen.todo_appen.repository.ToDoListCatalogRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ToDoListCatalogService {

    private final ToDoListCatalogRepository catalogRepository;

    public ToDoListCatalogService(ToDoListCatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    public ToDoListCatalogResponse createCatalog(Long userId, String name) {

        //TODO: get User from userRepository
        /*User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));*/

        /*if (catalogRepository.existsByUserIdAndName(userId, name)) {
            throw new IllegalArgumentException("Catalog with this name already exists for user");
        }*/

        //Dummy user remove later
        User user = new User();
        user.setId(1L);
        user.setUsername("Dummy user");

        ToDoListCatalog catalog = new ToDoListCatalog();
        catalog.setUser(user);
        catalog.setName(name);

        catalogRepository.save(catalog);

        return mapToResponse(catalog);
    }

    public List<ToDoListCatalogResponse> getCatalogsForUser(Long userId) {

        return catalogRepository.findAllByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ToDoListCatalogResponse mapToResponse(ToDoListCatalog catalog) {
        return new ToDoListCatalogResponse(catalog.getId(), catalog.getName(), catalog.getUser().getId());
    }
}
