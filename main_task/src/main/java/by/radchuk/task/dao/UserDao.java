package by.radchuk.task.dao;

import by.radchuk.task.dao.framework.Executor;
import by.radchuk.task.model.User;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.SQLException;

/**
 * User dao class.
 */
@NoArgsConstructor
public class UserDao implements AutoCloseable {
    /**
     * id column name.
     */
    private static final String ID = "id";
    /**
     * name column name.
     */
    private static final String LOGIN = "name";
    /**
     * password hash column name.
     */
    private static final String PASSWORD_HASH = "password_hash";
    /**
     * find user by id query.
     */
    private static final String FIND_USER_BY_ID
            = "SELECT id, name, password_hash FROM USER WHERE id = ?;";
    /**
     * find user by name query.
     */
    private static final String FIND_USER_BY_LOGIN
            = "SELECT id, name, password_hash FROM USER WHERE name = ?;";
    /**
     * save user query.
     */
    private static final String INSERT_USER
            = "INSERT INTO USER (name, password_hash) VALUES (?, ?);";
    /**
     * update user query.
     */
    private static final String UPDATE_USER
            = "UPDATE USER SET name = ?, password_hash = ? WHERE id= ?;";

    /**
     * query executor object.
     * @see by.radchuk.task.dao.framework.Executor
     */
    private Executor executor = new Executor();

    public void beginTransaction() throws DaoException{
        try {
            executor.beginTransaction();
        } catch (SQLException exception) {
            throw new DaoException(exception);
        }
    }

    public void rollbackTransaction() throws DaoException{
        try {
            executor.rollbackTransaction();
        } catch (SQLException exception) {
            throw new DaoException(exception);
        }
    }

    public void commitTransaction() throws DaoException{
        try {
            executor.commitTransaction();
        } catch (SQLException exception) {
            throw new DaoException(exception);
        }
    }

    /**
     * find user in the database by the given id.
     * @param id id of the user.
     * @return new <code>User</code> object if found, otherwise null.
     * @throws DaoException if dao operation info occurred.
     */
    public User find(final int id) throws DaoException {
        try {
            return executor.execQuery(FIND_USER_BY_ID, rs -> {
                if (rs.next()) {
                    return User.builder().id(rs.getInt(ID))
                            .name(rs.getString(LOGIN))
                            .password(rs.getString(PASSWORD_HASH))
                            .build();
                }
                return null;
            }, Integer.toString(id));
        } catch (SQLException exception) {
            throw new DaoException(exception);
        }
    }

    /**
     * find user in the database by given name.
     * @param login name of the user.
     * @return new <code>User</code> object if found, otherwise null.
     * @throws DaoException if dao operation info occurred.
     */
    public User find(@NonNull final String login)
            throws DaoException {
        try {
            return executor.execQuery(FIND_USER_BY_LOGIN, rs -> {
                if (rs.next()) {
                    return User.builder().id(rs.getInt(ID))
                            .name(rs.getString(LOGIN))
                            .password(rs.getString(PASSWORD_HASH))
                            .build();
                }
                return null;
            }, login);
        } catch (SQLException exception) {
            throw new DaoException(exception);
        }
    }

    /**
     * saves user in the database.
     * @param user <code>User</code> object.
     * @throws DaoException if dao operation info occurred.
     * @return id of the inserted user.
     */
    public int save(@NonNull final User user) throws DaoException {
        try {
            return executor.execSave(INSERT_USER,
                    user.getName(),
                    user.getPassword());
        } catch (SQLException exception) {
            throw new DaoException(exception);
        }
    }

    /**
     * updates user in the database.
     * @param user <code>User</code> object.
     * @throws DaoException if dao operation info occurred.
     * @return 1 - if the user was updated, 0 otherwise.
     */
    public int update(@NonNull final User user) throws DaoException {
        try {
            return executor.execUpdate(UPDATE_USER,
                    user.getName(),
                    user.getPassword(),
                    Integer.toString(user.getId()));
        } catch (SQLException exception) {
            throw new DaoException(exception);
        }
    }

    /**
     * closes resources of the dao.
     * @throws DaoException if dao operation info occurred.
     */
    @Override
    public void close() throws DaoException {
        try {
            executor.close();
        } catch (SQLException exception) {
            throw new DaoException(exception);
        }
    }
}
