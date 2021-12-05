package com.revature.p1.orm.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Properties;

public class ConnectionPool {

    private static final int MIN_POOL_SIZE = 8;
    private static final int MAX_POOL_SIZE = 64;
    private final Properties properties;
    private final LinkedList<Connection> pool = new LinkedList<>();

    private ConnectionPool(Properties properties) throws SQLException {
        this.properties = properties;
        for (int i = 0; i < MIN_POOL_SIZE; i++) {
            this.pool.add(this.createConnection());
        }
    }

    public static ConnectionPool from(String propertiesFile) throws IOException, SQLException {
        ClassLoader loader = Thread.currentThread()
                                   .getContextClassLoader();
        Properties properties = new Properties();
        properties.load(loader.getResourceAsStream(propertiesFile));
        return new ConnectionPool(properties);
    }

    public ConnectionLease getConnection() throws SQLException {
        if (this.pool.size() >= MAX_POOL_SIZE) {
            throw new IllegalStateException("Pool is full");
        }
        Connection conn = (this.pool.peekFirst() != null) ? this.pool.pollFirst() : this.createConnection();
        return new ConnectionLease(conn, this);
    }

    public void shutdown() throws SQLException {
        for (Connection conn : this.pool) {
            conn.close();
        }
        this.pool.clear();
    }

    protected void recycleConnection(Connection conn) {
        if (pool.size() < MIN_POOL_SIZE) {
            this.pool.addLast(conn);
        }
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(
                this.properties.getProperty("url"),
                this.properties.getProperty("username"),
                this.properties.getProperty("password")
        );
    }

    public static class ConnectionLease implements AutoCloseable {

        private Connection connection;
        private ConnectionPool pool;
        private boolean isClosed = false;

        protected ConnectionLease(Connection connection, ConnectionPool pool) {
            this.connection = connection;
            this.pool = pool;
        }

        public PreparedStatement prepareStatement(String sql) {
            try {
                return (this.isValid()) ? this.connection.prepareStatement(sql) : null;
            } catch (SQLException e) {
                this.close();
                e.printStackTrace();
                return null;
            }
        }

        public boolean isValid() throws SQLException {
            return !this.isClosed && !this.connection.isClosed();
        }

        @Override
        public void close() {
            this.pool.recycleConnection(this.connection);
            this.connection = null;
            this.pool = null;
            this.isClosed = true;
        }
    }
}
