package ru.yandex.practicum.filmorateApp.validation;

import ru.yandex.practicum.filmorateApp.exception.ValidationException;
import ru.yandex.practicum.filmorateApp.model.User;

import java.time.LocalDate;

public class UserValidator {

    public static void validate(User user) throws ValidationException {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Имейл должен быть указан");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин должен быть указан и не иметь пробелов");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
