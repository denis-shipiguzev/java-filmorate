package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Get All films");
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Creating film: {}", film);
        validateFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film updatedFilm) {
        log.info("Updating film with id: {}", updatedFilm.getId());
        if (updatedFilm.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        validateFilm(updatedFilm);
        if (films.containsKey(updatedFilm.getId())) {
            Film existingFilm = films.get(updatedFilm.getId());
            existingFilm.setName(updatedFilm.getName());
            existingFilm.setDescription(updatedFilm.getDescription());
            existingFilm.setReleaseDate(updatedFilm.getReleaseDate());
            existingFilm.setDuration(updatedFilm.getDuration());
            return existingFilm;
        }
        log.error("Validation failed: Фильм с id = {} не найден", updatedFilm.getId());
        throw new ValidationException("Фильм с id = " + updatedFilm.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            log.error("Validation failed: Название фильма не может быть пустым");
            throw new ValidationException("Название фильма не может быть пустым");
        }

        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.error("Validation failed: Максимальная длина описания фильма — 200 символов");
            throw new ValidationException("Максимальная длина описания фильма — 200 символов");
        }

        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Validation failed: Дата релиза не может быть раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }

        if (film.getDuration() != null && film.getDuration().isNegative()) {
            log.error("Validation failed: Продолжительность фильма должна быть положительным числом");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }
}
