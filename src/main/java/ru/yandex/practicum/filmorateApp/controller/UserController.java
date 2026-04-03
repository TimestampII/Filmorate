package ru.yandex.practicum.filmorateApp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorateApp.model.User;
import ru.yandex.practicum.filmorateApp.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("GET /users");
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable long id) {
        log.info("GET /users/{}", id);
        return userService.findByIdUser(id);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("POST /users: {}", user);
        return userService.addUser(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.info("PUT /users: {}", user);
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("PUT /users/{}/friends/{}", id, friendId);
        userService.addFriendUser(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("DELETE /users/{}/friends/{}", id, friendId);
        userService.removeFriendUser(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable long id) {
        log.info("GET /users/{}/friends", id);
        return userService.getFriendsUser(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(
            @PathVariable long id,
            @PathVariable long otherId) {
        log.info("GET /users/{}/friends/common/{}", id, otherId);
        return userService.getCommonFriendsUser(id, otherId);
    }
}