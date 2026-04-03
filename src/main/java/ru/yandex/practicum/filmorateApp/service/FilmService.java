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

    public Film addFilm(Film film) {
        FilmValidator.validate(film);
        return filmStorage.add(film);
    }

    public Film updateFilm(Film film) {
        FilmValidator.validate(film);
        getFilmOrThrow(film.getId()); // проверка существования перед обновлением
        return filmStorage.update(film);
    }

    public Film findByIdFilm(long id) {
        return getFilmOrThrow(id);
    }

    public Collection<Film> findAllFilm() {
        return filmStorage.findAll();
    }

    public void addLikeFilm(long filmId, long userId) {
        getFilmOrThrow(filmId);   // проверка фильма
        getUserOrThrow(userId);   // проверка пользователя
        filmStorage.findById(filmId).get().getLikes().add(userId);
        log.info("Пользователь {} поставил лайк фильму {}", userId, filmId);
    }

    public void removeLikeFilm(long filmId, long userId) {
        getFilmOrThrow(filmId);   //  проверка фильма
        getUserOrThrow(userId);   //  проверка пользователя
        filmStorage.findById(filmId).get().getLikes().remove(userId);
        log.info("Пользователь {} удалил лайк с фильма {}", userId, filmId);
    }

    public Collection<Film> getPopularFilm(int count) {
        return filmStorage.getPopular(count); // сортировка — в хранилище
    }

    // вспомогательные методы с говорящими именами
    private Film getFilmOrThrow(long id) {
        return filmStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id=" + id + " не найден"));
    }

    private void getUserOrThrow(long id) {
        userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден"));
    }
}