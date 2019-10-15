package utils;

import org.springframework.beans.factory.FactoryBean;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class JndiDataSourceFactoryBean implements FactoryBean<DataSource> {
    @Override
    public DataSource getObject() throws Exception {
        Context initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup("java:/comp/env");
        return (DataSource) envContext.lookup("db/h2");
    }

    @Override
    public Class<?> getObjectType() {
        return DataSource.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}