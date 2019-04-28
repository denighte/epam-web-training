package by.radchuk.task.model;

import lombok.*;

@Data
@Builder
public class User {
    private int id;
    private String login;
    private String passwordHash;
    private String name;
    private String surname;
    private String imageLink;
    private byte level;
}

