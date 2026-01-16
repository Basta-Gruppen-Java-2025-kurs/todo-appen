package se.bastagruppen.todo_appen.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "todo_list_entries")
public class ToDoListEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String summary;
    private String details;
    private Boolean done;
    private Date deadline;
    private List<ToDoListEntry> subtasks;
    // private List<Tag> tags;


    public ToDoListEntry() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public List<ToDoListEntry> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<ToDoListEntry> subtasks) {
        this.subtasks = subtasks;
    }
}
