package ru.yandex.practicum.filmorateApp.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

import lombok.Data;

@Data
public class User {
    private long id;
    @Email
    @NotBlank(message = "Имейл должен быть указан")
    private String email;
    @NotBlank(message = "Логин должен быть указан")
    private String login;
    private String name;
    private LocalDate birthday;
}
