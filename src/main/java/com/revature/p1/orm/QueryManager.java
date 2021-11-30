package com.revature.p1.orm;

import com.revature.p1.orm.data.QueryBuilder;
import com.revature.p1.orm.util.logging.Logger;

import java.sql.Connection;
import java.util.HashMap;

//TODO: Implement additional constructor which takes custom logger parameters

public class QueryManager {
    private final Logger logger = Logger.getLogger(Logger.Printer.CONSOLE);

    private static HashMap<Class<?>, QueryBuilder> builderCache = new HashMap<>();
    private static Connection connection;

    public static <T> QueryBuilder<T> getQueryBuilder(Class<T> reflectedClass){
        if (reflectedClass == null) {
            // TODO - Fancy error message here
            return null;
        }
        if (!builderCache.containsKey(reflectedClass)) {
            QueryBuilder newBuilder = new QueryBuilder<>(reflectedClass, connection);
            builderCache.put(reflectedClass, newBuilder);
        }
        return builderCache.get(reflectedClass);
    }

    public static void setConnection(Connection connection) {
        QueryManager.connection = connection;
    }
}