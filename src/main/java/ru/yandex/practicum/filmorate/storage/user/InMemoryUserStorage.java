package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User add(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.debug("Пользователь добавлен: {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        log.debug("Пользователь обновлён: {}", user);
        return user;
    }

    @Override
    public void delete(long id) {
        users.remove(id);
        log.debug("Пользователь удалён, id={}", id);
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public void addFriend(long userId, long friendId) {
        users.get(userId).getFriends().add(friendId);
        users.get(friendId).getFriends().add(userId);
        log.debug("Пользователи {} и {} теперь друзья", userId, friendId);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        users.get(userId).getFriends().remove(friendId);
        users.get(friendId).getFriends().remove(userId);
        log.debug("Пользователи {} и {} больше не друзья", userId, friendId);
    }

    @Override
    public Collection<User> getFriends(long userId) {
        return users.get(userId).getFriends().stream().map(users::get).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public Collection<User> getCommonFriends(long userId, long otherId) {
        Set<Long> userFriends = users.get(userId).getFriends();
        Set<Long> otherFriends = users.get(otherId).getFriends();
        return userFriends.stream().filter(otherFriends::contains).map(users::get).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private long getNextId() {
        return users.keySet().stream().mapToLong(id -> id).max().orElse(0) + 1;
    }
}