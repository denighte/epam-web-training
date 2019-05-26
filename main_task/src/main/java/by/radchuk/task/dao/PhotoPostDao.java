package by.radchuk.task.dao;

import by.radchuk.task.dao.framework.Executor;
import by.radchuk.task.model.PhotoPost;
import lombok.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhotoPostDao implements AutoCloseable{
    /**
     * photo post id column name.
     */
    private static final String ID = "id";
    /**
     * photo post description column name.
     */
    private static final String DESCRIPTION = "description";
    /**
     * photo post author column name.
     */
    private static final String USER_ID = "user_id";
    /**
     * photo post creationDate column name.
     */
    private static final String CREATION_DATE = "creation_date";
    /**
     * photo post image link column name.
     */
    private static final String SRC = "src";
    /**
     * find photo post by id query.
     */
    private static final String FIND_POST_BY_ID
            = "SELECT * FROM PHOTO_POST WHERE id = ?;";
    /**
     * delete photo post by id query.
     */
    private static final String DELETE_POST_BY_ID
            = "DELETE FROM PHOTO_POST WHERE id = ?";
    /**
     * save photo post query.
     */
    private static final String SAVE_POST
            = "INSERT INTO PHOTO_POST (description, user_id, creation_date, src)"
            + " VALUES (?, ?, ?, ?)";
    /**
     * Get last 10 posts.
     */
    private static final String GET_BY_MULTIPLE_CONDITIONS
            = "SELECT * FROM PHOTO_POST WHERE USER_ID = COALESCE(?, USER_ID)"
            + " AND CREATION_DATE = COALESCE(?, CREATION_DATE)"
            + " LIMIT ? OFFSET ? ;";
    /**
     * Get posts by creationDate.
     */
    private static final String GET_POSTS_BY_DATE
            = "SELECT * FROM PHOTO_POST WHERE creationDate = ?;";
    /**
     * Get posts by user id.
     */
    private static final String GET_POSTS_BY_USER_ID
            = "SELECT * FROM PHOTO_POST WHERE user_id = ?;";
    /**
     * query executor object.
     * @see by.radchuk.task.dao.framework.Executor
     */
    private Executor executor;

    public PhotoPostDao() throws DaoException {
        try {
            executor = new Executor();
        } catch (SQLException exception) {
            throw new DaoException(exception);
        }
    }

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
     * find photo post by id.
     * @param id photo post id.
     * @return PhotoPost new instance if found, otherwise null.
     * @throws DaoException if dao operation error occurred.
     */
    public PhotoPost find(final int id) throws DaoException {
        try {
            return executor.execQuery(FIND_POST_BY_ID, rs -> {
                if (rs.next()) {
                    return buildPost(rs);
                }
                return null;
            }, Integer.toString(id));
        } catch (SQLException exception) {
            throw new DaoException(exception);
        }
    }

    /**
     * get last n photo posts, n = number matching user_id and date parameters.
     * @param limit number of posts to get.
     * @return PhotoPost collection.
     * @throws DaoException if dao operation error occurred.
     */
    public List<PhotoPost> find(final int skip, final int limit, final Integer user_id, final String date) throws DaoException {
        List<PhotoPost> list = new ArrayList<>();
        try {
            return executor.execQuery(GET_BY_MULTIPLE_CONDITIONS, rs -> {
                while (rs.next()) {
                    PhotoPost post = buildPost(rs);
                    if (post != null) {
                        list.add(post);
                    }
                }
                return list;
            }, user_id, date, limit, skip);
        } catch (SQLException exception) {
            throw new DaoException(exception);
        }
    }

    /**
     * find photo posts by creationDate.
     * @param date creationDate to filter photo posts.
     * @return PhotoPost collection.
     * @throws DaoException if dao operation error occurred.
     */
    public List<PhotoPost> find(final String date) throws DaoException {
        List<PhotoPost> list = new ArrayList<>();
        try {
            return executor.execQuery(GET_POSTS_BY_DATE, rs -> {
                while (rs.next()) {
                    PhotoPost post = buildPost(rs);
                    if (post != null) {
                        list.add(post);
                    }
                }
                return list;
            }, date);
        } catch (SQLException exception) {
            throw new DaoException(exception);
        }
    }

    /**
     * find photo posts by author.
     * @param userId user id to filter.
     * @return PhotoPost collection.
     * @throws DaoException if dao operation info occurred.
     */
    public List<PhotoPost> findByUserId(final int userId) throws DaoException {
        List<PhotoPost> list = new ArrayList<>();
        try {
            return executor.execQuery(GET_POSTS_BY_USER_ID, rs -> {
                while (rs.next()) {
                    PhotoPost post = buildPost(rs);
                    if (post != null) {
                        list.add(post);
                    }
                }
                return list;
            }, userId);
        } catch (SQLException exception) {
            throw new DaoException(exception);
        }
    }

    /**
     * delete photo post with specified id.
     * @param id id of the photo post.
     * @return 1 if success, 0 otherwise.
     * @throws DaoException if dao operation info occurred.
     */
    public int delete(final int id) throws DaoException {
        try {
            return executor.execUpdate(DELETE_POST_BY_ID, Integer.toString(id));
        } catch (SQLException exception) {
            throw new DaoException(exception);
        }
    }

    /**
     * inserts photo post in database.
     * @param post PhotoPost instance.
     * @return id of inserted post.
     * @throws DaoException if dao operation info occurred.
     */
    public int save(@NonNull final PhotoPost post) throws DaoException {
        try {
            return executor.execSave(SAVE_POST,
                    post.getDescription(),
                    post.getUserId(),
                    post.getCreationDate(),
                    post.getSrc());
        } catch (SQLException exception) {
            throw new DaoException(exception);
        }
    }

    private PhotoPost buildPost(ResultSet rs) throws SQLException {
        return PhotoPost.builder().id(rs.getInt(ID))
                .description(rs.getString(DESCRIPTION))
                .userId(rs.getInt(USER_ID))
                .creationDate(rs.getString(CREATION_DATE))
                .src(rs.getString(SRC))
                .build();
    }

    /**
     * closes the executor
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
