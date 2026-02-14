package ru.yandex.practicum.filmorateApp.validation;


import ru.yandex.practicum.filmorateApp.exception.ValidationException;
import ru.yandex.practicum.filmorateApp.model.User;

import java.time.LocalDate;
import org.springframework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserValidator {

    public static void validate(User user) throws ValidationException {
        log.info("Начало валидации пользователя: {}", user);

        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Ошибка валидации email для пользователя {}: {}", user, user.getEmail());
            throw new ValidationException("Имейл должен быть указан и содержать '@'");
        }

        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Ошибка валидации логина для пользователя {}: {}", user, user.getLogin());
            throw new ValidationException("Логин должен быть указан и не иметь пробелов");
        }

        if (!StringUtils.hasText(user.getName())) {
            user.setName(user.getLogin());
            log.debug("Имя пользователя не указано. Присвоено имя по логину: {}", user.getName());
        }

        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Ошибка валидации даты рождения для пользователя {}: {}", user, user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем");
        }

        log.debug("Валидация пользователя {} пройдена успешно", user);
    }
}

