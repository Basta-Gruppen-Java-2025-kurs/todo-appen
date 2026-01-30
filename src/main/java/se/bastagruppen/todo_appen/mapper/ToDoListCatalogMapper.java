package se.bastagruppen.todo_appen.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import se.bastagruppen.todo_appen.model.ToDoListCatalog;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = ReferenceMapper.class)
public interface ToDoListCatalogMapper {
    ToDoListCatalog toEntity(Long id);
}
