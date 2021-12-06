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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class QueryBuilder<T> {

    private static final Logger logger = Logger.getLogger(QueryBuilder.class);
    private final ConnectionPool connectionPool;

    private final Class<T> mappedClass;
    private final List<MappedProperty> mappedProperties;
    private final String insertQuery;
    private final String updateQuery;
    private final String deleteQuery;
    private final String selectQuery;
    private final String tableName;


    /**
     * @param mappedClass The class to be mapped to a relational database table
     * @throws IllegalArgumentException Thrown if the class to map is null or does not have a table annotation
     */
    public QueryBuilder(Class<T> mappedClass, ConnectionPool connectionPool) throws IllegalArgumentException {
        if (mappedClass.getAnnotation(Table.class) == null) {
            throw new IllegalArgumentException("Class must be annotated with @Table");
        }
        logger.log(LogLevel.DEBUG, "Creating QueryBuilder for class: " + mappedClass.getName());
        this.connectionPool = connectionPool;
        this.mappedClass = mappedClass;
        this.mappedProperties = Arrays.stream(mappedClass.getDeclaredFields())
                .filter(field -> field.getAnnotation(Column.class) != null)
                .map(MappedProperty::of)
                .collect(Collectors.toList());
        logger.log(LogLevel.DEBUG, "Mapped properties: " + this.mappedProperties);
        this.tableName = mappedClass.getAnnotation(Table.class).name();
        this.insertQuery = createInsertTemplate();
        this.deleteQuery = createDeleteTemplate();
        this.selectQuery = createSelectTemplate();
        this.updateQuery = createUpdateTemplate();
    }

    private String createDeleteTemplate() {
        return String.format("DELETE FROM %s WHERE id = ?", this.tableName);
    }

    private String createSelectTemplate() {
        return String.format("SELECT * FROM %s WHERE id = ?", this.tableName);
    }

    private String createUpdateTemplate() {
        StringBuilder updateBuilder = new StringBuilder("UPDATE " + this.tableName + " SET ")
                .append(mappedProperties.get(0).column.name())
                .append(" = ?");
        mappedProperties
                .stream()
                .skip(1)
                .forEach(mappedProperty -> updateBuilder
                        .append(", ")
                        .append(mappedProperty.column.name())
                        .append(" = ?"));
        updateBuilder.append(" WHERE id = ?");
        return updateBuilder.toString();
    }

    private String createInsertTemplate() {
        StringBuilder insertBuilder = new StringBuilder("INSERT INTO ")
                .append(this.tableName)
                .append(" (");
        StringBuilder valuesBuilder = new StringBuilder("VALUES (");
        mappedProperties
                .forEach(mappedProperty -> {
                    insertBuilder.append(mappedProperty.column.name()).append(", ");
                    valuesBuilder.append("?, ");
                });
        insertBuilder.delete(insertBuilder.length() - 2, insertBuilder.length());
        valuesBuilder.delete(valuesBuilder.length() - 2, valuesBuilder.length());
        insertBuilder.append(") ").append(valuesBuilder.append(")"));
        return insertBuilder.toString();
    }

    /**
     * @param uuid Query to append to the end of SELECT * FROM TableName...
     * @return List of objects instantiated from the query
     */
    public List<T> createSelectQueryFrom(String uuid) {
        logger.log(LogLevel.DEBUG, "Starting select query for uuid: " + uuid);
        try (PreparedStatement statement = this.connectionPool.getConnection().prepareStatement(this.selectQuery)) {
            statement.setString(1, uuid);
            logger.log(LogLevel.DEBUG, "Created select query: " + statement);
            return this.createObjectsFrom(statement.executeQuery());
        } catch (SQLException | InstantiationException | IllegalAccessException  e) {
            logger.log(LogLevel.ERROR, e.getMessage());
        }
        return new ArrayList<>();
    }

    /**
     * @param object Object to be inserted into the database
     * @return Number of rows affected by the query
     */
    public int createInsertQueryFrom(T object) {
        try (PreparedStatement statement = this.connectionPool.getConnection().prepareStatement(this.insertQuery)) {
            for (int i = 0; i < this.mappedProperties.size(); i++) {
                MappedProperty mappedProperty = this.mappedProperties.get(i);
                this.setStatementValue(i+ 1, mappedProperty, object, statement);
                logger.log(LogLevel.DEBUG, "Current query: " + statement.toString());
            }
            return statement.executeUpdate();
        } catch (SQLException e) {
            logger.log(LogLevel.ERROR, "Failed to create insert query for object: " + object.toString());
        }
        return 0;
    }

    /**
     * Implicitly assumes that the object has an id field of type String
     * @param object Object to update in the  database
     * @return The number of rows affected by the query
     */
    public int createUpdateQueryFrom(T object) {
        try (PreparedStatement statement = this.connectionPool.getConnection().prepareStatement(this.updateQuery)) {
            for (int i = 0; i < this.mappedProperties.size(); i++) {
                MappedProperty mappedProperty = this.mappedProperties.get(i);
                this.setStatementValue(i+ 1, mappedProperty, object, statement);
                logger.log(LogLevel.DEBUG, "Current query: " + statement.toString());
                if (mappedProperty.column.type().equals(ColumnType.ID)) {
                    this.setStatementValue(mappedProperties.size() + 1, mappedProperty, object, statement);
                }
            }
            return statement.executeUpdate();
        } catch (SQLException e) {
            logger.log(LogLevel.ERROR, "Failed to create update query for object: " + object.toString());
        }
        return 0;
    }


    /**
     * Takes a UUID and deletes the object from the database
     * @param uuid The id of the object to delete
     * @return The number of rows affected by the query
     */
    public int createDeleteQueryFrom(String uuid) {
        logger.log(LogLevel.DEBUG, "Starting delete query for uuid: " + uuid);
        try (PreparedStatement statement = this.connectionPool.getConnection().prepareStatement(this.deleteQuery)) {
            statement.setString(1, uuid);
            return statement.executeUpdate();
        } catch (SQLException e) {
            logger.log(LogLevel.ERROR, "Failed to create delete query for id: " + uuid);
        }
        return 0;
    }

    private int createRecordsFrom(PreparedStatement statement, T object) throws SQLException {
        for (int i = 0; i < this.mappedProperties.size(); i++) {
            MappedProperty mappedProperty = this.mappedProperties.get(i);
            this.setStatementValue(i + 1, mappedProperty, object, statement);
            logger.log(LogLevel.DEBUG, "Created statement: " + statement.toString());
        }
        return statement.executeUpdate();
    }

    private ArrayList<T> createObjectsFrom(ResultSet resultSet) throws InstantiationException, IllegalAccessException, SQLException {
        logger.log(LogLevel.DEBUG, "Starting to create objects from result set");
        ArrayList<T> instantiatedObjects = new ArrayList<>();
        while (resultSet.next()) {
            T newObject = mappedClass.newInstance();
            mappedProperties.forEach(mappedProperty -> {
                try {
                    mappedProperty.invokeSet(newObject, resultSet.getObject(mappedProperty.getName()));
                } catch (InvocationTargetException | IllegalAccessException | SQLException e) {
                    logger.log(LogLevel.ERROR, "Failed to invoke setter for class: " + this.getClass() + " property: " + mappedProperty.getName());
                }
            });
            instantiatedObjects.add(newObject);
            logger.log(LogLevel.DEBUG, "Created object: " + newObject);
        }
        return instantiatedObjects;
    }

    private void setStatementValue(int index, MappedProperty mappedProperty, T object, PreparedStatement statement) {
        try {
            switch (mappedProperty.column.type()) {
                case ID:
                case STRING:
                    statement.setString(index, (String) mappedProperty.field.get(object));
                    break;
                case INT:
                    statement.setInt(index, (int) mappedProperty.field.get(object));
                    break;
                case BOOLEAN:
                    statement.setBoolean(index, (Boolean) mappedProperty.field.get(object));
                    break;
                default:
            }
            logger.log(LogLevel.DEBUG, "Set statement value: " + mappedProperty.field.get(object));
        } catch (IllegalAccessException | SQLException e) {
            logger.log(LogLevel.ERROR, "Failed to statement value for object: " + object + "\n  at index: " + index + "\n for column: " + mappedProperty.column.name() + "\n with type: " + mappedProperty.column.type());
        }
    }

    private static class MappedProperty {

        private final String name;
        private final Class<?> mappedClass;
        private final Field field;
        private final Column column;
        private final ColumnType mappedType;
        private final Method setter;
        private final Method getter;

        private MappedProperty(Field field, Column column, Class<?> mappedClass, String name, Method setter, Method getter) {
            if (field.getDeclaredAnnotation(Column.class) == null) {
                throw new IllegalArgumentException("Properties must be annotated with @Column");
            }
            this.field = field;
            this.column = column;
            this.mappedType = column.type();
            this.mappedClass = mappedClass;
            this.name = name;
            this.setter = setter;
            this.getter = getter;
        }

        public static MappedProperty of(Field field) {
            logger.log(LogLevel.DEBUG, "Creating mapped property for field: " + field.getName());
            String name = field.getName();
            Class<?> mappedClass = field.getDeclaringClass();
            Column column = field.getDeclaredAnnotation(Column.class);
            String methodNameSuffix = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
            Method setter = null;
            Method getter = null;
            try {
                setter = mappedClass.getMethod("set" + methodNameSuffix, field.getType());
                getter = mappedClass.getMethod("get" + methodNameSuffix);
            } catch (NoSuchMethodException e) {
                logger.log(LogLevel.WARN, "Failed to find setter in mapped class: " + mappedClass.getName() + " for field: " + field.getName());
            }
            return new MappedProperty(
                    field,
                    column,
                    mappedClass,
                    name,
                    setter,
                    getter
            );
        }

        public void setValue(Object object, Object value) {
            try {
                this.setter.invoke(object, value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                logger.log(LogLevel.ERROR, "Failed to set value: " + value + " for object: " + object);
            }
        }

        public Field getField() {
            return field;
        }

        public Column getColumn() {
            return column;
        }

        public String getName() {
            return name;
        }

        public Class<?> getMappedClass() {
            return mappedClass;
        }

        public ColumnType getMappedType() {
            return mappedType;
        }

        public Object invokeSet(Object instance, Object value) throws InvocationTargetException, IllegalAccessException {
            return this.setter.invoke(instance, value);
        }

        public <T> Object invokeGet(Object instance) throws InvocationTargetException, IllegalAccessException {
            return this.getter.invoke(instance);
        }

    }
}
