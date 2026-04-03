package ru.yandex.practicum.filmorateApp.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorateApp.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User add(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Пользователь добавлен: {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        log.info("Пользователь обновлён: {}", user);
        return user;
    }

    @Override
    public void delete(long id) {
        users.remove(id);
        log.info("Пользователь удалён, id={}", id);
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    private long getNextId() {
        return users.keySet().stream().mapToLong(id -> id).max().orElse(0) + 1;
    }
}