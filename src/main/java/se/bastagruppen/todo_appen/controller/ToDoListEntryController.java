package se.bastagruppen.todo_appen.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.bastagruppen.todo_appen.dto.ToDoListEntryRequestDto;
import se.bastagruppen.todo_appen.dto.ToDoListEntryResponseDto;
import se.bastagruppen.todo_appen.service.ToDoListEntryService;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ToDoListEntryController {
    private final ToDoListEntryService service;

    @PostMapping("/lists/{listId}/entries")
    public ResponseEntity<ToDoListEntryResponseDto> createToDoListEntry(@PathVariable Long listId, @Valid @RequestBody ToDoListEntryRequestDto dto) {
        // TODO: Change hard coded user id to logged in user
        return ResponseEntity.ok(service.createToDoListEntry(listId, dto, 1L));
    }

    // GET all entries including subentries for a list
    @GetMapping("/lists/{listId}/entries")
    public ResponseEntity<List<ToDoListEntryResponseDto>> getAllEntriesOfAList(@PathVariable Long listId) {
        // TODO: Change hard coded user id to logged in user
        return ResponseEntity.ok(service.getAllEntriesOfAList(listId, 1L));
    }

    // GET /entries/{entryId}
    // PUT /entries/{entryId}
    // PATCH /entries/{entryId}

    // DELETE entry
    @DeleteMapping("/entries/{entryId}")
    public ResponseEntity<Void> deleteEntryById(@PathVariable Long entryId) {
        // TODO: Change hard coded user id to logged in user
        service.deleteEntryById(entryId, 1L);

       return ResponseEntity.noContent().build();
    }

}
