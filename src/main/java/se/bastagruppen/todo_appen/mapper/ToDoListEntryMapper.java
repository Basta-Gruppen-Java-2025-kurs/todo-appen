package se.bastagruppen.todo_appen.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import se.bastagruppen.todo_appen.dto.ToDoListEntryRequestDto;
import se.bastagruppen.todo_appen.dto.ToDoListEntryResponseDto;
import se.bastagruppen.todo_appen.model.ToDoListEntry;

@Mapper(componentModel = "spring")
public interface ToDoListEntryMapper {

    ToDoListEntry toEntity(ToDoListEntryRequestDto dto);

    @Mapping(source = "parent.id", target = "parentId")
    ToDoListEntryResponseDto toDto(ToDoListEntry entity);
}
