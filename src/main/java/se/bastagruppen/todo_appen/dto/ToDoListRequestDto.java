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
    @NotNull(message = "cannot be null")
    @NotBlank(message = "cannot be empty")
    private String name;
    private Long userId;
}
