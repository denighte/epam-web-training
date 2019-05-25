package by.radchuk.task.dao.framework;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;
import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.PooledConnection;

/**
 * A simple JDBC fixed size connection pool.
 * It is based on the
 * <a href="http://www.source-code.biz/snippets/java/8.htm">
 *  MiniConnectionPoolManager written by Christian d'Heureuse (Java 1.5)
 * </a>.
 * It has been rewritten and rethought in Java 1.8 style.
 * Synchronization logic has been changed:
 * It almost lock-free, connection pool locks down only
 * if it run out of free <code>PooledConnection</code> objects.
 * It is used as follows:
 * <pre>
 * import java.sql.*;
 * import by.radchuk.handler.dao.framework.FixedConnectionPool;
 * public class Test {
 *     public static void main(String... args) throws Exception {
 *         FixedConnectionPool cp = FixedConnectionPool.create(
 *             "jdbc:h2:~/test", "name", "password");
 *         for (String sql : args) {
 *             Connection conn = cp.getConnection();
 *             conn.createStatement().execute(sql);
 *             conn.close();
 *         }
 *         cp.dispose();
 *     }
 * }
 * </pre>
 *
 * @author Dmitry Radchuk
 */
public class FixedConnectionPool implements DataSource,
                                            ConnectionEventListener {

    /**
     * 'SQL Server does not exist or access is denied' error code.
     */
    private static final int DB_ACCESS_ERROR = 8001;
    /**
     * Default await time for connection.
     */
    private static final int DEFAULT_TIMEOUT = 30;
    /**
     * A factory for <code>PooledConnection</code> objects.
     */
    private final ConnectionPoolDataSource dataSource;
    /**
     * Unused <code>PooledConnection</code> objects holder.
     * Every time pooled connection closes, it added to a queue.
     */
    private final Queue<PooledConnection> freeConnectionQueue
                                          = new ConcurrentLinkedQueue<>();
    /**
     * <code>PooledConnection</code> objects holder.
     * Every time a new pooled connection created,
     * it added to a queue.
     */
    private final Queue<PooledConnection> pooledConnectionQueue
                                          = new ConcurrentLinkedQueue<>();
    /**
     * <code>Semaphore</code> object.
     * Controls the number of connections available.
     */
    private Semaphore semaphore;
    /**
     * <p>Log writer for the <code>DataSource</code> object atomic reference.
     *
     * <p>The log writer is a character output stream to which all logging
     * and tracing messages for this data source will be
     * printed.  This includes messages printed by the methods of this
     * object, messages printed by methods of other objects manufactured
     * by this object, and so on.  Messages printed to a data source-
     * specific log writer are not printed to the log writer associated
     * with the <code>java.sql.DriverManager</code> class. When a
     * <code>DataSource</code> object is created the log writer is
     * initially null; in other words, the default is for logging to be
     * disabled.
     */
    private AtomicReference<PrintWriter> logWriter = new AtomicReference<>();;
    /**
     * Maximum number of connections to use.
     */
    private int poolSize;
    /**
     * The maximum time in seconds to wait for a free connection.
     */
    private AtomicInteger loginTimeout = new AtomicInteger(DEFAULT_TIMEOUT);
    /**
     * number of active (open) connections of this pool. This is the
     * number of <code>Connection</code> objects that have been issued by
     * getConnection() for which <code>Connection.close()</code> has
     * not yet been called.
     */
    private AtomicInteger activeConnections = new AtomicInteger();
    /**
     * mutex for {@link #dispose()} and {@link #disposeNow()} methods.
     */
    private Lock disposeLock = new ReentrantLock();
    /**
     * holds the status of the connection pool:
     * 2 - connection pool was hard disposed (All connections were closed).
     * 1 - connection pool was disposed (All inactive connections were closed).
     * 0 - connection pool is active
     */
    private AtomicInteger disposeStatus = new AtomicInteger(0);

    /**
     * Constructs a new connection pool.
     * @param poolDataSource the data source to create connections
     * @param size size of the connection pool
     */
    protected FixedConnectionPool(final ConnectionPoolDataSource poolDataSource,
                                  final int size) {
        this.dataSource = poolDataSource;
        poolSize = size;
        semaphore = new Semaphore(size);
        if (dataSource != null) {
            try {
                logWriter.set(dataSource.getLogWriter());
            } catch (SQLException e) {
                // ignore
            }
        }
    }

    /**
     * Constructs a new connection pool.
     *
     * @param dataSource the data source to create connections
     * @param size connection pool size
     * @return the connection pool
     */
    public static FixedConnectionPool create(
            final ConnectionPoolDataSource dataSource,
            final int size) {
        return new FixedConnectionPool(dataSource, size);
    }

    /**
     * Gets the maximum number of connections to use.
     *
     * @return the max the maximum number of connections
     */
    public synchronized int getPoolSize() {
        return poolSize;
    }

    /**
     * Gets the maximum time in seconds to wait for a free connection.
     *
     * @return the timeout in seconds
     */
    @Override
    public int getLoginTimeout() {
        return loginTimeout.get();
    }

    /**
     * <p>Retrieves the log writer for this <code>DataSource</code>
     * object.
     *
     * @return the log writer for this data source or null if
     *        logging is disabled
     * @see #setLogWriter
     */
    @Override
    public PrintWriter getLogWriter() {
        return logWriter.get();
    }

    /**
     * <p>Sets the log writer for this <code>DataSource</code>
     * object to the given <code>java.io.PrintWriter</code> object.
     *
     * @param out the new log writer; to disable logging, set to null
     * @see #getLogWriter
     */
    @Override
    public void setLogWriter(final PrintWriter out) {
        logWriter.set(out);
    }

    /**
     * Sets the maximum time in seconds to wait for a free connection.
     * The default timeout is 30 seconds. Calling this method with the
     * value 0 will set the timeout to the default value.
     *
     * @param seconds the timeout, 0 meaning the default
     */
    @Override
    public void setLoginTimeout(final int seconds) {
        if (seconds < 0) {
            throw new IllegalArgumentException("invalid name timeout!");
        }
        if (seconds == 0) {
            this.loginTimeout.set(DEFAULT_TIMEOUT);
        } else {
            this.loginTimeout.set(seconds);
        }
    }

    /**
     * [Not supported].
     */
    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * Closes all unused pooled connections.
     * Exceptions while closing are written to
     * the {@link #logWriter} stream (if set).
     *
     * @return true - if connection pool was successfully disposed,
     *         false - otherwise.
     */
    public boolean dispose() {
        if (disposeStatus.compareAndSet(0, 1)) {
            disposeLock.lock();
            try {
                freeConnectionQueue.forEach(this::closeConnection);
                return true;
            } finally {
                disposeLock.unlock();
            }
        }
        return false;
    }

    /**
     * Closes ALL pooled connections.
     * Exceptions while closing are written to
     * the {@link #logWriter} stream (if set).
     *
     * @return true - if connection pool was successfully disposed,
     *         false - otherwise.
     */
    public boolean disposeNow() {
        if (disposeStatus.compareAndSet(0, 2) || disposeStatus.compareAndSet(1, 2)) {
            disposeLock.lock();
            try {
                pooledConnectionQueue.forEach(this::closeConnection);
                return true;
            } finally {
                disposeLock.unlock();
            }
        }
        return false;
    }

    /**
     * Retrieves a connection from the connection pool. If
     * <code>poolSize</code> connections are already in use, the method
     * waits until a connection becomes available or <code>loginTimeout</code>
     * seconds elapsed. When the application is finished using the connection,
     * it must close it in order to return it to the pool.
     * If no connection becomes available within
     * the given <code>loginTimeout</code>, an exception
     * with SQL state 08001 and vendor code 8001 is thrown.
     *
     * @return a new Connection object.
     * @throws SQLException if a new connection could not be established,
     *      or a loginTimeout occurred
     */
    @Override
    public Connection getConnection() throws SQLException {
        boolean loginSuccess = true;
        try {
            loginSuccess = semaphore.tryAcquire(loginTimeout.get(),
                                                TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
            //ignore ???
        }
        if (loginSuccess) {
            return getConnectionNow();
        }
        throw new SQLException("Login timeout", "08001", DB_ACCESS_ERROR);
    }

    /**
     * [Not supported].
     */
    @Override
    public Connection getConnection(final String user,
                                    final String password) {
        throw new UnsupportedOperationException("Feature not supported: "
                                    + "\"getConnection(String, String)\"");
    }

    /**
     * Get connection from pool logic.
     * @return new <code>Connection</code> object.
     * @throws SQLException if a new connection could not be established.
     */
    private Connection getConnectionNow() throws SQLException {
        if (!disposeStatus.compareAndSet(0, 0)) {
            throw new IllegalStateException("Connection pool was disposed.");
        }
        PooledConnection pc = freeConnectionQueue.poll();
        if (pc == null) {
            pc = dataSource.getPooledConnection();
            pooledConnectionQueue.add(pc);
        }
        Connection connection = pc.getConnection();
        activeConnections.incrementAndGet();
        pc.addConnectionEventListener(this);
        return connection;
    }

    /**
     * This method usually puts the connection back into the pool. There are
     * some exceptions: if the pool is disposed, the connection is disposed as
     * well. If the pool is full, the connection is closed.
     *
     * @param pc the pooled connection
     */
    private void recycleConnection(final PooledConnection pc) {
        if (disposeStatus.compareAndSet(0, 0)
            && activeConnections.decrementAndGet() < poolSize) {
            freeConnectionQueue.add(pc);
        } else if (!disposeStatus.compareAndSet(2, 2)) {
            closeConnection(pc);
        }
        semaphore.release();
    }

    /**
     * Closes <code>PooledConnection</code> object.
     * If <code>SQLException</code> occurred, it is printed
     * to <code>logWriter</code>
     *
     * @param pc <code>PooledConnection</code> object.
     */
    private void closeConnection(final PooledConnection pc) {
        try {
            pc.close();
        } catch (SQLException e) {
            if (logWriter != null) {
                e.printStackTrace(logWriter.get());
            }
        }
    }

    /**
     * INTERNAL
     * Notifies this <code>ConnectionEventListener</code> that
     * the application has called the method <code>close</code> on its
     * representation of a pooled connection.
     *
     * @param event an event object describing the source of
     * the event
     */
    @Override
    public void connectionClosed(final ConnectionEvent event) {
        PooledConnection pc = (PooledConnection) event.getSource();
        pc.removeConnectionEventListener(this);
        recycleConnection(pc);
    }

    /**
     * INTERNAL
     * Notifies this <code>ConnectionEventListener</code> that
     * a fatal error has occurred and the pooled connection can
     * no longer be used.  The driver makes this notification just
     * before it throws the application the <code>SQLException</code>
     * contained in the given <code>ConnectionEvent</code> object.
     *
     * @param event an event object describing the source of
     * the event and containing the <code>SQLException</code> that the
     * driver is about to throw
     */
    @Override
    public void connectionErrorOccurred(final ConnectionEvent event) {
        PooledConnection pc = (PooledConnection) event.getSource();
        pc.removeConnectionEventListener(this);
    }

    /**
     * Returns the number of active (open) connections of this pool. This is the
     * number of <code>Connection</code> objects that have been issued by
     * getConnection() for which <code>Connection.close()</code> has
     * not yet been called.
     *
     * @return the number of active connections.
     */
    public int getActiveConnections() {
        return activeConnections.get();
    }

    /**
     * [Not supported] Return an object of this class if possible.
     * @param iface A Class defining an interface
     *              that the result must implement.
     */
    @Override
    public <T> T unwrap(final Class<T> iface) {
        throw new UnsupportedOperationException("Feature not supported:"
                                                + " \"unwrap\"");
    }

    /**
     * [Not supported] Checks if unwrap can return an object of this class.
     * @param iface a Class defining an interface.
     */
    @Override
    public boolean isWrapperFor(final Class<?> iface) {
        throw new UnsupportedOperationException("Feature not supported:"
                                                + " \"isWrapperFor\"");
    }
}
