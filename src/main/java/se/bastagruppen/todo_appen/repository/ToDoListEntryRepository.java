package se.bastagruppen.todo_appen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.bastagruppen.todo_appen.model.ToDoListEntry;

import java.util.List;
import java.util.Optional;

public interface ToDoListEntryRepository extends JpaRepository<ToDoListEntry, Long> {
    List<ToDoListEntry> findByListIdAndParentIsNull(Long listId);

    @Query("""
            SELECT e FROM ToDoListEntry e
            JOIN FETCH e.list l
            WHERE e.id = :entryId AND l.owner.id = :userId
            """)
    Optional<ToDoListEntry> findByIdWithOwner(@Param("entryId") Long entryId,
                                              @Param("userId") Long userId);

    @Query("""
        SELECT e FROM ToDoListEntry e
        WHERE e.list.id = :listId
        AND e.list.owner.id = :userId
        AND e.parent IS NULL
        """)
    List<ToDoListEntry> findRootEntriesByListIdAndOwnerId(Long listId, Long userId);

}
