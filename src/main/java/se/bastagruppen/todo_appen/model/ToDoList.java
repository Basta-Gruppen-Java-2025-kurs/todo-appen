package se.bastagruppen.todo_appen.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "todo_lists", uniqueConstraints = {@UniqueConstraint(name = "UniqueListNamesForCatalog", columnNames = {"name", "catalog_id"})})
@Getter
@Setter
@ToString
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "catalog_id", insertable = false, updatable = false)
    private ToDoListCatalog catalog;

    @Column(name = "catalog_id")
    @JsonProperty("catalog_id")
    private Long catalogId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User owner;

    @Column(name = "user_id")
    @JsonProperty("user_id")
    private Long ownerId;

    public ToDoList(Long id, String name, Set<Tag> tags, Long catalogId, Long ownerId) {
        this.id = id;
        this.name = name;
        this.tags = tags;
        this.catalogId = catalogId;
        this.ownerId = ownerId;
    }
}
