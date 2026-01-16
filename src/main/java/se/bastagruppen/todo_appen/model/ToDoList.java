package se.bastagruppen.todo_appen.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "todo_lists", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "catalog_id"})})
@Getter
@Setter
public class ToDoList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable( name = "tags_todo_lists",
                joinColumns = @JoinColumn(name = "todo_list_id"),
                inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @JsonManagedReference
    private Set<Tag> tags;

    @ManyToOne
    @JoinColumn(name = "catalog_id")
    private ToDoListCatalog catalog;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;
}
