package model;

import java.sql.Time;

import lombok.Data;

@Data
public class Film {
    private int id;
    private String name;
    private String description;
    private Time duration;
}
