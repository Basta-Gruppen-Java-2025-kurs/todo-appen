package se.bastagruppen.todo_appen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.bastagruppen.todo_appen.model.ToDoList;

import java.util.Optional;

public interface ToDoListRepository extends JpaRepository<ToDoList, Long> {

    boolean existsByCatalogIdAndName(Long catalogId, String name);

    @Query("""
    SELECT l FROM ToDoList l
    WHERE l.id = :listId
    AND l.owner.id = :userId
    """)
    Optional<ToDoList> findByIdAndOwnerId(Long listId, Long userId);
}
