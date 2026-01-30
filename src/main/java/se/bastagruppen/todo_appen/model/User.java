package se.bastagruppen.todo_appen.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Table(
        name = "users",
        uniqueConstraints = @UniqueConstraint(columnNames = "username"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user")
    private List<ToDoListCatalog> catalogs;

    @OneToMany(mappedBy = "owner")
    private List<Tag> tags;
}
