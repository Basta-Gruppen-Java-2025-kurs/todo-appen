package se.bastagruppen.todo_appen.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import se.bastagruppen.todo_appen.dto.ToDoListEntryRequestDto;
import se.bastagruppen.todo_appen.dto.ToDoListEntryResponseDto;
import se.bastagruppen.todo_appen.service.ToDoListEntryService;


@RequestMapping("/entries")
@RequiredArgsConstructor
public class ToDoListEntryController {
    private final ToDoListEntryService service;

    @PostMapping
    public ResponseEntity<ToDoListEntryResponseDto> createToDoListEntry(@Valid @RequestBody ToDoListEntryRequestDto dto) {
        return ResponseEntity.ok(service.createToDoListEntry(dto));
    }
}
