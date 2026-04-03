package ru.yandex.practicum.filmorateApp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorateApp.exception.NotFoundException;
import ru.yandex.practicum.filmorateApp.model.User;
import ru.yandex.practicum.filmorateApp.storage.user.UserStorage;
import ru.yandex.practicum.filmorateApp.validation.UserValidator;

import java.util.Collection;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        UserValidator.validate(user);
        return userStorage.add(user);
    }

    public User updateUser(User user) {
        UserValidator.validate(user);
        getUserOrThrow(user.getId()); // проверка существования перед обновлением
        return userStorage.update(user);
    }

    public User findByIdUser(long id) {
        return getUserOrThrow(id);
    }

    public Collection<User> findAllUsers() {
        return userStorage.findAll();
    }

    public void addFriendUser(long userId, long friendId) {
        getUserOrThrow(userId);   // проверка обоих пользователей
        getUserOrThrow(friendId);
        userStorage.addFriend(userId, friendId); // логика дружбы — в хранилище
        log.info("Пользователи {} и {} теперь друзья", userId, friendId);
    }

    public void removeFriendUser(long userId, long friendId) {
        getUserOrThrow(userId);   // проверка обоих пользователей
        getUserOrThrow(friendId);
        userStorage.removeFriend(userId, friendId); // логика дружбы — в хранилище
        log.info("Пользователи {} и {} больше не друзья", userId, friendId);
    }

    public Collection<User> getFriendsUser(long userId) {
        getUserOrThrow(userId);
        return userStorage.getFriends(userId); // выборка — в хранилище
    }

    public Collection<User> getCommonFriendsUser(long userId, long otherId) {
        getUserOrThrow(userId);
        getUserOrThrow(otherId);
        return userStorage.getCommonFriends(userId, otherId); // фильтрация — в хранилище
    }

    private User getUserOrThrow(long id) {
        return userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден"));
    }
}