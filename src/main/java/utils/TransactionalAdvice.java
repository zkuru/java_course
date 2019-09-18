package utils;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;

import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor
public class TransactionalAdvice {
    private final JdbcConnectionHolder connectionHolder;

    public Object addTransactionSupport(ProceedingJoinPoint joinpoint) {
        Connection connection;
        Object result;
        try {
            connection = connectionHolder.getConnection();
            connection.setAutoCommit(false);
            result = joinpoint.proceed();
            connection.commit();
        } catch (SQLException e) {
            connectionHolder.rollBack();
            throw new RuntimeException(e);
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        } finally {
            connectionHolder.closeConnection();
        }
        return result;
    }

}