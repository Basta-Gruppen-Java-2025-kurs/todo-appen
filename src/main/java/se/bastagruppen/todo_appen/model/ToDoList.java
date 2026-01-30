package se.bastagruppen.todo_appen.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
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

    @ManyToOne(fetch = FetchType.EAGER, optional = false, targetEntity = ToDoListCatalog.class)
    @JoinColumn(name = "catalog_id", insertable = false, updatable = false)
    private ToDoListCatalog catalog;

    @Column(name = "catalog_id", nullable = false)
    @JsonProperty("catalog_id")
    private Long catalogId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, targetEntity = User.class)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User owner;

    @Column(name = "user_id", nullable = false)
    @JsonProperty("user_id")
    private Long ownerId;

    @OneToMany(
            mappedBy = "list",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ToDoListEntry> entries = new ArrayList<>();

    public ToDoList(Long id, String name, Set<Tag> tags, Long catalogId, Long ownerId) {
        this.id = id;
        this.name = name;
        this.tags = tags;
        this.catalogId = catalogId;
        this.ownerId = ownerId;
    }

    public ToDoList(Long id, String name, Set<Tag> tags, ToDoListCatalog catalog, User owner) {
        this.id = id;
        this.name = name;
        this.tags = tags;
        this.catalog = catalog;
        this.catalogId = catalog.getId();
        this.owner = owner;
        this.ownerId = owner.getId();
    }
}
