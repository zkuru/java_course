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
        if (connection.get() == null) {
            try {
                connection.set(dataSource.getConnection());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection.get();
    }
}