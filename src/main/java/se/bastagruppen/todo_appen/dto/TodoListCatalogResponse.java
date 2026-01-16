package se.bastagruppen.todo_appen.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TodoListCatalogResponse {
    private Long id;
    private String name;
    private Long userId;
}
