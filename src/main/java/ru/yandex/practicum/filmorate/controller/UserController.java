package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        log.info("Get All users");
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Creating user: {}", user);
        validateUser(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User updatedUser) {
        log.info("Updating user with id: {}", updatedUser.getId());
        if (updatedUser.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        validateUser(updatedUser);
        if (users.containsKey(updatedUser.getId())) {
            User existingUser = users.get(updatedUser.getId());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setLogin(updatedUser.getLogin());
            existingUser.setName(updatedUser.getName());
            existingUser.setBirthday(updatedUser.getBirthday());
            return existingUser;
        }
        log.error("Validation failed: Пользователь с id = {} не найден", updatedUser.getId());
        throw new ValidationException("Пользователь с id = " + updatedUser.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            log.error("Validation failed: Электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }

        if (user.getLogin() == null || user.getLogin().contains(" ")) {
            log.error("Validation failed: Логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            log.warn("User name is empty, using login as display name");
            user.setName(user.getLogin());
        }

        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Validation failed: Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
