package se.bastagruppen.todo_appen.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoListEntryRepository extends JpaRepository<ToDoListEntry, Long> {
}
