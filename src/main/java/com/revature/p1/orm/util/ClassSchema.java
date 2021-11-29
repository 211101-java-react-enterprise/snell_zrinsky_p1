package com.revature.p1.orm.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.revature.p1.orm.annotations.Column;
import com.revature.p1.orm.annotations.Table;

/**
 * Contains information required to define a table in SQL.
 * Returns concrete values
 */
public class ClassSchema<T> {

    private static Connection conn;
    private Class<T> reflectedClass;
    private Table tableDefinition;
    private ArrayList<ColumnSchema> columnSchemas = new ArrayList<ColumnSchema>();
    private String insertQuery;
    private String updateQuery;
    private String deleteQuery;

    public ClassSchema(Class<T> reflectedClass) throws NoSuchMethodException {
        this.reflectedClass = reflectedClass;
        this.tableDefinition = this.reflectedClass.getDeclaredAnnotation(Table.class);
        // this.constructor = this.reflectedClass.getConstructor();
        // The beginning of ourSQL statements
        StringBuilder insertBuilder = new StringBuilder("INSERT INTO " + this.tableDefinition.name() + " ("); // Needs explicit field names, variable field values
        StringBuilder updateBuilder = new StringBuilder("UPDATE " + this.tableDefinition.name() + " SET "); // Needs explicit field names (different format from INSERT) with field values next to it
        StringBuilder deleteBuilder = new StringBuilder("DELETE FROM " + this.tableDefinition.name() + " WHERE id = ?"); // Doesn't need any values - forcing to delete by ID

        for (Field field : this.reflectedClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                try {
                    ColumnSchema cs = new ColumnSchema(field, field.getDeclaredAnnotation(Column.class));
                    this.columnSchemas.add(cs);

                    // Add column names to the building statement
                    if (this.columnSchemas.size() == 1) {
                        insertBuilder.append(cs.column.name());
                        updateBuilder.append(cs.column.name() + " = ?"); // Example: SET program = ?, level = ?, &c&c
                    } else { // The first item shouldn't have a comma before it, the last item shouldn't have a comma after
                        insertBuilder.append(", " + cs.column.name());
                        updateBuilder.append(", " + cs.column.name() + " = ?");
                    }
                    //TODO: LOG THIS LOOOOG
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }

        updateBuilder.append(" WHERE id = ?");

        // We have to do a second loop :(
        insertBuilder.append(") VALUES (");
        for (int i =  0; i < columnSchemas.size(); i++) {
            if (i == 0) {
                insertBuilder.append("?");
            } else {
                insertBuilder.append(", ?");
            }
        }
        insertBuilder.append(")");

        this.insertQuery = insertBuilder.toString();
        this.updateQuery = updateBuilder.toString();
        this.deleteQuery = deleteBuilder.toString();

        System.out.println("~~~~~~~~ FLAG - ClassSchema L.60 ~~~~~~~~\n" + this.insertQuery);
        System.out.println(this.updateQuery);
        System.out.println(this.deleteQuery);

    }

    public static void setConnection(Connection conn) {
        ClassSchema.conn = conn;
    }

    public List<T> createQuery(String querySuffix) throws SQLException {
        String queryPrefix = "SELECT * FROM " + this.tableDefinition.name();
        String query = queryPrefix + " " + querySuffix;
        List<T> result = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
               Object obj = this.reflectedClass.newInstance();
                for (ColumnSchema cs : this.columnSchemas) {
                    //QUESTION: Why tho this smell doe?
                    cs.field.set(obj, rs.getObject(cs.column.name()));
                    result.add(obj);
                }
            }
            return result;
        } catch (SQLException | IllegalAccessException  | InstantiationException e) {
            e.printStackTrace();
        }
        return result;
    }

    // TODO - Make this standard with our other statements
    public T getNewInstanceFromIdInDatabase(String uuid) throws InstantiationException, IllegalAccessException {
        T obj = this.reflectedClass.newInstance();
        try (PreparedStatement statement = conn.prepareStatement(String.format("select * from %s where id = ?", this.tableDefinition.name())))
        {
            statement.setString(1, uuid);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                for (ColumnSchema cs : this.columnSchemas) {
                    //QUESTION: Why tho this smell doe?

                    String fieldSetter = "set" + cs.field.getName().substring(0, 0).toUpperCase() + cs.field.getName().substring(1);
                    Method setter = this.reflectedClass.getMethod(fieldSetter, cs.field.getType());
                    setter.invoke(obj, rs.getObject(cs.column.name()));
//                    cs.field.set(obj, rs.getObject(cs.column.name()));
                }
            } else {
                System.out.println("~~~~~~~~ NO RESULTS RETURNED ~~~~~~~~");
            }
        } catch (SQLException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public void insertNewDatabaseRecord(T obj) { // Pass in an instance of the appropriate Model class
        // Taking the string built during the ClassSchema constructor
        try  (
                PreparedStatement statement = conn.prepareStatement(this.insertQuery)
                ){
            for (int i = 1; i <= columnSchemas.size(); i++) {
                ColumnSchema cs = this.columnSchemas.get(i - 1);
                // Iterate through the obj, get data type passed in through Column annotation

                switch (cs.column.type()) {
                    case STRING:
                        statement.setString(i, (String)cs.field.get(obj));
                        break;
                    case ID:
                        String id = (String)cs.field.get(obj);
                        if (id == null || id.equals("")) {
                           statement.setString(i, UUID.randomUUID().toString());
                        } else {
                            statement.setString(i, (String) cs.field.get(obj));
                        }
                        break;
                    case INT:
                        statement.setInt(i, (Integer)cs.field.get(obj));
                        break;
                    case DATE:
                        // TODO later
                        break;
                    case FLOAT:
                        statement.setFloat(i, (Float)cs.field.get(obj));
                        break;
                    case DOUBLE:
                        statement.setDouble(i, (Double) cs.field.get(obj));
                        break;
                    case BOOLEAN:
                        statement.setBoolean(i, (Boolean)cs.field.get(obj));
                        break;
                } 
            }
            statement.executeUpdate();
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void updateRecord(T obj, String uuid) {
        try {
            PreparedStatement statement = conn.prepareStatement(this.updateQuery);
            for (int i = 1; i <= columnSchemas.size(); i++) {
                ColumnSchema cs = this.columnSchemas.get(i - 1);

                switch (cs.column.type()) {
                    case STRING:
                        statement.setString(i, (String)cs.field.get(obj));
                        break;
                    case ID:
                        statement.setString(i, (String)cs.field.get(obj));
                        break;
                    case INT:
                        statement.setInt(i, (Integer)cs.field.get(obj));
                        break;
                    case DATE:
                        // TODO later
                        break;
                    case FLOAT:
                        statement.setFloat(i, (Float)cs.field.get(obj));
                        break;
                    case DOUBLE:
                        statement.setDouble(i, (Double) cs.field.get(obj));
                        break;
                    case BOOLEAN:
                        statement.setBoolean(i, (Boolean)cs.field.get(obj));
                        break;
                }
            }
            statement.setString(this.columnSchemas.size() + 1, uuid);
            statement.executeUpdate();

        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void deleteRecordById(String id) {
        try {
            PreparedStatement statement = conn.prepareStatement(this.deleteQuery);
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static class ColumnSchema {

        public Field field;
        public Column column;

        public ColumnSchema(Field field, Column column) {
            this.field = field;
            this.column = column;
        }

        public Field getField() {
            return field;
        }

        public void setField(Field field) {
            this.field = field;
        }

        public Column getColumn() {
            return column;
        }

        public void setColumn(Column column) {
            this.column = column;
        }

    }
}