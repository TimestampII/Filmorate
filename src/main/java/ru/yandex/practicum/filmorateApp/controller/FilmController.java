package ru.yandex.practicum.filmorateApp.controller;

import jakarta.validation.Valid;
import ru.yandex.practicum.filmorateApp.exception.NotFoundException;
import ru.yandex.practicum.filmorateApp.model.Film;
import ru.yandex.practicum.filmorateApp.validation.FilmValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {


    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Получен запрос на все фильмы. Всего фильмов: {}", films.size());
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Добавление фильма: {}", film);
        FilmValidator.validate(film);

        film.setId(getNextFilmId());
        films.put(film.getId(), film);
        log.info("Фильм добавлен: {}", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws NotFoundException {
        log.info("Вносятся изменения в фильм: {}", film);
        FilmValidator.validate(film);

        if (!exists(film)) {
            log.warn("Фильм с таким {} ID не найден при обновлении", film.getId());
            throw new NotFoundException("Фильм с таким id " + film.getId() + " отсутствует");
        }

        films.put(film.getId(), film);
        log.info("Фильм найден и изменен: {}", film);
        return film;
    }

    private boolean exists(Film film) {
        return films.containsKey(film.getId());
    }

    private long getNextFilmId() {
        return films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0) + 1;
    }
}

