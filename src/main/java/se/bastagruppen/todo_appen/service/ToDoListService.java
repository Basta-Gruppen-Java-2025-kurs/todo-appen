package se.bastagruppen.todo_appen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.bastagruppen.todo_appen.dto.ToDoListRequestDto;
import se.bastagruppen.todo_appen.dto.ToDoListResponseDto;
import se.bastagruppen.todo_appen.exception.ToDoListNotFoundException;
import se.bastagruppen.todo_appen.mapper.ToDoListMapper;
import se.bastagruppen.todo_appen.model.ToDoList;
import se.bastagruppen.todo_appen.model.ToDoListCatalog;
import se.bastagruppen.todo_appen.repository.ToDoListCatalogRepository;
import se.bastagruppen.todo_appen.repository.ToDoListRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ToDoListService {
    private final ToDoListRepository repository;
    private final ToDoListMapper mapper;
    private final ToDoListCatalogRepository catalogRepository;

    public ToDoListResponseDto createToDoList(ToDoListRequestDto dto) {
        ToDoListCatalog catalog = catalogRepository.findById(dto.getCatalogId())
                .orElseThrow(() -> new IllegalArgumentException("Catalog not foud with id: " + dto.getCatalogId()));

        if (repository.existsByCatalogIdAndName(catalog.getId(), dto.getName())) {
            throw new IllegalArgumentException("A list with this name already exists in the catalog");
        }

//        ToDoList list = mapper.toEntity(dto);
//        list.setCatalog(catalog);
        ToDoList result = repository.save(mapper.toEntity(dto));
        ToDoList fullResult = repository.findById(result.getId()).orElseThrow(() -> new ToDoListNotFoundException(result.getId()));
        return mapper.toDto(fullResult);
    }

    public List<ToDoListResponseDto> getAllToDoLists() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    public ToDoListResponseDto getById(Long id) {
        return mapper.toDto(repository.findById(id).orElseThrow(() -> new ToDoListNotFoundException(id)));
    }

    public List<ToDoListResponseDto> search(Long userId, Long catalogId, String filter, List<String> tags) {
        String filterString = (filter == null || filter.isEmpty()) ? null : "%" + filter + "%";
        return repository.search(userId, catalogId, filterString, tags).stream().map(mapper::toDto).toList();
    }

    public ToDoListResponseDto renameToDoList(Long id, String newName) {
        ToDoList list = repository.findById(id)
                .orElseThrow(() -> new ToDoListNotFoundException(id));

        if (!list.getName().equals(newName) && repository.existsByCatalogIdAndName(list.getCatalog().getId(), newName)) {
            throw new IllegalArgumentException("A list with this name already exists in the catalog");
        }

        list.setName(newName);

        return mapper.toDto(repository.save(list));
    }
}
