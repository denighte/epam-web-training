package by.radchuk.task.app;

import by.radchuk.task.model.User;
import lombok.var;

import java.util.Arrays;


public class Main {
    private static final String URL = "jdbc:h2:./h2db";
    private static final String LOGIN = "tully";
    private static final String PASSWORD = "tully";

    public static void main(String[] args) throws Exception {
        User user = User.builder().name("name").id(5).build();
        for(var field : User.class.getDeclaredFields()) {
            field.setAccessible(true);
            System.out.println(field.get(user));
        }
    }

}
