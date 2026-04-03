package ru.yandex.practicum.filmorateApp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorateApp.exception.NotFoundException;
import ru.yandex.practicum.filmorateApp.model.Film;
import ru.yandex.practicum.filmorateApp.storage.film.FilmStorage;
import ru.yandex.practicum.filmorateApp.validation.FilmValidator;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, @Lazy UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Film add(Film film) {
        FilmValidator.validate(film);
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        FilmValidator.validate(film);
        filmStorage.findById(film.getId())
                .orElseThrow(() -> new NotFoundException("Фильм с id=" + film.getId() + " не найден"));
        return filmStorage.update(film);
    }

    public Film findById(long id) {
        return filmStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id=" + id + " не найден"));
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public void addLike(long filmId, long userId) {
        Film film = findById(filmId);           // 404 если фильм не найден
        userService.findById(userId);           // 404 если пользователь не найден
        film.getLikes().add(userId);
        log.info("Пользователь {} поставил лайк фильму {}", userId, filmId);
    }

    public void removeLike(long filmId, long userId) {
        Film film = findById(filmId);           // 404 если фильм не найден
        userService.findById(userId);           // 404 если пользователь не найден
        film.getLikes().remove(userId);         // если лайка не было — просто игнорируем
        log.info("Пользователь {} удалил лайк с фильма {}", userId, filmId);
    }

    public Collection<Film> getPopular(int count) {
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}