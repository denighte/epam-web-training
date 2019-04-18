package by.radchuk.task.app;

import by.radchuk.task.dao.framework.Executor;
import by.radchuk.task.dao.framework.FixedConnectionPool;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;

public class Main {
    private static final String URL = "jdbc:h2:./h2db";
    private static final String LOGIN = "tully";
    private static final String PASSWORD = "tully";

    public static void main(String[] args) throws Exception {
        Executor executor = new Executor();
        String login = "login";
        String password = "password";
        int id = executor.execSave("INSERT INTO users (login, password) VALUES (?, ?)",
                          login,
                          password);
        System.out.println(id);
    }
}
