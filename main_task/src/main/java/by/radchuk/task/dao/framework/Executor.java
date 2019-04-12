package by.radchuk.task.dao.framework;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.apache.commons.dbutils.DbUtils;

import java.sql.*;

@AllArgsConstructor
public class Executor {
    @NonNull private final Connection connection;

    public void execUpdate(String query,
                           String ... params) throws SQLException {
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; ++i) {
                statement.setString(i, params[i]);
            }
            statement.execute();
        }
    }

    public <R> R execQuery(String query,
                           ResultHandler<R> handler,
                           String ... params)
                                    throws SQLException {
        ResultSet result = null;
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            for(int i = 0; i < params.length; ++i) {
                statement.setString(i, params[i]);
            }
            statement.execute();
            result = statement.getResultSet();
            return handler.apply(result);
        } finally {
            DbUtils.closeQuietly(result);
        }
    }

}