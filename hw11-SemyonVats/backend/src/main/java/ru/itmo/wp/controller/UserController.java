package ru.itmo.wp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.dto.UserDto;
import ru.itmo.wp.form.UserCredentials;
import ru.itmo.wp.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/users")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserCredentials credentials) {
        try {
            String login = credentials.getLogin().toLowerCase().trim();

            if (!login.matches("[a-z0-9]{3,16}")) {
                return ResponseEntity.badRequest().body("Login must be 3-16 lowercase Latin letters or digits");
            }

            if (credentials.getPassword() == null ||
                    credentials.getPassword().length() < 8 ||
                    credentials.getPassword().length() > 32) {
                return ResponseEntity.badRequest().body("Password must be 8-32 characters long");
            }

            if (!userService.isLoginVacant(login)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("This login is already taken");
            }

            User user = userService.register(credentials);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}