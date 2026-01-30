package se.bastagruppen.todo_appen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.bastagruppen.todo_appen.model.ToDoListCatalog;

import java.util.List;
import java.util.Optional;

public interface ToDoListCatalogRepository extends JpaRepository<ToDoListCatalog, Long> {

    boolean existsByUserIdAndName(Long userId, String name);

    List<ToDoListCatalog> findAllByUserId(Long userId);

    Optional<ToDoListCatalog> findByIdAndUserId(Long id, Long userId);
}
