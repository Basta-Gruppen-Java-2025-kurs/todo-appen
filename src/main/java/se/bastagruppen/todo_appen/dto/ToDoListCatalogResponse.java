package se.bastagruppen.todo_appen.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ToDoListCatalogResponse {
    private Long id;
    private String name;
    private Long userId;
}
