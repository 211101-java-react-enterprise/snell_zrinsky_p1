package com.revature.p1.orm;

import com.revature.p1.orm.util.logging.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//TODO: Implement additional constructor which takes custom logger parameters

public class TableService<T> {
    private final Logger logger = Logger.getLogger(Logger.Printer.CONSOLE);
    private final Connection connection;
    private final Class<T> reflectedClass;
    private final QueryBuilder queryBuilder;

    public TableService(Class<T> reflectedClass, Connection connection) {
        this.connection = connection;
        this.reflectedClass = reflectedClass;
        this.queryBuilder = new QueryBuilder(reflectedClass);
    }

    public T createInstanceFromId(String id) throws InstantiationException, IllegalAccessException {
       T newInstance = this.reflectedClass.newInstance();
       try (PreparedStatement statement = this.queryBuilder.createSelectQuery(id, this.connection)) {

       ResultSet resultSet = statement.executeQuery();
       if (rs.next()) {
           for (ColumnSchema cs : this.queryBuilder.getColumnSchemas()) {
       }
       return newInstance;
    } catch (SQLException e) {
           this.logger.log(Logger.Level.ERROR, "Failed to create new instance of " + this.reflectedClass.getName() + " with id " + id);
       }
    }