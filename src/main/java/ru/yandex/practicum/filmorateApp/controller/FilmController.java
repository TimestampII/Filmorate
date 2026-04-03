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
        log.debug("GET /films");
        return filmService.findAllFilm();
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable long id) {
        log.debug("GET /films/{}", id);
        return filmService.findByIdFilm(id);
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.debug("POST /films: {}", film);
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.debug("PUT /films: {}", film);
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        log.debug("PUT /films/{}/like/{}", id, userId);
        filmService.addLikeFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable long id, @PathVariable long userId) {
        log.debug("DELETE /films/{}/like/{}", id, userId);
        filmService.removeLikeFilm(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopular(
            @RequestParam(defaultValue = "10") int count) {
        log.debug("GET /films/popular?count={}", count);
        return filmService.getPopularFilm(count);
    }
}