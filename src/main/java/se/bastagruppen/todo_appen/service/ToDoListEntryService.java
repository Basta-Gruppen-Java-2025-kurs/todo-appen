package se.bastagruppen.todo_appen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.bastagruppen.todo_appen.dto.ToDoListEntryRequestDto;
import se.bastagruppen.todo_appen.dto.ToDoListEntryResponseDto;
import se.bastagruppen.todo_appen.exception.BadRequestException;
import se.bastagruppen.todo_appen.exception.NotFoundException;
import se.bastagruppen.todo_appen.exception.ToDoListNotFoundException;
import se.bastagruppen.todo_appen.mapper.ToDoListEntryMapper;
import se.bastagruppen.todo_appen.model.ToDoListEntry;
import se.bastagruppen.todo_appen.repository.ToDoListEntryRepository;
import se.bastagruppen.todo_appen.model.ToDoList;
import se.bastagruppen.todo_appen.repository.ToDoListRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ToDoListEntryService {
    private final ToDoListEntryRepository repository;
    private final ToDoListRepository listRepository;
    private final ToDoListEntryMapper mapper;

    public ToDoListEntryResponseDto createToDoListEntry(Long listId, ToDoListEntryRequestDto dto) {

        ToDoList list = listRepository.findById(listId)
                .orElseThrow(() -> new ToDoListNotFoundException(listId));

        // TODO: make sure the list belongs to an authenticated user
        // if (!list.getOwner().getId().equals(authenticatedUserId)) {
        //     throw new AccessDeniedException("This list id belongs to another user");
        // }

        ToDoListEntry entry = mapper.toEntity(dto);
        entry.setList(list);

        if (dto.getParentId() != null) {
            ToDoListEntry parent = repository.findById(dto.getParentId())
                    .orElseThrow(() -> new NotFoundException("Parent entry not found"));

            if(!parent.getList().getId().equals(listId)) {
                throw new BadRequestException("Parent task must be in the same list as subtask");
            }

            entry.setParent(parent);
        }

        ToDoListEntry saved = repository.save(entry);

        return mapper.toDto(saved);
    }

    public List<ToDoListEntryResponseDto> getAllEntriesOfAList(Long listId) {
        ToDoList list = listRepository.findById(listId)
                .orElseThrow(() -> new ToDoListNotFoundException(listId));

        List<ToDoListEntry> entries = repository.findByListIdAndParentIsNull(listId);

        return entries.stream().map(mapper::toDto).toList();
    }

}
