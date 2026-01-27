package se.bastagruppen.todo_appen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.bastagruppen.todo_appen.model.ToDoList;

import java.util.List;

public interface ToDoListRepository extends JpaRepository<ToDoList, Long> {
    @Query("SELECT DISTINCT tl FROM ToDoList tl JOIN tl.tags t " +
                                        "WHERE (:userId IS NULL OR tl.owner.id = :userId) " +
                                        "AND (:catalogId IS NULL OR tl.catalog.id = :catalogId) " +
                                        "AND (:filter IS NULL OR tl.name LIKE :filter) " +
                                        "AND (COALESCE(:tags) IS NULL OR t.name IN (:tags))")
    List<ToDoList> search(@Param("userId") Long userId, @Param("catalogId") Long catalogId, @Param("filter") String filter, @Param("tags") List<String> tags);

    @Query("SELECT DISTINCT tl FROM ToDoList tl JOIN tl.tags t " +
            "WHERE (:userId IS NULL OR tl.owner.id = :userId) " +
            "AND (:catalogId IS NULL OR tl.catalog.id = :catalogId) " +
            "AND (:filter IS NULL OR tl.name LIKE :filter)")
    List<ToDoList> searchByParams(@Param("userId") Long userId, @Param("catalogId") Long catalogId, @Param("filter") String filter);

    @Query("SELECT DISTINCT tl FROM ToDoList tl JOIN tl.tags t " +
            "WHERE (:tag IS NULL OR t.name LIKE (:tag))")
    List<ToDoList> searchByTag(@Param("tag") String tag);

    List<ToDoList> findByCatalogId(@Param("catalogId") Long catalogId);

    List<ToDoList> findByOwnerId(@Param("userId") Long userId);

    @Query("SELECT DISTINCT tl FROM ToDoList tl JOIN tl.tags t " +
            "WHERE (:tags IS NULL OR t.name IN (:tags))")
    List<ToDoList> searchByTags(@Param("tags") List<String> tags);
}
