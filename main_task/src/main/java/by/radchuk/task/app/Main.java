package by.radchuk.task.app;

import by.radchuk.task.dao.framework.Executor;
import by.radchuk.task.dao.framework.H2Manger;
import by.radchuk.task.dao.framework.Queries;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {

        Queries.getInstance();
//        String sql =  "CREATE TABLE   REGISTRATION " +
//                "(id INTEGER not NULL, " +
//                " first VARCHAR(255), " +
//                " last VARCHAR(255), " +
//                " age INTEGER, " +
//                " PRIMARY KEY ( id ))";
        //sql = "INSERT INTO Registration " + "VALUES (100, 'Zara', 'Ali', 18)";

        //sql = "INSERT INTO Registration " + "VALUES (101, 'Mahnaz', 'Fatma', 25)";

        //sql = "INSERT INTO Registration " + "VALUES (102, 'Zaid', 'Khan', 30)";

        //sql = "INSERT INTO Registration " + "VALUES(103, 'Sumit', 'Mittal', 28)";
        try (Connection connection = H2Manger.getInstance().getConnection(); Statement statement = connection.createStatement()) {
            Executor executor = new Executor(connection);
            executor.execQuery(Queries.getInstance().getQuery("users:getAll"), rs -> {
                while (rs.next()) {
                    // Retrieve by column name
                    int id = rs.getInt("id");
                    int age = rs.getInt("age");
                    String first = rs.getString("first");
                    String last = rs.getString("last");

                    // Display values
                    System.out.print("ID: " + id);
                    System.out.print(", Age: " + age);
                    System.out.print(", First: " + first);
                    System.out.println(", Last: " + last);
                }
                return 0;
            });
        } catch (SQLException exception) {
            System.out.println(exception.getErrorCode());
        }
    }
}
