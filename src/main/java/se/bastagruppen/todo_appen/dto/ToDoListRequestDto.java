package se.bastagruppen.todo_appen.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToDoListRequestDto {
    private final String CANNOT_BE_NULL = "cannot be null";
    @NotNull(message = CANNOT_BE_NULL)
    @NotBlank(message = "cannot be empty")
    private String name;

    @NotNull(message = CANNOT_BE_NULL)
    private Long userId;

    @NotNull(message = CANNOT_BE_NULL)
    private Long catalogId;
}
