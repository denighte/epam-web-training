package by.radchuk.task.dao.framework;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultHandler<R> {
    R apply(ResultSet resultSet) throws SQLException;
}