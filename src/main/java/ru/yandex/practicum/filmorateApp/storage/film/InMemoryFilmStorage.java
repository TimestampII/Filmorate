package ru.yandex.practicum.filmorateApp.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorateApp.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film add(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм добавлен: {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);
        log.info("Фильм обновлён: {}", film);
        return film;
    }

    @Override
    public void delete(long id) {
        films.remove(id);
        log.info("Фильм удалён, id={}", id);
    }

    @Override
    public Optional<Film> findById(long id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    private long getNextId() {
        return films.keySet().stream().mapToLong(id -> id).max().orElse(0) + 1;
    }
}