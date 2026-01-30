package se.bastagruppen.todo_appen.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import se.bastagruppen.todo_appen.dto.ToDoListRequestDto;
import se.bastagruppen.todo_appen.dto.ToDoListResponseDto;
import se.bastagruppen.todo_appen.dto.ToDoListRenameRequestDto;
import se.bastagruppen.todo_appen.security.CustomPrincipal;
import se.bastagruppen.todo_appen.service.ToDoListService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/list")
@RequiredArgsConstructor
public class ToDoListController {
    private final ToDoListService service;

    @GetMapping
    public ResponseEntity<List<ToDoListResponseDto>> getAllToDoLists(@AuthenticationPrincipal CustomPrincipal user,
                                                                     @RequestParam(required = false) @Valid Long catalogId,
                                                                     @RequestParam(required = false) @Valid String filter,
                                                                     @RequestParam(required = false) @Valid List<String> tags) {
        Long userId = user != null ? user.getUserId() :  null;
        if (userId == null &&  catalogId == null && tags == null && filter == null) {
            return ResponseEntity.ok(service.getAllToDoLists());
        }
        log.info("Searching for userId: " + userId + " and catalogId: " + catalogId + " and tags: " + tags + " and filter: " + filter);
        return ResponseEntity.ok(service.search(userId, catalogId, filter, tags));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ToDoListResponseDto> getToDoListById(@PathVariable Long id, @AuthenticationPrincipal CustomPrincipal user) {
        Long userId = user != null ? user.getUserId() :  null;
        return ResponseEntity.ok(service.getByIdAndUserId(id, userId));
    }

    @PostMapping
    public ResponseEntity<ToDoListResponseDto> createToDoList(@Valid @RequestBody ToDoListRequestDto toDoListRequestDto,
                                                              @AuthenticationPrincipal CustomPrincipal user) {
        if (user != null) {
            toDoListRequestDto.setUserId(user.getUserId());
        }
        return ResponseEntity.ok(service.createToDoList(toDoListRequestDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ToDoListResponseDto> renameToDoList(@PathVariable Long id,
                                                              @Valid @RequestBody ToDoListRenameRequestDto toDoListRenameDto,
                                                              @AuthenticationPrincipal CustomPrincipal user) {
        Long userId = user != null ? user.getUserId() :  null;
        return ResponseEntity.ok(service.renameToDoList(id, toDoListRenameDto.getName(), userId));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTodoList(@PathVariable Long id, @AuthenticationPrincipal CustomPrincipal user) {
        Long userId = user != null ? user.getUserId() :  null;
        service.deleteToDoList(id, userId);
        return ResponseEntity.ok("ToDoList # " + id + " deleted");
    }
}
