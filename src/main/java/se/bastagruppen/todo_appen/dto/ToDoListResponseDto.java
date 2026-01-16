package se.bastagruppen.todo_appen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToDoListResponseDto {
    private Long id;
    private String name;
    private String username;
    private Set<String> tags;
    private String catalog;
}
