package utils;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor
public class CglibTransactionalDogService implements BeanPostProcessor {
    private final JdbcConnectionHolder connectionHolder;

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @SneakyThrows
    public Object postProcessAfterInitialization(final Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        for (Method method : bean.getClass().getMethods()) {
            if (method.isAnnotationPresent(CGLibTransactional.class)) {
                Enhancer enhancer = new Enhancer();
                enhancer.setSuperclass(beanClass);
                enhancer.setCallback(new TransactionalInvocationHandler());
                // TODO inject dogDao
                return enhancer.create();
            }
        }
        return bean;
    }

    @RequiredArgsConstructor
    private class TransactionalInvocationHandler implements MethodInterceptor {


        @Override
        public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            Connection connection;
            Object result = null;
            if (!method.isAnnotationPresent(JDKTransactional.class))
                return methodProxy.invokeSuper(o, args);
            try {
                connection = connectionHolder.getConnection();
                connection.setAutoCommit(false);
                result = methodProxy.invokeSuper(o, args);
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
                connectionHolder.rollBack();
            } finally {
                connectionHolder.closeConnection();
            }
            return result;
        }
    }
}