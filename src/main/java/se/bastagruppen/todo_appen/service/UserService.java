package se.bastagruppen.todo_appen.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.bastagruppen.todo_appen.dto.UserRequestDto;
import se.bastagruppen.todo_appen.dto.UserResponseDto;
import se.bastagruppen.todo_appen.model.User;
import se.bastagruppen.todo_appen.repository.UserRepository;

import java.util.ArrayList;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDto createUser(UserRequestDto request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }

    private UserResponseDto mapToResponse(User user) {
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(user.getId());
        responseDto.setUsername(user.getUsername());
        //Empty lists until tags and catalogs is implemented
        responseDto.setCatalogs(new ArrayList<>());
        responseDto.setTags(new ArrayList<>());
        return responseDto;
    }
}
