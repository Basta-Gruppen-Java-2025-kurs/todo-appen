package se.bastagruppen.todo_appen.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.bastagruppen.todo_appen.dto.ToDoListRequestDto;
import se.bastagruppen.todo_appen.dto.ToDoListResponseDto;
import se.bastagruppen.todo_appen.dto.ToDoListRenameRequestDto;
import se.bastagruppen.todo_appen.service.ToDoListService;

import java.util.List;

@RestController
@RequestMapping("/list")
@RequiredArgsConstructor
public class ToDoListController {
    private final ToDoListService service;

    @GetMapping
    public ResponseEntity<List<ToDoListResponseDto>> getAllToDoLists(@RequestParam(required = false) @Valid Long userId,
                                                                     @RequestParam(required = false) @Valid Long catalogId,
                                                                     @RequestParam(required = false) @Valid String filter,
                                                                     @RequestParam(required = false) @Valid List<String> tags) {
        if (userId == null &&  catalogId == null && tags == null && filter == null) {
            return ResponseEntity.ok(service.getAllToDoLists());
        }
        return ResponseEntity.ok(service.search(userId, catalogId, filter, tags));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ToDoListResponseDto> getToDoListById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<ToDoListResponseDto> createToDoList(@Valid @RequestBody ToDoListRequestDto toDoListRequestDto) {
        return ResponseEntity.ok(service.createToDoList(toDoListRequestDto));
    }

    @PatchMapping("/{id}/rename")
    public ResponseEntity<ToDoListResponseDto> renameToDoList(@PathVariable Long id,
                                                          @Valid @RequestBody ToDoListRenameRequestDto toDoListRenameDto) {
        return ResponseEntity.ok(service.renameToDoList(id, toDoListRenameDto.getName()));
    }
}
