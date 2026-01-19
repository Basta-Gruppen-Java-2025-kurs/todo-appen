package se.bastagruppen.todo_appen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.bastagruppen.todo_appen.dto.ToDoListEntryRequestDto;
import se.bastagruppen.todo_appen.dto.ToDoListEntryResponseDto;
import se.bastagruppen.todo_appen.mapper.ToDoListEntryMapper;
import se.bastagruppen.todo_appen.repository.ToDoListEntryRepository;

@Service
@RequiredArgsConstructor
public class ToDoListEntryService {
    private final ToDoListEntryRepository repository;
    private final ToDoListEntryMapper mapper;

    public ToDoListEntryService(ToDoListEntryRepository repository, ToDoListEntryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public ToDoListEntryResponseDto createToDoListEntry(ToDoListEntryRequestDto dto) {}
}
