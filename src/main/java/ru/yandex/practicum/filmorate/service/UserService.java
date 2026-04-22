package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.UserValidator;

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
        getUserByIdOrThrow(user.getId());
        return userStorage.update(user);
    }

    public User findById(long id) {
        return getUserByIdOrThrow(id);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public void addFriend(long userId, long friendId) {
        User user = getUserByIdOrThrow(userId);
        User friend = getUserByIdOrThrow(friendId);
        userStorage.addFriend(user, friend);
        log.info("Пользователь {} добавил в друзья {}", user.getId(), friend.getId());
    }

    public void removeFriend(long userId, long friendId) {
        User user = getUserByIdOrThrow(userId);
        User friend = getUserByIdOrThrow(friendId);
        userStorage.removeFriend(user, friend);
        log.info("Пользователь {} удалил из друзей {}", user.getId(), friend.getId());
    }

    public Collection<User> getFriends(long userId) {
        User user = getUserByIdOrThrow(userId);
        return userStorage.getFriends(user);
    }

    public Collection<User> getCommonFriends(long userId, long otherId) {
        User user = getUserByIdOrThrow(userId);
        User other = getUserByIdOrThrow(otherId);
        return userStorage.getCommonFriends(user, other);
    }

    private User getUserByIdOrThrow(long id) {
        return userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден"));
    }
}