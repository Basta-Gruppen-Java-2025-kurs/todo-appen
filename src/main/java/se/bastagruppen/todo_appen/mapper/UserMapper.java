package se.bastagruppen.todo_appen.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import se.bastagruppen.todo_appen.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = ReferenceMapper.class)
public interface UserMapper {
    User toEntity(Long id);
}
