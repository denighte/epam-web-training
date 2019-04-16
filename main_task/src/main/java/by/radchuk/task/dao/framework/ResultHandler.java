package by.radchuk.task.dao.framework;

import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Represents a function that accepts one argument and produces a result.
 *
 * <p>This is a functional interface
 * whose functional method is {@link #apply(ResultSet)}.
 *
 * @param <R> the type of the result of the function
 */
@FunctionalInterface
public interface ResultHandler<R> {
    /**
     * Applies this function to the given <code>resultSet</code>.
     *
     * @param resultSet <code>ResultSet</code> object.
     * @return the function result
     */
    R apply(ResultSet resultSet) throws SQLException;
}