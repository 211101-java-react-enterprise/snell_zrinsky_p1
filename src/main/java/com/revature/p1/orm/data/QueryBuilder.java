package com.revature.p1.orm.data;

import com.revature.p1.orm.annotations.Column;
import com.revature.p1.orm.annotations.Table;
import com.revature.p1.orm.annotations.types.ColumnType;
import com.revature.p1.orm.util.logging.LogLevel;
import com.revature.p1.orm.util.logging.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// TODO: Refactor to automatically reflect object id.
// QOL: Refactor null returns to Option Type.

public class QueryBuilder<T> {

    private final static Logger logger = Logger.getLogger(QueryBuilder.class);

    private final ConnectionPool pool;
    private final Class<T> reflectedClass;
    private final String insertQuery;
    private final String updateQuery;
    private final String deleteQuery;
    private final String selectQuery;
    private final ArrayList<ColumnSchema> columnSchemas = new ArrayList<>();

    /**
     * @param reflectedClass The class to be reflected
     * @throws IllegalArgumentException Thrown if the class to reflect is null or does not have a table annotation
     */
    public QueryBuilder(Class<T> reflectedClass, ConnectionPool pool) throws IllegalArgumentException {
        if (reflectedClass.getAnnotation(Table.class) == null) {
            throw new IllegalArgumentException("Class must be annotated with @Table");
        }
        Table tableSchema;
        if (reflectedClass != null) {
            this.pool = pool;
            this.reflectedClass = reflectedClass;
            tableSchema = this.reflectedClass.getAnnotation(Table.class);
            this.deleteQuery = "DELETE FROM " + tableSchema.name() + " WHERE id = ?"; // Doesn't need any values - forcing to delete by ID
            this.selectQuery = "SELECT * FROM " + tableSchema.name();
        } else {
            throw new IllegalArgumentException("Reflected class cannot be null");
        }
        StringBuilder insertBuilder = new StringBuilder("INSERT INTO " + tableSchema.name() + " ("); // Needs explicit field names, variable field values
        StringBuilder updateBuilder = new StringBuilder("UPDATE " + tableSchema.name() + " SET "); // Needs explicit field names (different format from INSERT) with field values next to it
        for (Field field : this.reflectedClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                try {
                    ColumnSchema columnSchema = new ColumnSchema(field, field.getDeclaredAnnotation(Column.class));
                    this.columnSchemas.add(columnSchema);

                    // Add column names to the building statement
                    if (this.columnSchemas.size() == 1) {
                        insertBuilder.append(columnSchema.column.name());
                        updateBuilder.append(columnSchema.column.name()).append(" = ?"); // Example: SET program = ?, level = ?, &c&c
                    } else { // The first item shouldn't have a comma before it, the last item shouldn't have a comma after
                        insertBuilder.append(", ").append(columnSchema.column.name());
                        updateBuilder.append(", ").append(columnSchema.column.name()).append(" = ?");
                    }
                    //TODO: LOG THIS LOOOOG
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
        // We have to do a second loop :(
        insertBuilder.append(") VALUES (");
        updateBuilder.append(" WHERE id = ?");
        for (int i = 0; i < columnSchemas.size(); i++) {
            if (i == 0) {
                insertBuilder.append("?");
            } else {
                insertBuilder.append(", ?");
            }
        }
        insertBuilder.append(")");
        this.insertQuery = insertBuilder.toString();
        this.updateQuery = updateBuilder.toString();
        logger.log(LogLevel.DEBUG, "Generated Insert Query: " + this.insertQuery);
        logger.log(LogLevel.DEBUG, "Generated Delete Query: " + this.deleteQuery);
        logger.log(LogLevel.DEBUG, "Generated Update Query: " + this.updateQuery);
        logger.log(LogLevel.DEBUG, "Generated Select Query: " + this.selectQuery);
    }

    /**
     * @param uuid Query to append to the end of SELECT * FROM TableName...
     * @return List of objects instantiated from the query
     */
    public List<T> createSelectQueryFrom(String uuid) {
        try (PreparedStatement statement = this.pool.getConnection().prepareStatement(this.selectQuery)) {
            statement.setString(1, uuid);
            logger.log(LogLevel.DEBUG, "Created select query: " + statement);
            return this.createObjectsFrom(statement.executeQuery());
        } catch (SQLException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException  e) {
            logger.log(LogLevel.ERROR, "Failed to create select query from user query: " + uuid);
        }
        return new ArrayList<>();
    }

    /**
     * @param object Object to be inserted into the database
     * @return Number of rows affected by the query
     */
    public int createInsertQueryFrom(T object) {
        try (PreparedStatement statement = this.pool.getConnection().prepareStatement(this.insertQuery)) {
            logger.log(LogLevel.DEBUG, "Inserting object: " + object.toString());
            return this.createRecordsFrom(statement, object);
        } catch (SQLException e) {
            logger.log(LogLevel.ERROR, "Failed to create insert query from object: " + object);
        }
        return 0;
    }

    /**
     * Implicitly assumes that the object has an id field of type String
     * @param object Object to update in the  database
     * @return The number of rows affected by the query
     */
    public int createUpdateQueryFrom(T object) {
        String id = null;
        try (PreparedStatement statement = this.pool.getConnection().prepareStatement(this.updateQuery)) {
            for (int i = 0; i < this.columnSchemas.size(); i++) {
                ColumnSchema columnSchema = this.columnSchemas.get(i);
                this.setStatementValue(i+ 1, columnSchema, object, statement);
                logger.log(LogLevel.ERROR, "Update query: " + statement.toString());
                if (columnSchema.column.type().equals(ColumnType.ID)) {
                   id = columnSchema.field.get(object).toString();
                }
            }
            // TODO: This is a hack, implement way to get field from ClassSchema by ColumnType
            if (id != null) {
                statement.setString(this.columnSchemas.size() + 1, id);
                logger.log(LogLevel.DEBUG, "Created update query: " + this.updateQuery);
                return this.createRecordsFrom(statement, object);
            } else {
                throw new NullPointerException("No id found for object: " + object);
            }
        } catch (SQLException | IllegalAccessException e) {
            logger.log(LogLevel.ERROR, "Failed to create update query for id: " + id);

        }
        return 0;
    }


    /**
     * Implicitly assumes that the object has an id field of type String
     * @param object The object to delete
     * @return The number of rows affected by the query
     */
    public int createDeleteQueryFrom(T object) {
        String id = null;
        try (PreparedStatement statement = this.pool.getConnection().prepareStatement(this.deleteQuery)) {
            for (ColumnSchema columnSchema : this.columnSchemas) {
                if (columnSchema.column.type().equals(ColumnType.ID)) {
                    id = columnSchema.field.get(object).toString();
                }
            }
            // TODO: This is a hack, implement way to get field from ClassSchema by ColumnType
            if (id != null) {
                statement.setString(1, id);
                logger.log(LogLevel.DEBUG, "Created delete query: " + this.deleteQuery);
                statement.executeUpdate();
            } else {
                throw new NullPointerException( "No id found for object: " + object);
            }

        } catch (SQLException | IllegalAccessException  e) {
            logger.log(LogLevel.ERROR, "Failed to create delete query for id: " + id);
        }
        return 0;
    }

    private int createRecordsFrom(PreparedStatement statement, T object) throws SQLException {
        for (int i = 0; i < this.columnSchemas.size(); i++) {
            ColumnSchema columnSchema = this.columnSchemas.get(i);
            this.setStatementValue(i + 1, columnSchema, object, statement);
        }
        return statement.executeUpdate();
    }

    private ArrayList<T> createObjectsFrom(ResultSet resultSet) throws InstantiationException, IllegalAccessException, SQLException, InvocationTargetException, NoSuchMethodException {
        ArrayList<T> instantiatedObjects = new ArrayList<>();
        while (resultSet.next()) {
            T newObject = this.reflectedClass.newInstance();
            for (ColumnSchema columnSchema : this.columnSchemas) {
                Method setMethod = columnSchema.reflectSetter(this.reflectedClass);
                setMethod.invoke(newObject, resultSet.getObject(columnSchema.column.name()));
            }
            instantiatedObjects.add(newObject);
            logger.log(LogLevel.DEBUG, "Instantiated object: " + newObject);
        }
        return instantiatedObjects;
    }

    private void setStatementValue(int index, ColumnSchema columnSchema, T object, PreparedStatement statement) {
        try {
            switch (columnSchema.column.type()) {
                case STRING:
                    statement.setString(index, (String) columnSchema.field.get(object));
                    break;
                case ID:
                    String id = (String) columnSchema.field.get(object);
                    if (id == null || "".equals(id)) {
                        statement.setString(index, UUID.randomUUID().toString());
                    } else {
                        statement.setString(index, (String) columnSchema.field.get(object));
                    }
                    break;
                case INT:
                    statement.setInt(index, (int) columnSchema.field.get(object));
                    break;
                case DATE:
                    // TODO later
                    break;
                case FLOAT:
                    statement.setFloat(index, (Float) columnSchema.field.get(object));
                    break;
                case DOUBLE:
                    statement.setDouble(index, (Double) columnSchema.field.get(object));
                    break;
                case BOOLEAN:
                    statement.setBoolean(index, (Boolean) columnSchema.field.get(object));
                    break;
            }
            logger.log(LogLevel.DEBUG, "Set statement value: " + columnSchema.field.get(object));
        } catch (IllegalAccessException | SQLException e) {
            logger.log(LogLevel.ERROR, "Failed to set statement value for field: " + columnSchema.field.getName());
        }
    }

    private static class ColumnSchema {

        public Field field;
        public Column column;

        public ColumnSchema(Field field, Column column) {
            this.field = field;
            this.column = column;
        }

        public <T> Method reflectSetter(Class<T> reflectedClass) throws NoSuchMethodException {
            String fieldSetter = "set" + field.getName().substring(0, 1).toUpperCase() + this.field.getName().substring(1);
            return reflectedClass.getMethod(fieldSetter, field.getType());
        }

        public <T> Method reflectGetter(Class<T> reflectedClass) throws NoSuchMethodException {
            String fieldSetter = "get" + field.getName().substring(0, 1).toUpperCase() + this.field.getName().substring(1);
            return reflectedClass.getMethod(fieldSetter, field.getType());
        }
    }
}
