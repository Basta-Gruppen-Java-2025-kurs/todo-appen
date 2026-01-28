package se.bastagruppen.todo_appen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.bastagruppen.todo_appen.dto.ToDoListEntryUpdateDoneDto;
import se.bastagruppen.todo_appen.dto.ToDoListEntryRequestDto;
import se.bastagruppen.todo_appen.dto.ToDoListEntryResponseDto;
import se.bastagruppen.todo_appen.dto.ToDoListEntryUpdateDto;
import se.bastagruppen.todo_appen.exception.BadRequestException;
import se.bastagruppen.todo_appen.exception.NotFoundException;
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

    public ToDoListEntryResponseDto createToDoListEntry(Long listId, ToDoListEntryRequestDto dto, Long userId) {
        ToDoList list = listRepository.findByIdAndOwnerId(listId, userId)
                .orElseThrow(() -> new NotFoundException("List not found or you do not have permission"));

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

    public List<ToDoListEntryResponseDto> getAllEntriesOfAList(Long listId, Long userId) {
        ToDoList list = listRepository.findByIdAndOwnerId(listId, userId)
                .orElseThrow(() -> new NotFoundException("List not found or you do not have permission"));

        List<ToDoListEntry> entries = repository.findByListIdAndParentIsNull(list.getId());

        return entries.stream().map(mapper::toDto).toList();
    }

    public ToDoListEntryResponseDto updateEntry(Long entryId, Long userId, ToDoListEntryUpdateDto dto) {
        ToDoListEntry existing = repository.findByIdWithOwner(entryId, userId)
                .orElseThrow(() -> new NotFoundException("Entry not found or you do not have permission"));

        existing.setSummary(dto.getSummary());
        existing.setDetails(dto.getDetails());
        existing.setDeadline(dto.getDeadline());
        existing.setDone(dto.getDone());

        ToDoListEntry saved = repository.save(existing);

        return mapper.toDto(saved);
    }

    public void updateDone(Long entryId, Long userId, ToDoListEntryUpdateDoneDto dto) {
        ToDoListEntry existing = repository.findByIdWithOwner(entryId, userId)
                .orElseThrow(() -> new NotFoundException("Entry not found or you do not have permission"));

        existing.setDone(dto.getDone());

        repository.save(existing);
    }

    public void deleteEntryById(Long entryId, Long userId) {
        ToDoListEntry entry = repository.findByIdWithOwner(entryId, userId)
                        .orElseThrow(() -> new NotFoundException("Entry not found or you do not have permission"));

        repository.deleteById(entryId);
    }
}
