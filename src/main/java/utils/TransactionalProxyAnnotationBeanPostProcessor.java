package utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor
public class TransactionalProxyAnnotationBeanPostProcessor implements BeanPostProcessor {
    private final JdbcConnectionHolder connectionHolder;

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(final Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        for (Method method : bean.getClass().getMethods()) {
            if (method.isAnnotationPresent(TransactionalProxy.class))
                return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), new TransactionalInvocationHandler(bean));
        }
        return bean;
    }

    @RequiredArgsConstructor
    private class TransactionalInvocationHandler implements InvocationHandler {
        private final Object bean;

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Connection connection;
            try {
                connection = connectionHolder.getConnection();
                connection.setAutoCommit(false);
                method.invoke(bean, args);
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
                connectionHolder.rollBack();
            } finally {
                connectionHolder.closeConnection();
            }
            return bean;
        }
    }

}