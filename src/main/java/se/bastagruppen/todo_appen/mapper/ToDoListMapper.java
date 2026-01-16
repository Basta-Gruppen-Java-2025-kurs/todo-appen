package se.bastagruppen.todo_appen.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import se.bastagruppen.todo_appen.dto.ToDoListRequestDto;
import se.bastagruppen.todo_appen.dto.ToDoListResponseDto;
import se.bastagruppen.todo_appen.model.ToDoList;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ToDoListMapper {
    ToDoList toEntity(ToDoListRequestDto dto);

    ToDoListResponseDto toDto(ToDoList toDoList);
}
