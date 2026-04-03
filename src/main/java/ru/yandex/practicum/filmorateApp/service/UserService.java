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

    public User addUser(User user) {
        UserValidator.validate(user);
        return userStorage.add(user);
    }

    public User updateUser(User user) {
        UserValidator.validate(user);
        userStorage.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + user.getId() + " не найден"));
        return userStorage.update(user);
    }

    public User findByIdUser(long id) {
        return userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден"));
    }

    public Collection<User> findAllUsers() {
        return userStorage.findAll();
    }

    public void addFriendUser(long userId, long friendId) {
        User user = findByIdUser(userId);
        User friend = findByIdUser(friendId); // проверяем существование
        user.getFriends().add(friendId);
        friend.getFriends().add(userId); // дружба взаимная
        log.info("Пользователи {} и {} теперь друзья", userId, friendId);
    }

    public void removeFriendUser(long userId, long friendId) {
        User user = findByIdUser(userId);       // 404 если userId не существует
        User friend = findByIdUser(friendId);   // 404 если friendId не существует
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        log.info("Пользователи {} и {} больше не друзья", userId, friendId);
    }

    public Collection<User> getFriendsUser(long userId) {
        User user = findByIdUser(userId);
        return user.getFriends().stream()
                .map(this::findByIdUser)
                .collect(Collectors.toList());
    }

    public Collection<User> getCommonFriendsUser(long userId, long otherId) {
        Set<Long> userFriends = findByIdUser(userId).getFriends();
        Set<Long> otherFriends = findByIdUser(otherId).getFriends();
        return userFriends.stream()
                .filter(otherFriends::contains)
                .map(this::findByIdUser)
                .collect(Collectors.toList());
    }
}