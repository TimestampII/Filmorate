package ru.yandex.practicum.filmorateApp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorateApp.exception.NotFoundException;
import ru.yandex.practicum.filmorateApp.model.Film;
import ru.yandex.practicum.filmorateApp.storage.film.FilmStorage;
import ru.yandex.practicum.filmorateApp.storage.user.UserStorage;
import ru.yandex.practicum.filmorateApp.validation.FilmValidator;

import java.util.Collection;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film add(Film film) {
        FilmValidator.validate(film);
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        FilmValidator.validate(film);
        getFilmOrThrow(film.getId()); // явная проверка существования перед обновлением
        return filmStorage.update(film);
    }

    public Film findById(long id) {
        return getFilmOrThrow(id);
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public void addLike(long filmId, long userId) {
        getFilmOrThrow(filmId);   // явная проверка фильма
        getUserOrThrow(userId);   // явная проверка пользователя
        filmStorage.findById(filmId).get().getLikes().add(userId);
        log.info("Пользователь {} поставил лайк фильму {}", userId, filmId);
    }

    public void removeLike(long filmId, long userId) {
        getFilmOrThrow(filmId);   // явная проверка фильма
        getUserOrThrow(userId);   // явная проверка пользователя
        filmStorage.findById(filmId).get().getLikes().remove(userId);
        log.info("Пользователь {} удалил лайк с фильма {}", userId, filmId);
    }

    public Collection<Film> getPopular(int count) {
        return filmStorage.getPopular(count); // сортировка — в хранилище
    }

    // явные вспомогательные методы с говорящими именами
    private Film getFilmOrThrow(long id) {
        return filmStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id=" + id + " не найден"));
    }

    private void getUserOrThrow(long id) {
        userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден"));
    }
}