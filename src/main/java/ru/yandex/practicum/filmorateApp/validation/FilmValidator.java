package ru.yandex.practicum.filmorateApp.validation;

import ru.yandex.practicum.filmorateApp.exception.ValidationException;
import ru.yandex.practicum.filmorateApp.model.Film;

import java.time.LocalDate;

public class FilmValidator {

    private static final LocalDate MIN_RELEASE_DATE =
            LocalDate.of(1895, 12, 28);


    public static void validate(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Поле имя должно быть заполненно");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidationException("Описание должно быть не больше 200 символов");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
        }
    }
}

