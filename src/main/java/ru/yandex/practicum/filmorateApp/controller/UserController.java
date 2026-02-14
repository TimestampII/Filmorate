package ru.yandex.practicum.filmorateApp.controller;

import ru.yandex.practicum.filmorateApp.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorateApp.validation.UserValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {

        UserValidator.validate(user);
        user.setId(getNextUserId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {

        UserValidator.validate(user);

        if (!users.containsKey(user.getId())) {
            throw new IllegalArgumentException("Пользователь с таким ID " + user.getId() + " отсутствует");
        }
        users.put(user.getId(), user);
        return user;


    }

    private long getNextUserId() {
        return users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0) + 1;
    }
}


