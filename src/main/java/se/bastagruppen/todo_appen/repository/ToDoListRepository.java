package se.bastagruppen.todo_appen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.bastagruppen.todo_appen.model.ToDoList;

public interface ToDoListRepository extends JpaRepository<ToDoList, Long> {

}
