package se.bastagruppen.todo_appen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.bastagruppen.todo_appen.model.ToDoListEntry;

import java.util.List;

public interface ToDoListEntryRepository extends JpaRepository<ToDoListEntry, Long> {
    List<ToDoListEntry> findByListIdAndParentIsNull(Long listId);
}
