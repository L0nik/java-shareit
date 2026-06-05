package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.CreateUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserClient userClient;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable Long userId) {
        log.info("UserController: получен запрос на получение пользователя по userId={}", userId);
        return userClient.getUserById(userId);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid CreateUserRequest userData) {
        log.info("UserController: получен запрос на добавление пользователя {}", userData);
        return userClient.createUser(userData);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(
            @PathVariable Long userId,
            @RequestBody @Valid UpdateUserRequest userData
    ) {
        log.info("UserController: получен запрос на обновление данных пользователя (userId={}) {}", userId, userData);
        return userClient.updateUser(userId, userData);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("UserController: получен запрос на удаление пользователя по userId={}", userId);
        userClient.deleteUser(userId);
    }

}
