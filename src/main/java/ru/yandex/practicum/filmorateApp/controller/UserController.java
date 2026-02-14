package ru.yandex.practicum.filmorateApp.controller;

import jakarta.validation.Valid;
import ru.yandex.practicum.filmorateApp.exception.NotFoundException;
import ru.yandex.practicum.filmorateApp.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorateApp.validation.UserValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        log.info("Получен запрос на вывод всех пользователей. Всего пользователей: {}", users.size());
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.debug("Добавление пользователя: {}", user);

        try {
            UserValidator.validate(user);
        } catch (Exception e) {
            log.warn("Ошибка валидации при добавлении пользователя {}; {}", user, e.getMessage());
            throw e;
        }

        user.setId(getNextUserId());
        users.put(user.getId(), user);

        log.debug("Пользователь успешно добавлен: {}", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) throws NotFoundException {
        log.debug("Обновление пользователя: {}", user);
        UserValidator.validate(user);

        if (!exists(user)) {
            log.warn("Пользователь с таким {} ID не найден при обновлении", user.getId());
            throw new NotFoundException("Пользователь с таким ID " + user.getId() + " отсутствует");
        }

        users.put(user.getId(), user);
        log.debug("Пользователь успешно обновлен: {}", user);
        return user;


    }

    private boolean exists(User user) {
        return users.containsKey(user.getId());
    }

    private long getNextUserId() {
        return users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0) + 1;
    }
}


