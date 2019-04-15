package by.radchuk.task.dao.framework;


import lombok.extern.slf4j.Slf4j;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public final class H2Manger implements ConnectionManager {
    private static final String SQL_INIT_SCRIPT = "src/main/resources/sql/script/script.sql";
    private static final String SQL_TEST_DATA_SCRIPT = "src/main/resources/sql/script/test.sql";
    private static final String URL = "jdbc:h2:./h2db";
    private static final String LOGIN = "tully";
    private static final String PASSWORD = "tully";

    private static final H2Manger INSTANCE = new H2Manger();
    public static H2Manger getInstance() {
        return INSTANCE;
    }

    private JdbcConnectionPool connectionPool;

    private H2Manger() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(URL);
        dataSource.setUser(LOGIN);
        dataSource.setPassword(PASSWORD);
        connectionPool = JdbcConnectionPool.create(dataSource);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

    private void initDatabase() throws SQLException {
        try {
            RunScript.execute(getConnection(),
                              new FileReader(SQL_INIT_SCRIPT));
            RunScript.execute(getConnection(),
                              new FileReader(SQL_TEST_DATA_SCRIPT));
        } catch (FileNotFoundException exception) {
            log.error("Failed to read SQL script file: File not found!", exception);
        }
    }

    public static void main(String[] args) {
        try {
            getInstance().initDatabase();
        } catch (SQLException exception) {
            log.error("Failed to init Database: SQL exception!", exception);
        }

    }
}