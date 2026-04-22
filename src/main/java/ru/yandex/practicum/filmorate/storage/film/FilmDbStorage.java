package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbc;

    public FilmDbStorage(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Film add(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO films (name, description, release_date, duration, mpa_id) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setObject(5, film.getMpa() != null ? film.getMpa().getId() : null);
            return ps;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        saveGenres(film);
        log.debug("Фильм добавлен в БД: {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        jdbc.update(
                "UPDATE films SET name=?, description=?, release_date=?, duration=?, mpa_id=? WHERE id=?",
                film.getName(), film.getDescription(),
                Date.valueOf(film.getReleaseDate()), film.getDuration(),
                film.getMpa() != null ? film.getMpa().getId() : null,
                film.getId());
        jdbc.update("DELETE FROM film_genres WHERE film_id=?", film.getId());
        saveGenres(film);
        log.debug("Фильм обновлён в БД: {}", film);
        return findById(film.getId()).orElse(film);
    }

    @Override
    public void delete(long id) {
        jdbc.update("DELETE FROM films WHERE id=?", id);
    }

    @Override
    public Optional<Film> findById(long id) {
        List<Film> films = jdbc.query(
                "SELECT f.*, m.name AS mpa_name FROM films f " +
                        "LEFT JOIN mpa_ratings m ON f.mpa_id = m.id WHERE f.id=?",
                this::mapRow, id);
        if (films.isEmpty()) return Optional.empty();
        Film film = films.get(0);
        film.setGenres(loadGenres(film.getId()));
        film.setLikes(loadLikes(film.getId()));
        return Optional.of(film);
    }

    @Override
    public Collection<Film> findAll() {
        List<Film> films = jdbc.query(
                "SELECT f.*, m.name AS mpa_name FROM films f " +
                        "LEFT JOIN mpa_ratings m ON f.mpa_id = m.id",
                this::mapRow);
        films.forEach(f -> {
            f.setGenres(loadGenres(f.getId()));
            f.setLikes(loadLikes(f.getId()));
        });
        return films;
    }

    @Override
    public Collection<Film> getPopular(int count) {
        List<Film> films = jdbc.query(
                "SELECT f.*, m.name AS mpa_name, COUNT(fl.user_id) AS likes_count " +
                        "FROM films f " +
                        "LEFT JOIN mpa_ratings m ON f.mpa_id = m.id " +
                        "LEFT JOIN film_likes fl ON f.id = fl.film_id " +
                        "GROUP BY f.id, m.name " +
                        "ORDER BY likes_count DESC " +
                        "LIMIT ?",
                this::mapRow, count);
        films.forEach(f -> {
            f.setGenres(loadGenres(f.getId()));
            f.setLikes(loadLikes(f.getId()));
        });
        return films;
    }

    private void saveGenres(Film film) {
        if (film.getGenres() == null || film.getGenres().isEmpty()) return;
        List<Genre> unique = film.getGenres().stream()
                .distinct()
                .collect(Collectors.toList());
        for (Genre g : unique) {
            jdbc.update("INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)",
                    film.getId(), g.getId());
        }
    }

    private List<Genre> loadGenres(long filmId) {
        return jdbc.query(
                "SELECT g.id, g.name FROM genres g " +
                        "JOIN film_genres fg ON g.id = fg.genre_id " +
                        "WHERE fg.film_id=? ORDER BY g.id",
                (rs, rn) -> new Genre(rs.getInt("id"), rs.getString("name")),
                filmId);
    }

    private Set<Long> loadLikes(long filmId) {
        return new HashSet<>(jdbc.query(
                "SELECT user_id FROM film_likes WHERE film_id=?",
                (rs, rn) -> rs.getLong("user_id"),
                filmId));
    }

    private Film mapRow(ResultSet rs, int rn) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        int mpaId = rs.getInt("mpa_id");
        if (!rs.wasNull()) {
            film.setMpa(new Mpa(mpaId, rs.getString("mpa_name")));
        }
        return film;
    }

    public void addLike(long filmId, long userId) {
        jdbc.update("MERGE INTO film_likes (film_id, user_id) VALUES (?, ?)", filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        jdbc.update("DELETE FROM film_likes WHERE film_id=? AND user_id=?", filmId, userId);
    }
}