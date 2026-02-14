package ru.yandex.practicum.filmorateApp.controller;

import jakarta.validation.Valid;
import ru.yandex.practicum.filmorateApp.exception.NotFoundException;
import ru.yandex.practicum.filmorateApp.model.Film;
import ru.yandex.practicum.filmorateApp.model.User;
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
    public Film create(@Valid @RequestBody Film film) {
        log.debug("Добавление фильма: {}", film);

        try {
            FilmValidator.validate(film);
        } catch (Exception e) {
            log.warn("Ошибка валидации при добавлении фильма {}: {}", film, e.getMessage());
            throw e;
        }
        film.setId(getNextFilmId());
        films.put(film.getId(), film);
        log.debug("Фильм добавлен: {}", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws NotFoundException {
        log.debug("Вносятся изменения в фильм: {}", film);
        FilmValidator.validate(film);

        if (!exists(film.getId())) {
            log.warn("Фильм с таким {} ID не найден при обновлении", film.getId());
            throw new NotFoundException("Фильм с таким id " + film.getId() + " отсутствует");
        }

        films.put(film.getId(), film);
        log.debug("Фильм найден и изменен: {}", film);
        return film;
    }

    private boolean exists(Long filmId) {
       return films.containsKey(filmId);
    }

    private long getNextFilmId() {
        return films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0) + 1;
    }
}

