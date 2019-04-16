package by.radchuk.task.dao.framework;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * A factory for connections to the physical data source that this
 * {@code ConnectionManager} object represents.
 *
 * @author Dmitry Radchuk
 */
public interface ConnectionManager {
    /**
     * <p>Attempts to establish a connection with the data source that
     * this {@code ConnectionManager} object represents.
     *
     * @return  a connection to the data source
     * @exception SQLException if a database access error occurs
     */
    Connection getConnection() throws SQLException;
}
