package se.bastagruppen.todo_appen.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.bastagruppen.todo_appen.dto.UserRequestDto;
import se.bastagruppen.todo_appen.dto.UserResponseDto;
import se.bastagruppen.todo_appen.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequest) {
        UserResponseDto userResponse = userService.createUser(userRequest);
        return ResponseEntity.ok(userResponse);
    }
}
