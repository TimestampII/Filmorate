package ru.yandex.practicum.filmorateApp.validation;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorateApp.exception.ValidationException;
import ru.yandex.practicum.filmorateApp.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    @Test
    void shouldValidateUserSuccessfully() {
        // Given: корректный пользователь
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("user123");
        user.setName("Иван Иванов");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        // When & Then: валидация должна пройти без исключений
        assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNull() {
        // Given: пользователь с null email
        User user = new User();
        user.setEmail(null);
        user.setLogin("user123");
        user.setName("Имя");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        // When & Then: должно быть выброшено исключение
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> UserValidator.validate(user)
        );
        assertEquals("Имейл должен быть указан и содержать '@'", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenEmailIsEmpty() {
        // Given: пользователь с пустым email
        User user = new User();
        user.setEmail("");
        user.setLogin("user123");
        user.setName("Имя");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        // When & Then: должно быть выброшено исключение
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> UserValidator.validate(user)
        );
        assertEquals("Имейл должен быть указан и содержать '@'", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenEmailIsBlank() {
        // Given: пользователь с email из пробелов
        User user = new User();
        user.setEmail("   ");
        user.setLogin("user123");
        user.setName("Имя");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        // When & Then: должно быть выброшено исключение
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> UserValidator.validate(user)
        );
        assertEquals("Имейл должен быть указан и содержать '@'", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenEmailDoesNotContainAtSymbol() {
        // Given: пользователь с email без символа @
        User user = new User();
        user.setEmail("userexample.com");
        user.setLogin("user123");
        user.setName("Имя");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        // When & Then: должно быть выброшено исключение
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> UserValidator.validate(user)
        );
        assertEquals("Имейл должен быть указан и содержать '@'", exception.getMessage());
    }

    @Test
    void shouldValidateWhenEmailContainsAtSymbol() {
        // Given: пользователь с корректным email
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("user123");
        user.setName("Имя");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        // When & Then: валидация должна пройти
        assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    @Test
    void shouldThrowExceptionWhenLoginIsNull() {
        // Given: пользователь с null логином
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin(null);
        user.setName("Имя");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        // When & Then: должно быть выброшено исключение
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> UserValidator.validate(user)
        );
        assertEquals("Логин должен быть указан и не иметь пробелов", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenLoginIsEmpty() {
        // Given: пользователь с пустым логином
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("");
        user.setName("Имя");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        // When & Then: должно быть выброшено исключение
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> UserValidator.validate(user)
        );
        assertEquals("Логин должен быть указан и не иметь пробелов", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenLoginIsBlank() {
        // Given: пользователь с логином из пробелов
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("   ");
        user.setName("Имя");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        // When & Then: должно быть выброшено исключение
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> UserValidator.validate(user)
        );
        assertEquals("Логин должен быть указан и не иметь пробелов", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenLoginContainsSpaces() {
        // Given: пользователь с логином, содержащим пробелы
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("user 123");
        user.setName("Имя");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        // When & Then: должно быть выброшено исключение
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> UserValidator.validate(user)
        );
        assertEquals("Логин должен быть указан и не иметь пробелов", exception.getMessage());
    }

    @Test
    void shouldValidateWhenLoginHasNoSpaces() {
        // Given: пользователь с логином без пробелов
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("user123");
        user.setName("Имя");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        // When & Then: валидация должна пройти
        assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    @Test
    void shouldUseLoginAsNameWhenNameIsNull() {
        // Given: пользователь с null именем
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("user123");
        user.setName(null);
        user.setBirthday(LocalDate.of(1990, 1, 1));

        // When: выполняем валидацию
        assertDoesNotThrow(() -> UserValidator.validate(user));

        // Then: имя должно быть установлено равным логину
        assertEquals("user123", user.getName());
    }

    @Test
    void shouldUseLoginAsNameWhenNameIsEmpty() {
        // Given: пользователь с пустым именем
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("user123");
        user.setName("");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        // When: выполняем валидацию
        assertDoesNotThrow(() -> UserValidator.validate(user));

        // Then: имя должно быть установлено равным логину
        assertEquals("user123", user.getName());
    }

    @Test
    void shouldUseLoginAsNameWhenNameIsBlank() {
        // Given: пользователь с именем из пробелов
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("user123");
        user.setName("   ");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        // When: выполняем валидацию
        assertDoesNotThrow(() -> UserValidator.validate(user));

        // Then: имя должно быть установлено равным логину
        assertEquals("user123", user.getName());
    }

    @Test
    void shouldNotChangeNameWhenNameIsProvided() {
        // Given: пользователь с указанным именем
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("user123");
        user.setName("Иван Иванов");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        // When: выполняем валидацию
        assertDoesNotThrow(() -> UserValidator.validate(user));

        // Then: имя не должно измениться
        assertEquals("Иван Иванов", user.getName());
    }

    @Test
    void shouldThrowExceptionWhenBirthdayIsInFuture() {
        // Given: пользователь с датой рождения в будущем
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("user123");
        user.setName("Имя");
        user.setBirthday(LocalDate.now().plusDays(1));

        // When & Then: должно быть выброшено исключение
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> UserValidator.validate(user)
        );
        assertEquals("Дата рождения не может быть в будущем", exception.getMessage());
    }

    @Test
    void shouldValidateWhenBirthdayIsToday() {
        // Given: пользователь с датой рождения сегодня (граничное условие)
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("user123");
        user.setName("Имя");
        user.setBirthday(LocalDate.now());

        // When & Then: валидация должна пройти
        assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    @Test
    void shouldValidateWhenBirthdayIsInPast() {
        // Given: пользователь с датой рождения в прошлом
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("user123");
        user.setName("Имя");
        user.setBirthday(LocalDate.now().minusDays(1));

        // When & Then: валидация должна пройти
        assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    @Test
    void shouldValidateWhenBirthdayIsNull() {
        // Given: пользователь с null датой рождения
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("user123");
        user.setName("Имя");
        user.setBirthday(null);

        // When & Then: валидация должна пройти
        assertDoesNotThrow(() -> UserValidator.validate(user));
    }
}