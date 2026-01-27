package se.bastagruppen.todo_appen.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;
import org.springframework.test.context.ActiveProfiles;
import se.bastagruppen.todo_appen.model.ToDoList;

@ActiveProfiles("test")
public class ToDoListMapperTest {
    @Spy
    private ToDoListMapper mapper = Mappers.getMapper(ToDoListMapper.class);

    @Test
    public void entityToDtoTest() {
        ToDoList toDoList = new ToDoList();
    }
}
