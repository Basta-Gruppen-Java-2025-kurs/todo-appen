package se.bastagruppen.todo_appen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToDoListResponseDto {
    private Long id;
    private String name;

}
