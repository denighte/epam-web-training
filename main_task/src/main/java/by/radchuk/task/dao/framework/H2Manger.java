package by.radchuk.task.dao.framework;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.prefs.Preferences;

/**
 * H2 database connection manager. Singleton.
 * It wraps the <code>Connection</code> objects creation.
 * Provides a simple connection pooling.
 *
 * NOTE: it would be great to use IOC container for this class.
 *
 * @see FixedConnectionPool
 *
 * @author Dmitry Radchuk
 */
@Slf4j
public final class H2Manger implements ConnectionManager, AutoCloseable {
    /**
     * <code>Preferences</code> object for connection manager.
     * Stores the init values of db and connection pool.
     */
    private static final Preferences DB_PREFERENCES
            = Preferences.userNodeForPackage(H2Manger.class);
    /**
     * <code>FixedConnectionPool</code> object size key.
     */
    private static final String POOL_SIZE = "db_pool_size";
    /**
     * Default <code>FixedConnectionPool</code> object size.
     */
    private static final int DEFAULT_POOL_SIZE = 10;
    /**
     * Database run option:
     * default - default run option, only table creation.
     * test - test run option, table + test data creation.
     */
    private static final String SQL_RUN_OPTION = "db_run_option";
    /**
     * Database default run option.
     */
    private static final String SQL_DEFAULT_RUN_OPTION = "default";
    /**
     * Database test run option.
     */
    private static final String SQL_TEST_RUN_OPTION = "test";
    /**
     * Database init sql script file key.
     */
    private static final String SQL_INIT_SCRIPT = "db_init_file";
    /**
     * Database test sql script file key.
     */
    private static final String SQL_TEST_SCRIPT = "db_test_file";
    /**
     * Database init sql script file.
     * This file stores Tables creation queries, and other
     * database initialization queries.
     */
    private static final String SQL_DEFAULT_INIT_SCRIPT
            = "/sql/script/script.sql";
    /**
     * Database test sql script file.
     * This file stores test data queries.
     */
    private static final String SQL_DEFAULT_TEST_DATA_SCRIPT
            = "/sql/script/test.sql";
    /**
     * URL to database.
     */
    private static final String URL = "db_url";
    /**
     * Database user login.
     */
    private static final String LOGIN = "db_login";
    /**
     * Database user password.
     */
    private static final String PASSWORD = "db_password";
    /**
     * Default URL to database.
     */
    private static final String DEFAULT_URL = "jdbc:h2:./h2db";
    /**
     * Default database user login.
     */
    private static final String DEFAULT_LOGIN = "tully";
    /**
     * Default database user password.
     */
    private static final String DEFAULT_PASSWORD = "tully";

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
        dataSource.setURL(DB_PREFERENCES.get(URL, DEFAULT_URL));
        dataSource.setUser(DB_PREFERENCES.get(LOGIN, DEFAULT_LOGIN));
        dataSource.setPassword(DB_PREFERENCES.get(PASSWORD, DEFAULT_PASSWORD));
        connectionPool = FixedConnectionPool.create(dataSource,
                     DB_PREFERENCES.getInt(POOL_SIZE, DEFAULT_POOL_SIZE));
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
     * Runs sql script files with specified path.
     *
     * @param paths paths to script files.
     * @throws SQLException if script file can't be read.
     */
    private void runScripts(final String... paths) throws SQLException {
        try {
            for (String path : paths) {
                InputStream stream = this.getClass().getResourceAsStream(path);
                if (stream == null) {
                    throw new NullPointerException(
                            "no resource with this name is found"
                    );
                }
                RunScript.execute(getConnection(),
                        new BufferedReader(new InputStreamReader(stream)));
            }
        } catch (NullPointerException exception) {
            log.error("Fatal: failed to read SQL script file.", exception);
            throw new SQLException(exception);
        } catch (SQLException exception) {
            log.error("Fatal: failed to execute SQL script.", exception);
            throw exception;
        }
    }

    /**
     * Set up database.
     * @throws SQLException if script file can't be read.
     */
    private void setUpDatabase() throws SQLException {
        String runOption = DB_PREFERENCES.get(SQL_RUN_OPTION,
                                             SQL_DEFAULT_RUN_OPTION);
        String init = DB_PREFERENCES.get(SQL_INIT_SCRIPT,
                                        SQL_DEFAULT_INIT_SCRIPT);
        String test = DB_PREFERENCES.get(SQL_TEST_SCRIPT,
                                        SQL_DEFAULT_TEST_DATA_SCRIPT);
        switch (runOption) {
            case SQL_DEFAULT_RUN_OPTION:
                runScripts(init);
                break;
            case SQL_TEST_RUN_OPTION:
                runScripts(init, test);
                break;
            default:
                log.warn("Unknown option.");
        }
    }

    /**
     * Runs database script files.
     * @param args console parameters.
     */
    public static void main(final String[] args) {
        try {
            getInstance().setUpDatabase();
        } catch (SQLException exception) {
            log.error("Failed to init Database: SQL exception!", exception);
        }

    }

    @Override
    public void close() throws Exception {
        connectionPool.dispose();
    }
}
