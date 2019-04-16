package by.radchuk.task.dao.framework;


import lombok.extern.slf4j.Slf4j;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * H2 database connection manager. Singleton.
 * It wraps the <code>Connection</code> objects creation.
 * Provides a simple connection pooling.
 *
 * @see FixedConnectionPool
 *
 * @author Dmitry Radchuk
 */
@Slf4j
public final class H2Manger implements ConnectionManager {
    /**
     * <code>FixedConnectionPool</code> object size.
     */
    private static final int JDBC_CONNECTION_POOL_SIZE = 10;
    /**
     * Database init sql script file.
     * This file stores Tables creation queries, and other
     * database initialization queries.
     */
    private static final String SQL_INIT_SCRIPT
            = "src/main/resources/sql/script/script.sql";
    /**
     * Database test sql script file.
     * This file stores test data queries.
     */
    private static final String SQL_TEST_DATA_SCRIPT
            = "src/main/resources/sql/script/test.sql";
    /**
     * URL to database.
     */
    private static final String URL = "jdbc:h2:./h2db";
    /**
     * Database user login.
     */
    private static final String LOGIN = "tully";
    /**
     * Database user password.
     */
    private static final String PASSWORD = "tully";

    /**
     * Singleton instance.
     */
    private static final H2Manger INSTANCE = new H2Manger();

    /**
     * Singleton instance getter.
     * @return <code>H2Manger</code> object instance.
     */
    static H2Manger getInstance() {
        return INSTANCE;
    }

    /**
     * Simple connection pool.
     * @see FixedConnectionPool
     */
    private FixedConnectionPool connectionPool;

    /**
     * H2 connection manager default constructor.
     * set ups the <code>Connection</code> objects pool.
     */
    private H2Manger() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(URL);
        dataSource.setUser(LOGIN);
        dataSource.setPassword(PASSWORD);
        connectionPool = FixedConnectionPool.create(dataSource,
                                      JDBC_CONNECTION_POOL_SIZE);
    }

    /**
     * Gets JDBC connection from the connection pool.
     * @return new <code>Connection</code> object.
     * @throws SQLException if a new connection could not be established,
     * or a loginTimeout occurred
     */
    @Override
    public Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

    /**
     * Runs database init script file.
     * @throws SQLException if script file can't be read.
     */
    private void initDatabase() throws SQLException {
        try {
            RunScript.execute(getConnection(),
                              new FileReader(SQL_INIT_SCRIPT));
        } catch (FileNotFoundException exception) {
            log.error("Failed to read SQL script file:"
                      + " File not found!", exception);
        }
    }

    /**
     * Runs database test script file.
     * @throws SQLException if script file can't be read.
     */
    private void initTestDatabase() throws SQLException {
        try {
            RunScript.execute(getConnection(),
                    new FileReader(SQL_INIT_SCRIPT));
            RunScript.execute(getConnection(),
                    new FileReader(SQL_TEST_DATA_SCRIPT));
        } catch (FileNotFoundException exception) {
            log.error("Failed to read SQL script file:"
                      + " File not found!", exception);
        }
    }

    /**
     * Runs database script files.
     * @param args console parameters.
     */
    public static void main(final String[] args) {
        try {
            getInstance().initTestDatabase();
        } catch (SQLException exception) {
            log.error("Failed to init Database: SQL exception!", exception);
        }

    }
}
