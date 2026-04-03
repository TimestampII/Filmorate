package ru.yandex.practicum.filmorateApp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorateApp.exception.NotFoundException;
import ru.yandex.practicum.filmorateApp.model.User;
import ru.yandex.practicum.filmorateApp.storage.user.UserStorage;
import ru.yandex.practicum.filmorateApp.validation.UserValidator;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User add(User user) {
        UserValidator.validate(user);
        return userStorage.add(user);
    }

    public User update(User user) {
        UserValidator.validate(user);
        userStorage.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + user.getId() + " не найден"));
        return userStorage.update(user);
    }

    public User findById(long id) {
        return userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден"));
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public void addFriend(long userId, long friendId) {
        User user = findById(userId);
        User friend = findById(friendId); // проверяем существование
        user.getFriends().add(friendId);
        friend.getFriends().add(userId); // дружба взаимная
        log.info("Пользователи {} и {} теперь друзья", userId, friendId);
    }

    public void removeFriend(long userId, long friendId) {
        User user = findById(userId);       // 404 если userId не существует
        User friend = findById(friendId);   // 404 если friendId не существует
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        log.info("Пользователи {} и {} больше не друзья", userId, friendId);
    }

    public Collection<User> getFriends(long userId) {
        User user = findById(userId);
        return user.getFriends().stream()
                .map(this::findById)
                .collect(Collectors.toList());
    }

    public Collection<User> getCommonFriends(long userId, long otherId) {
        Set<Long> userFriends = findById(userId).getFriends();
        Set<Long> otherFriends = findById(otherId).getFriends();
        return userFriends.stream()
                .filter(otherFriends::contains)
                .map(this::findById)
                .collect(Collectors.toList());
    }
}