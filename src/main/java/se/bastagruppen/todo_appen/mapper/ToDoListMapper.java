package se.bastagruppen.todo_appen.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import se.bastagruppen.todo_appen.dto.ToDoListRequestDto;
import se.bastagruppen.todo_appen.dto.ToDoListResponseDto;
import se.bastagruppen.todo_appen.model.Tag;
import se.bastagruppen.todo_appen.model.ToDoList;
import se.bastagruppen.todo_appen.model.ToDoListCatalog;
import se.bastagruppen.todo_appen.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        uses = {UserMapper.class, ToDoListCatalogMapper.class, ReferenceMapper.class})
public interface ToDoListMapper {
    @Mapping(source = "dto.userId", target = "ownerId")
    @Mapping(source = "dto.catalogId", target = "catalogId")
    @Mapping(source = "dto.userId", target = "owner")
    @Mapping(source = "dto.catalogId", target = "catalog")
    ToDoList toEntity(ToDoListRequestDto dto);

    @Mapping(source = "entity.tags", target = "tags")
    @Mapping(source = "entity.catalog", target = "catalogName")
    @Mapping(source = "owner", target = "username")
    ToDoListResponseDto toDto(ToDoList entity);

    ToDoList toEntity(Long id);

    default String tagToString(Tag tag) {
        return (tag == null) ? null : tag.getName();
    }

    default String catalogToString(ToDoListCatalog catalog) {
        return (catalog == null) ? null : catalog.getName();
    }

    default String userToString(User user) {
        return (user == null) ? null : user.getUsername();
    }

}
