package se.bastagruppen.todo_appen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.bastagruppen.todo_appen.model.ToDoListCatalog;

import java.util.List;

public interface ToDoListCatalogRepository extends JpaRepository<ToDoListCatalog, Long> {

    boolean existsByUserIdAndName(Long userId, String name);

    List<ToDoListCatalog> findAllByUserId(Long userId);
}
