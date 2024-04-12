package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    private FilmController controller = new FilmController();

    @Test
    public void shouldCreateFilm() {
        Film film = Film.builder()
                .name("testName")
                .description("testDescription")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(Duration.ofMinutes(90))
                .build();
        controller.create(film);
        assertEquals(1, controller.findAll().size());

    }

    @Test
    public void shouldThrowExceptionIfNameIsIncorrect() {
        Film film = Film.builder()
                .name("")
                .description("testDescription")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(Duration.ofMinutes(90))
                .build();
        Exception exception = assertThrows(
                ValidationException.class,
                () -> controller.create(film)
        );
        assertEquals("Название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfReleaseDateIsIncorrect() {
        Film film = Film.builder()
                .name("testName")
                .description("testDescription")
                .releaseDate(LocalDate.of(1894, 1, 1))
                .duration(Duration.ofMinutes(90))
                .build();
        Exception exception = assertThrows(
                ValidationException.class,
                () -> controller.create(film)
        );
        assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года", exception.getMessage());
    }
}
