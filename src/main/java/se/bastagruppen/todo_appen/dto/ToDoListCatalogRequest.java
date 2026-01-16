package se.bastagruppen.todo_appen.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ToDoListCatalogRequest {

    @NotBlank(message = "Catalog name cannot be blank")
    private String name;

}
