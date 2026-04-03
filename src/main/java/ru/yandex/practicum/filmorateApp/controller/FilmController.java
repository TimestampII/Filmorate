package ru.yandex.practicum.filmorateApp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorateApp.model.Film;
import ru.yandex.practicum.filmorateApp.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("GET /films");
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable long id) {
        log.info("GET /films/{}", id);
        return filmService.findById(id);
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("POST /films: {}", film);
        return filmService.add(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info("PUT /films: {}", film);
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        log.info("PUT /films/{}/like/{}", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable long id, @PathVariable long userId) {
        log.info("DELETE /films/{}/like/{}", id, userId);
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopular(
            @RequestParam(defaultValue = "10") int count) {
        log.info("GET /films/popular?count={}", count);
        return filmService.getPopular(count);
    }
}