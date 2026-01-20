package se.bastagruppen.todo_appen.exception;

public class ToDoListNotFoundException extends RuntimeException {
    public ToDoListNotFoundException(Long id) {
        super("TODO list # " + id + " not found.");
    }
}
