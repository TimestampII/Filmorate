package model;

import java.util.Date;
import lombok.Data;

@Data
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private Date birthday;
}
