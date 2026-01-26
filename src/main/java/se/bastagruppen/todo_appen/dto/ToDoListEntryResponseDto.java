package se.bastagruppen.todo_appen.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToDoListEntryResponseDto {
    @NotNull(message = "Id can not be null")
    private Long id;
    @NotNull(message = "Summary can not be null")
    private String summary;
    private String details;
    private Boolean done;
    private LocalDate deadline;
    @NotNull(message = "List id can not be null")
    private Long listId;
    private Long parentId;
    private List<ToDoListEntryResponseDto> subtasks;
}
