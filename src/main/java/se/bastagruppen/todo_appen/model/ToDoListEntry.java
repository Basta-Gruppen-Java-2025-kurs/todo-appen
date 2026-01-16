package se.bastagruppen.todo_appen.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "todo_list_entries")
public class ToDoListEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String summary;
    private String details;
    private Boolean done;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deadline;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ToDoListEntry> subtasks = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    private ToDoListEntry parent;

    // TODO: finnish tags
    // private List<Tag> tags;
}
