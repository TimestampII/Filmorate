package ru.yandex.practicum.filmorateApp.model;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Film {
    private long id;
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
}
