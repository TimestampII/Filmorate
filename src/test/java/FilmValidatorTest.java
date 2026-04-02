package ru.yandex.practicum.filmorateApp.validation;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorateApp.exception.ValidationException;
import ru.yandex.practicum.filmorateApp.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidatorTest {

    @Test
    void shouldValidateFilmSuccessfully() {
        // Given: корректный фильм
        Film film = new Film();
        film.setName("Интерстеллар");
        film.setDescription("Научно-фантастический фильм");
        film.setReleaseDate(LocalDate.of(2014, 11, 7));
        film.setDuration(169);

        // When & Then: валидация должна пройти без исключений
        assertDoesNotThrow(() -> FilmValidator.validate(film));
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        // Given: фильм с null именем
        Film film = new Film();
        film.setName(null);
        film.setDescription("Описание");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120);

        // When & Then: должно быть выброшено исключение
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> FilmValidator.validate(film)
        );
        assertEquals("Поле имя должно быть заполнено", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        // Given: фильм с пустым именем
        Film film = new Film();
        film.setName("");
        film.setDescription("Описание");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120);

        // When & Then: должно быть выброшено исключение
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> FilmValidator.validate(film)
        );
        assertEquals("Поле имя должно быть заполнено", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        // Given: фильм с именем из пробелов
        Film film = new Film();
        film.setName("   ");
        film.setDescription("Описание");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120);

        // When & Then: должно быть выброшено исключение
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> FilmValidator.validate(film)
        );
        assertEquals("Поле имя должно быть заполнено", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDescriptionExceeds200Characters() {
        // Given: фильм с описанием длиной 201 символ
        Film film = new Film();
        film.setName("Название");
        film.setDescription("а".repeat(201));
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120);

        // When & Then: должно быть выброшено исключение
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> FilmValidator.validate(film)
        );
        assertEquals("Описание должно быть не больше 200 символов", exception.getMessage());
    }

    @Test
    void shouldValidateWhenDescriptionIs200Characters() {
        // Given: фильм с описанием ровно 200 символов (граничное условие)
        Film film = new Film();
        film.setName("Название");
        film.setDescription("а".repeat(200));
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120);

        // When & Then: валидация должна пройти
        assertDoesNotThrow(() -> FilmValidator.validate(film));
    }

    @Test
    void shouldValidateWhenDescriptionIs199Characters() {
        // Given: фильм с описанием 199 символов
        Film film = new Film();
        film.setName("Название");
        film.setDescription("а".repeat(199));
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120);

        // When & Then: валидация должна пройти
        assertDoesNotThrow(() -> FilmValidator.validate(film));
    }

    @Test
    void shouldValidateWhenDescriptionIsEmpty() {
        // Given: фильм с пустым описанием
        Film film = new Film();
        film.setName("Название");
        film.setDescription("");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120);

        // When & Then: валидация должна пройти
        assertDoesNotThrow(() -> FilmValidator.validate(film));
    }

    @Test
    void shouldThrowExceptionWhenReleaseDateIsNull() {
        // Given: фильм с null датой релиза
        Film film = new Film();
        film.setName("Название");
        film.setDescription("Описание");
        film.setReleaseDate(null);
        film.setDuration(120);

        // When & Then: должно быть выброшено исключение
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> FilmValidator.validate(film)
        );
        assertEquals("Дата релиза — не раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenReleaseDateIsBefore1895() {
        // Given: фильм с датой релиза 27 декабря 1895
        Film film = new Film();
        film.setName("Название");
        film.setDescription("Описание");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(120);

        // When & Then: должно быть выброшено исключение
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> FilmValidator.validate(film)
        );
        assertEquals("Дата релиза — не раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    void shouldValidateWhenReleaseDateIs28December1895() {
        // Given: фильм с датой релиза ровно 28 декабря 1895 (граничное условие)
        Film film = new Film();
        film.setName("Название");
        film.setDescription("Описание");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(120);

        // When & Then: валидация должна пройти
        assertDoesNotThrow(() -> FilmValidator.validate(film));
    }

    @Test
    void shouldValidateWhenReleaseDateIs29December1895() {
        // Given: фильм с датой релиза 29 декабря 1895
        Film film = new Film();
        film.setName("Название");
        film.setDescription("Описание");
        film.setReleaseDate(LocalDate.of(1895, 12, 29));
        film.setDuration(120);

        // When & Then: валидация должна пройти
        assertDoesNotThrow(() -> FilmValidator.validate(film));
    }

    @Test
    void shouldThrowExceptionWhenDurationIsZero() {
        // Given: фильм с нулевой продолжительностью
        Film film = new Film();
        film.setName("Название");
        film.setDescription("Описание");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(0);

        // When & Then: должно быть выброшено исключение
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> FilmValidator.validate(film)
        );
        assertEquals("Продолжительность фильма должна быть положительным числом.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDurationIsNegative() {
        // Given: фильм с отрицательной продолжительностью
        Film film = new Film();
        film.setName("Название");
        film.setDescription("Описание");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(-10);

        // When & Then: должно быть выброшено исключение
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> FilmValidator.validate(film)
        );
        assertEquals("Продолжительность фильма должна быть положительным числом.", exception.getMessage());
    }

    @Test
    void shouldValidateWhenDurationIsOne() {
        // Given: фильм с продолжительностью 1 минута (граничное условие)
        Film film = new Film();
        film.setName("Название");
        film.setDescription("Описание");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(1);

        // When & Then: валидация должна пройти
        assertDoesNotThrow(() -> FilmValidator.validate(film));
    }
}