package by.radchuk.task.model;

import lombok.Builder;
import lombok.Data;

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
