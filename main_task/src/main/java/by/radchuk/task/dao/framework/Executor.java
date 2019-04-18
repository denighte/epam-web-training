package by.radchuk.task.dao.framework;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import java.sql.*;

/**
 * Queries executor.
 * Forwards queries to sql database.
 * Wraps up work with jdbc Connection and Statement classes.
 * Provides basic last query caching.
 * It is used as follows:
 * <pre>
 * import java.sql.*;
 * import by.radchuk.task.dao.framework.Executor;
 * public class Test {
 *     public static void main(String ... args) throws Exception {
 *         Executor executor = new Executor();
 *         String login = "login";
 *         String password = "password";
 *         int id = executor.execSave(
 *             "INSERT INTO users (login, password) VALUES (?, ?)",
 *             login,
 *             password
 *         );
 *         System.out.println(id);
 *     }
 * }
 * </pre>
 * @author Dmitry Radchuk
 */
@Slf4j
public class Executor implements AutoCloseable {
    //@Autowired
    /**
     * Connection manager.
     */
    private ConnectionManager manager;
    /**
     * JDBC connection.
     */
    private Connection connection;
    /**
     * JDBC PreparedStatement.
     * Contains statement class for cachedQuery.
     */
    private PreparedStatement statement;
    /**
     * cached query.
     * simply it's the last asked query.
     */
    private String cachedQuery;

    /**
     * Executor default constructor.
     * Initializes ConnectionManager (in this case it is a H2Manager).
     * Gets JDBC connection instance.
     */
    public Executor() {
        manager = H2Manger.getInstance();
        try {
            connection = manager.getConnection();
        } catch (SQLException exception) {
            log.error("Failed to establish connection!", exception);
            connection = null;
        }
    }

    /**
     * disable autocommit of <code>Connection</code> object.
     * @throws SQLException if <code>Connection</code> object could not be used.
     */
    public void beginTransaction() throws SQLException {
        connection.setAutoCommit(false);
    }

    /**
     * commit made changes.
     * enable autocommit of connection.
     * @throws SQLException if <code>Connection</code> object could not be used.
     */
    public void commitTransaction() throws SQLException {
        connection.commit();
        connection.setAutoCommit(true);
    }

    /**
     * Executes save query and returns id of the saved object
     * @param query sql query.
     * @param params parameters for statement
     * @return saved object id
     * @throws SQLException if <code>Connection</code> or
     * <code>PreparedStatement</code> objects could not be used.
     */
    public int execSave(@NonNull final String query,
                                  final String... params) throws SQLException {
        initStatement(query);
        setStatementParameters(params);
        var affectedRows = statement.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Saving failed, no rows affected.");
        }
        @Cleanup ResultSet resultSet = statement.getGeneratedKeys();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        throw new SQLException("Saving failed, no ID obtained.");
    }

    /**
     * Execute update query (no result set handling).
     * @param query sql query.
     * @param params parameters for statement
     * @return number of rows affected.
     * @throws SQLException if <code>Connection</code> or
     * <code>PreparedStatement</code> objects could not be used.
     */
    public int execUpdate(@NonNull final String query,
                           final String... params) throws SQLException {
        initStatement(query);
        setStatementParameters(params);
        return statement.executeUpdate();
    }

    /**
     * Execute query (with result set handling).
     * @param query sql query.
     * @param handler ResultSet handler function.
     * @param params parameters for statement.
     * @param <R> handler return type.
     * @return Object
     * @throws SQLException if <code>Connection</code> or
     * <code>PreparedStatement</code> objects could not be used.
     */
    public <R> R execQuery(@NonNull final String query,
                           final ResultHandler<R> handler,
                           final String... params)
                                    throws SQLException {
        initStatement(query);
        setStatementParameters(params);
        statement.execute();
        @Cleanup ResultSet result = statement.getResultSet();
        return handler.apply(result);
    }

    /**
     * init statement for given query or use cached.
     * @param query sql query
     * @throws SQLException db operation error.
     */
    private void initStatement(final String query)  throws SQLException {
        if (statement == null) {
            statement = connection.prepareStatement(query);
            cachedQuery = query;
        } else if (!query.equals(cachedQuery)) {
            statement.close();
            statement = connection.prepareStatement(query);
            cachedQuery = query;
        }
    }

    /**
     * set Statement parameters.
     * @param params parameters for statement.
     * @throws SQLException if <code>Statement</code> object could not be used.
     */
    private void setStatementParameters(final String... params)
                                            throws SQLException {
        var paramsNumber = (int) cachedQuery.chars()
                                           .filter(ch -> ch == '?').count();
        if (params.length != paramsNumber) {
            throw new SQLException("Invalid number of parameters!");
        }
        for (var i = 0; i < params.length; ++i) {
            statement.setString(i + 1, params[i]);
        }
    }

    /**
     * Closes resources.
     * @throws SQLException if can't close <code>Connection</code>
     * or(and) <code>PreparedStatement</code> objects.
     */
    @Override
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
        if (statement != null && !statement.isClosed()) {
            statement.close();
        }
    }
}
