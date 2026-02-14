package ru.yandex.practicum.filmorateApp.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorateApp.model.Film;
import ru.yandex.practicum.filmorateApp.validation.FilmValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {

        FilmValidator.validate(film);
        film.setId(getNextFilmId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {

        FilmValidator.validate(film);

        if (!films.containsKey(film.getId())) {
            throw new IllegalArgumentException("Фильм с таким id " + film.getId() + " отсутствует");
        }

        films.put(film.getId(), film);
        return film;
    }

    private long getNextFilmId() {
        return films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0) + 1;
    }
}

