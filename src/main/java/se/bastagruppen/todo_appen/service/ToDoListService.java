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

        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    public List<ToDoListResponseDto> getAllToDoLists() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    public ToDoListResponseDto getByIdAndUserId(Long id, Long userId) {
        return mapper.toDto(repository.findByIdAndOwnerId(id, userId).orElseThrow(() -> new ToDoListNotFoundException(id)));
    }

    public List<ToDoListResponseDto> search(Long userId, Long catalogId, String filter, List<String> tags) {
        String filterString = (filter == null || filter.isEmpty()) ? null : "%" + filter + "%";
        List <ToDoList> result;

        if (tags == null) {
            result = repository.searchByParams(userId, catalogId, filterString);
        } else if (userId == null && catalogId == null && filterString == null) {
            result = repository.search(userId, catalogId, filterString, tags);
        } else if (tags.size() < 2) {
            result = repository.searchByTag(tags.getFirst());
        } else {
            result = repository.searchByTags(tags);
        }
        return result.stream().map(mapper::toDto).toList();
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
        
    public void deleteToDoList(Long id){
        ToDoList target =  repository.findById(id).orElseThrow(() -> new ToDoListNotFoundException(id));

        //todo: requires user verification functionality to implement fully, tentative implement:
        // if (!target.getUser().getId().equals(userId)) {throw new UnauthorizedActionException("Cannot delete a list you do not own"); }

        repository.delete(target);
    }
}
