package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class FilmValidator {

    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate MIN_RELEASE_DATE =
            LocalDate.of(1895, 12, 28);


    public static void validate(Film film) throws ValidationException {
        log.info("Начата валидация фильма: {}", film);

        if (!StringUtils.hasText(film.getName())) {
            log.warn("Ошибка валидации: имя фильма пустое или null: {}", film);
            throw new ValidationException("Поле имя должно быть заполнено");
        }

        if (film.getDescription() != null && film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            log.warn("Ошибка валидации: описание фильма слишком длинное ({} символов): {}",
                    film.getDescription().length(), film);
            throw new ValidationException("Описание должно быть не больше 200 символов");
        }

        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.warn("Ошибка валидации: дата релиза {} раньше минимальной {}: {}",
                    film.getReleaseDate(), MIN_RELEASE_DATE, film);
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }

        if (film.getDuration() <= 0) {
            log.warn("Ошибка валидации: продолжительность фильма отрицательная или нулевая ({}): {}",
                    film.getDuration(), film);
            throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
        }

        log.info("Валидация фильма {} пройдена успешно", film);
    }
}

