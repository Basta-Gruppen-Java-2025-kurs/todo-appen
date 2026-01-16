package se.bastagruppen.todo_appen.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import se.bastagruppen.todo_appen.model.ToDoListCatalog;
import se.bastagruppen.todo_appen.repository.ToDoListCatalogRepository;

import java.util.List;

@Service
@Transactional
public class ToDoListCatalogService {

    private final ToDoListCatalogRepository catalogRepository;

    public ToDoListCatalogService(ToDoListCatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    public ToDoListCatalog createCatalog(Long userId, String name) {

        //TODO: get User from userRepository
        /*User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));*/

        /*if (catalogRepository.existsByUserIdAndName(userId, name)) {
            throw new IllegalArgumentException("Catalog with this name already exists for user");
        }*/

        ToDoListCatalog catalog = new ToDoListCatalog();
        // catalog.setUser(user);
        catalog.setName(name);

        return catalogRepository.save(catalog);
    }

    public List<ToDoListCatalog> getCatalogsForUser(Long userId) {
        return catalogRepository.findAllByUserId(userId);
    }
}
