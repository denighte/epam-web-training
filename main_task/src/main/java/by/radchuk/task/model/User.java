package by.radchuk.task.model;

import lombok.*;

@Data
@Builder
public class User {
    int id;
    String login;
    String passwordHash;
    String name;
    String surname;
    String imageLink;
}

