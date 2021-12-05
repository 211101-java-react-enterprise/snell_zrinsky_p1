package com.revature.p1.orm;

import com.revature.p1.orm.data.ConnectionPool;
import com.revature.p1.orm.data.QueryBuilder;
import com.revature.p1.orm.util.logging.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

//TODO: Implement additional constructor which takes custom logger parameters

public class QueryManager {

    private static final Logger logger = Logger.getLogger(QueryManager.class);

    private final ConnectionPool pool;

    private QueryManager(ConnectionPool pool) {
        this.pool = pool;
    }

    public static QueryManager configure(String propertiesFile) throws IOException, SQLException {
        ConnectionPool pool = ConnectionPool.from(propertiesFile);
        return new QueryManager(
                pool
        );
    }

    public QueryBuilder getQueryBuilder(Class<?> annotatedClass){
        if (annotatedClass == null) {
            throw new IllegalArgumentException("Annotated class cannot be null");
        }
        logger.log(LogLevel.DEBUG, "Creating new QueryBuilder for class: " + annotatedClass.getName());
        return new QueryBuilder<>(annotatedClass, this.pool);
    }
}