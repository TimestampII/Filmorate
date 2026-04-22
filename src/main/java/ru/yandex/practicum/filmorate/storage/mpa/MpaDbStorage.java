package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbc;

    public MpaDbStorage(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Collection<Mpa> findAll() {
        return jdbc.query("SELECT * FROM mpa_ratings ORDER BY id",
                (rs, rn) -> new Mpa(rs.getInt("id"), rs.getString("name")));
    }

    @Override
    public Optional<Mpa> findById(int id) {
        List<Mpa> result = jdbc.query("SELECT * FROM mpa_ratings WHERE id=?",
                (rs, rn) -> new Mpa(rs.getInt("id"), rs.getString("name")), id);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
}