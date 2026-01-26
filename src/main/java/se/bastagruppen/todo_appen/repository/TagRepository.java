package se.bastagruppen.todo_appen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.bastagruppen.todo_appen.model.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
