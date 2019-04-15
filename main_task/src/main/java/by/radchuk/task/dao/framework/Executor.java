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
     * Default constructor.
     * Initializes ConnectionManager.
     * Creates JDBC Connection instance.
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
     * disable autocommit of connection.
     * @throws SQLException db operation error.
     */
    public void beginTransaction() throws SQLException {
        connection.setAutoCommit(false);
    }

    /**
     * commit made changes.
     * enable autocommit of connection.
     * @throws SQLException
     */
    public void commitTransaction() throws SQLException {
        connection.commit();
        connection.setAutoCommit(true);
    }

    /**
     * Execute update query (no result set handling).
     * @param query sql query.
     * @param params parameters for statement
     * @throws SQLException db operation error.
     */
    public void execUpdate(@NonNull String query,
                           String ... params) throws SQLException {
        initStatement(query);
        setStatementParameters(params);
        statement.execute();
    }

    /**
     * Execute query (with result set handling).
     * @param query sql query.
     * @param handler ResultSet handler function.
     * @param params parameters for statement.
     * @param <R> handler return type.
     * @return Object
     * @throws SQLException db operation error.
     */
    public <R> R execQuery(@NonNull String query,
                           ResultHandler<R> handler,
                           String ... params)
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
     * @throws SQLException db operation errors.
     */
    private void setStatementParameters(final String ... params)
                                            throws SQLException {
        var paramsNumber = (int)cachedQuery.chars().filter(ch -> ch == '?').count();
        if (params.length != paramsNumber) {
            throw new SQLException("Invalid number of parameters!");
        }
        for (var i = 0; i < params.length; ++i) {
            statement.setString(i + 1, params[i]);
        }
    }

    /**
     * Close resources 
     * @throws SQLException
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