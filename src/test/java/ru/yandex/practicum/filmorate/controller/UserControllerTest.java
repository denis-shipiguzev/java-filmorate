package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {
    private UserController controller = new UserController();

    @Test
    public void shouldCreateUser() {
        User user = User.builder()
                .login("testLogin")
                .name("testName")
                .email("testemail@mail.com")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        controller.create(user);
        assertEquals(1, controller.findAll().size());

    }

    @Test
    public void shouldThrowExceptionIfLoginIsIncorrect() {
        User user = User.builder()
                .login("  test Login")
                .name("testName")
                .email("testemail@mail.com")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        Exception exception = assertThrows(
                ValidationException.class,
                () -> controller.create(user)
        );
        assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfBirthdayIsIncorrect() {
        User user = User.builder()
                .login("testLogin")
                .name("testName")
                .email("testemail@mail.com")
                .birthday(LocalDate.of(2025, 1, 1))
                .build();

        Exception exception = assertThrows(
                ValidationException.class,
                () -> controller.create(user)
        );
        assertEquals("Дата рождения не может быть в будущем", exception.getMessage());
    }
}
