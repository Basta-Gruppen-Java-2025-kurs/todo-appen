package se.bastagruppen.todo_appen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.bastagruppen.todo_appen.model.ToDoListCatalog;

public interface ToDoListCatalogRepository extends JpaRepository<ToDoListCatalog, Long> {
}
