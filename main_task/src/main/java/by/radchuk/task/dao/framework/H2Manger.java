package by.radchuk.task.dao.framework;


import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;
import java.sql.SQLException;
public final class H2Manger implements ConnectionManager {
    private static final String URL = "jdbc:h2:./h2db";
    private static final String LOGIN = "tully";
    private static final String PASSWORD = "tully";

    private static final H2Manger INSTANCE = new H2Manger();
    public static H2Manger getInstance() {
        return INSTANCE;
    }

    private JdbcDataSource dataSource;
    private JdbcConnectionPool connectionPool;

    private H2Manger() {
        dataSource = new JdbcDataSource();
        dataSource.setURL(URL);
        dataSource.setUser(LOGIN);
        dataSource.setPassword(PASSWORD);
        connectionPool = JdbcConnectionPool.create(dataSource);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

    //    public long addUser(String name) throws DBException {
//        try {
//            connection.setAutoCommit(false);
//            UsersDAO dao = new UsersDAO(connection);
//            dao.createTable();
//            dao.insertUser(name);
//            connection.commit();
//            return dao.getUserId(name);
//        } catch (SQLException e) {
//            try {
//                connection.rollback();
//            } catch (SQLException ignore) {
//            }
//            throw new DBException(e);
//        } finally {
//            try {
//                connection.setAutoCommit(true);
//            } catch (SQLException ignore) {
//            }
//        }
//    }
}