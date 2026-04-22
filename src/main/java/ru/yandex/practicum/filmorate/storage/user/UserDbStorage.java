package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component("userDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbc;

    public UserDbStorage(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public User add(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, user.getBirthday() != null ? Date.valueOf(user.getBirthday()) : null);
            return ps;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        log.debug("Пользователь добавлен в БД: {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        jdbc.update("UPDATE users SET email=?, login=?, name=?, birthday=? WHERE id=?",
                user.getEmail(), user.getLogin(), user.getName(),
                user.getBirthday() != null ? Date.valueOf(user.getBirthday()) : null,
                user.getId());
        log.debug("Пользователь обновлён в БД: {}", user);
        return user;
    }

    @Override
    public void delete(long id) {
        jdbc.update("DELETE FROM users WHERE id=?", id);
    }

    @Override
    public Optional<User> findById(long id) {
        List<User> users = jdbc.query("SELECT * FROM users WHERE id=?", this::mapRow, id);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    @Override
    public Collection<User> findAll() {
        return jdbc.query("SELECT * FROM users", this::mapRow);
    }

    @Override
    public void addFriend(User user, User friend) {
        jdbc.update(
                "MERGE INTO friendships (user_id, friend_id, confirmed) VALUES (?, ?, FALSE)",
                user.getId(), friend.getId());
        log.debug("Пользователь {} отправил заявку в друзья {}", user.getId(), friend.getId());
    }

    @Override
    public void removeFriend(User user, User friend) {
        jdbc.update("DELETE FROM friendships WHERE user_id=? AND friend_id=?",
                user.getId(), friend.getId());
        log.debug("Пользователь {} удалил из друзей {}", user.getId(), friend.getId());
    }

    @Override
    public Collection<User> getFriends(User user) {
        return jdbc.query(
                "SELECT u.* FROM users u " +
                        "JOIN friendships f ON u.id = f.friend_id " +
                        "WHERE f.user_id=?",
                this::mapRow, user.getId());
    }

    @Override
    public Collection<User> getCommonFriends(User user, User other) {
        return jdbc.query(
                "SELECT u.* FROM users u " +
                        "JOIN friendships f1 ON u.id = f1.friend_id AND f1.user_id=? " +
                        "JOIN friendships f2 ON u.id = f2.friend_id AND f2.user_id=?",
                this::mapRow, user.getId(), other.getId());
    }

    private User mapRow(ResultSet rs, int rn) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setEmail(rs.getString("email"));
        user.setLogin(rs.getString("login"));
        user.setName(rs.getString("name"));
        Date birthday = rs.getDate("birthday");
        if (birthday != null) user.setBirthday(birthday.toLocalDate());
        return user;
    }
}