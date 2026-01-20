package se.bastagruppen.todo_appen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToDoListEntryResponseDto {
    private Long id;
    private String summary;
    private String details;
    private Boolean done;
    private LocalDate deadline;
    private Long parentId;
    private List<ToDoListEntryResponseDto> subtasks;
}
