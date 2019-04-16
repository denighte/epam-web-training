package by.radchuk.task.app;

import by.radchuk.task.dao.framework.FixedConnectionPool;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;

public class Main {
    private static final String URL = "jdbc:h2:./h2db";
    private static final String LOGIN = "tully";
    private static final String PASSWORD = "tully";

    public static void main(String[] args) throws Exception {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(URL);
        dataSource.setUser(LOGIN);
        dataSource.setPassword(PASSWORD);
        FixedConnectionPool cp = FixedConnectionPool.create(dataSource, 10);
        Connection[] connections = new Connection[10];
        for (int i = 0; i < 11; ++i) {
            connections[i] = cp.getConnection();
            //conn.close();
        }
    }
}
