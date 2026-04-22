package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbc;

    public GenreDbStorage(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Collection<Genre> findAll() {
        return jdbc.query("SELECT * FROM genres ORDER BY id",
                (rs, rn) -> new Genre(rs.getInt("id"), rs.getString("name")));
    }

    @Override
    public Optional<Genre> findById(int id) {
        List<Genre> result = jdbc.query("SELECT * FROM genres WHERE id=?",
                (rs, rn) -> new Genre(rs.getInt("id"), rs.getString("name")), id);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
}