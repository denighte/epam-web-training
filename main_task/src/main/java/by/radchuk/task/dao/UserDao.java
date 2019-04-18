package by.radchuk.task.dao;

import by.radchuk.task.dao.framework.Executor;
import by.radchuk.task.dao.framework.Queries;
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
     * login column name.
     */
    private static final String LOGIN = "login";
    /**
     * password hash column name.
     */
    private static final String PASSWORD_HASH = "password_hash";
    /**
     * name column name.
     */
    private static final String NAME = "name";
    /**
     * surname column name.
     */
    private static final String SURNAME = "surname";
    /**
     * image link column name.
     */
    private static final String IMAGE_LINK = "image_link";
    /**
     * find user by id query.
     */
    private static final String FIND_USER_BY_ID;
    /**
     * find user by login query.
     */
    private static final String FIND_USER_BY_LOGIN;
    /**
     * insert user query.
     */
    private static final String INSERT_USER;
    /**
     * update user query.
     */
    private static final String UPDATE_USER;
    static {
        Queries queries = Queries.getInstance();
        FIND_USER_BY_ID = queries.getQuery("users:find_by_id");
        FIND_USER_BY_LOGIN = queries.getQuery("users:find_by_login");
        INSERT_USER = queries.getQuery("users:insert");
        UPDATE_USER = queries.getQuery("users:update");
    }

    /**
     * query executor objects.
     * @see by.radchuk.task.dao.framework.Executor
     */
    private Executor executor = new Executor();

    /**
     * find user in the database by the given id.
     * @param id id of the user.
     * @return new <code>User</code> object if found, otherwise null.
     * @throws DaoException if dao operation error occurred.
     */
    public User find(final int id) throws DaoException {
        try {
            return executor.execQuery(FIND_USER_BY_ID, rs -> {
                if (rs.next()) {
                   return User.builder().id(rs.getInt(ID))
                              .login(rs.getString(LOGIN))
                              .passwordHash(rs.getString(PASSWORD_HASH))
                              .name(rs.getString(NAME))
                              .surname(rs.getString(SURNAME))
                              .imageLink(rs.getString(IMAGE_LINK))
                              .build();
                }
                return null;
            }, Integer.toString(id));
        } catch (SQLException exception) {
            throw new DaoException(exception);
        }
    }

    /**
     * find user in the database by given login.
     * @param login login of the user.
     * @return new <code>User</code> object if found, otherwise null.
     * @throws DaoException if dao operation error occurred.
     */
    public User find(@NonNull final String login)
                                    throws DaoException {
        try {
            return executor.execQuery(FIND_USER_BY_LOGIN, rs -> {
                if (rs.next()) {
                    return User.builder().id(rs.getInt(ID))
                            .login(rs.getString(LOGIN))
                            .passwordHash(rs.getString(PASSWORD_HASH))
                            .name(rs.getString(NAME))
                            .surname(rs.getString(SURNAME))
                            .imageLink(rs.getString(IMAGE_LINK))
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
     * @throws DaoException if dao operation error occurred.
     * @return id of the inserted user.
     */
    public int insert(@NonNull final User user) throws DaoException {
        try {
            return executor.execSave(INSERT_USER,
                    user.getLogin(),
                    user.getPasswordHash(),
                    user.getName(),
                    user.getSurname(),
                    user.getImageLink());
        } catch (SQLException exception) {
            throw new DaoException(exception);
        }
    }

    /**
     * updates user in the database.
     * @param user <code>User</code> object.
     * @throws DaoException if dao operation error occurred.
     * @return 1 - if the user was updated, 0 otherwise.
     */
    public int update(@NonNull final User user) throws DaoException {
        try {
            return executor.execUpdate(UPDATE_USER,
                    user.getLogin(),
                    user.getPasswordHash(),
                    user.getName(),
                    user.getSurname(),
                    user.getImageLink(),
                    Integer.toString(user.getId()));
        } catch (SQLException exception) {
            throw new DaoException(exception);
        }
    }

    /**
     * closes resources of the dao.
     * @throws DaoException if dao operation error occurred.
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