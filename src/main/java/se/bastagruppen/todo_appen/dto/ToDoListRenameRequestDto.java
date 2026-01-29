package se.bastagruppen.todo_appen.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToDoListRenameRequestDto {

    @NotBlank(message = "cannot be empty")
    private String name;

}
