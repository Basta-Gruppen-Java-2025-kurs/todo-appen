package se.bastagruppen.todo_appen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.bastagruppen.todo_appen.dto.ToDoListRequestDto;
import se.bastagruppen.todo_appen.dto.ToDoListResponseDto;
import se.bastagruppen.todo_appen.exception.ToDoListNotFoundException;
import se.bastagruppen.todo_appen.mapper.ToDoListMapper;
import se.bastagruppen.todo_appen.repository.ToDoListRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ToDoListService {
    private final ToDoListRepository repository;
    private final ToDoListMapper mapper;

    public ToDoListResponseDto createToDoList(ToDoListRequestDto dto) {
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    public List<ToDoListResponseDto> getAllToDoLists() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    public ToDoListResponseDto getById(Long id) {
        return mapper.toDto(repository.findById(id).orElseThrow(() -> new ToDoListNotFoundException(id)));
    }
}
