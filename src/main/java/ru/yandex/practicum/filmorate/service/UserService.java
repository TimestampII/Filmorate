package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.UserValidator;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Collection;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User add(User user) {
        UserValidator.validate(user);
        return userStorage.add(user);
    }

    public User update(User user) {
        UserValidator.validate(user);
        getUserByIdOrThrow(user.getId()); // явная проверка существования перед обновлением
        return userStorage.update(user);
    }

    public User findById(long id) {
        return getUserByIdOrThrow(id);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public void addFriend(long userId, long friendId) {
        getUserByIdOrThrow(userId);
        getUserByIdOrThrow(friendId);
        userStorage.addFriend(userId, friendId); // логика — в хранилище
        log.info("Пользователи {} и {} теперь друзья", userId, friendId);
    }

    public void removeFriend(long userId, long friendId) {
        getUserByIdOrThrow(userId);
        getUserByIdOrThrow(friendId);
        userStorage.removeFriend(userId, friendId); // логика — в хранилище
        log.info("Пользователи {} и {} больше не друзья", userId, friendId);
    }

    public Collection<User> getFriends(long userId) {
        getUserByIdOrThrow(userId);
        return userStorage.getFriends(userId); // выборка — в хранилище
    }

    public Collection<User> getCommonFriends(long userId, long otherId) {
        getUserByIdOrThrow(userId);
        getUserByIdOrThrow(otherId);
        return userStorage.getCommonFriends(userId, otherId); // фильтрация — в хранилище
    }

    private User getUserByIdOrThrow(long id) {
        return userStorage.findById(id).orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден"));
    }
}