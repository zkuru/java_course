package utils;

import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor
public class JdbcConnectionHolder {
    private static final ThreadLocal<Connection> connection = new ThreadLocal<>();
    private final DataSource dataSource;

    public Connection getConnection() {
        try {
            if (connection.get() == null || connection.get().isClosed())
                connection.set(dataSource.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection.get();
    }

    void closeConnection() {
        Connection connection = getConnection();
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void rollBack() {
        Connection connection = getConnection();
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                throw new RuntimeException(e1);
            }
        }
    }
}