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
import com.revature.p1.orm.util.logging.Logger;

/**
 * Contains information required to define a table in SQL.
 * Returns concrete values
 */
public class ClassSchema<T> {

    private final Logger logger = Logger.getLogger(Logger.Printer.CONSOLE);
    private static Connection conn;
    private Class<T> reflectedClass;
    private Table tableDefinition;
    private ArrayList<ColumnSchema> columnSchemas = new ArrayList<ColumnSchema>();
    private String insertQuery;
    private String updateQuery;
    private String deleteQuery;
    private String selectQuery;

    public ClassSchema(Class<T> reflectedClass) throws NoSuchMethodException {
        this.reflectedClass = reflectedClass; // Input on the app end as SomeClass.class, where SomeClass is an annotated Model
        this.tableDefinition = this.reflectedClass.getDeclaredAnnotation(Table.class); // Discovers the Table annotation in the app's annotated Model class. There should only be one, declared above the class declaration
        // this.constructor = this.reflectedClass.getConstructor();
        // The beginning of ourSQL statements
        StringBuilder insertBuilderFormer = new StringBuilder("INSERT INTO " + this.tableDefinition.name() + " ("); // Statement requires field names and values in two distinct groups
        StringBuilder insertBuilderLatter = new StringBuilder(") VALUES (");
        StringBuilder updateBuilder = new StringBuilder("UPDATE " + this.tableDefinition.name() + " SET "); // Statement requires field names and values to collate
        StringBuilder deleteBuilder = new StringBuilder("DELETE FROM " + this.tableDefinition.name() + " WHERE id = ?"); // Statement doesn't require any data from fields

        // Handle proper insertion of data into the INSERT and UPDATE commands
        for (Field field : this.reflectedClass.getDeclaredFields()) { // Iterate through each property of the input class
            if (field.isAnnotationPresent(Column.class)) { // Discovers the Column annotations in the app's annotated Model class. There should be one above each property that describes a field/column inside the declared @Table.
                try {
                    ColumnSchema cs = new ColumnSchema(field, field.getDeclaredAnnotation(Column.class));  // ColumnSchema as a container for data. Defined at the bottom of this file.
                    this.columnSchemas.add(cs); // Captures the information attached to the @Column annotation and saves it for later use

                    // Add the required field data to the SQL statement
                    if (this.columnSchemas.size() == 1) { // We decided to prepend each additional field with a comma. Thus, the first must be added differently from the rest.
                        insertBuilderFormer.append(cs.column.name());
                        insertBuilderLatter.append("?");
                        updateBuilder.append(cs.column.name() + " = ?");
                    } else {
                        insertBuilderFormer.append(", " + cs.column.name()); // As: (firstField, secondfField, &c&c)
                        insertBuilderFormer.append(", ?");
                        updateBuilder.append(", " + cs.column.name() + " = ?"); // As: firstField = ?, secondField = ?, &c&c
                    }
                    //TODO: LOG THIS LOOOOG
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }

        // The end of the SQL statements
        insertBuilderLatter.append(")");
        updateBuilder.append(" WHERE id = ?"); // QOL: Free the users from the ORM Dev's tyranny! Allow them to update by more than just a matching ID field.

        // Finalize the built queries and log them for our sake
        this.insertQuery = insertBuilderFormer.toString();
        this.updateQuery = updateBuilder.toString();
        this.deleteQuery = deleteBuilder.toString();
        this.selectQuery = "SELECT * FROM  " + this.tableDefinition.name(); // Oh yeah, SELECT is here too

        this.logger.log(Logger.Level.DEBUG, "Generated Insert Query: " + this.insertQuery);
        this.logger.log(Logger.Level.DEBUG, "Generated Delete Query: " + this.deleteQuery);
        this.logger.log(Logger.Level.DEBUG, "Generated Update Query: " + this.updateQuery);
        this.logger.log(Logger.Level.DEBUG, "Generated Select Query: " + this.selectQuery);

    }

    public static void setConnection(Connection conn) {
        ClassSchema.conn = conn; //?? Enforces one connection per Schema ??//
    }

    /**
     * Allows users the freedom to select records the table as they please.
     * @param querySuffix
     * @return A list of generically typed objects
     * @throws SQLException
     */
    public List<T> createQuery(String querySuffix) throws SQLException {
        String queryPrefix = "SELECT * FROM " + this.tableDefinition.name();
        String query = queryPrefix + " " + querySuffix;
        List<T> result = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
               T obj = this.reflectedClass.newInstance();
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

    /**
     * Creates a new database record based on an instantiated object
     * @param obj An instance of this schema's class, with all non-nullable fields populated
     */
    public void insertNewDatabaseRecord(T obj) {
        try  ( // Use the statement prepared in the constructor and populate each ? with the appropriate value
                PreparedStatement statement = conn.prepareStatement(this.insertQuery)
                ){
            for (int i = 1; i <= columnSchemas.size(); i++) { // SQL strings have an arbitrary number of fields
                ColumnSchema cs = this.columnSchemas.get(i - 1);

                switch (cs.column.type()) { // PreparedStatement uses different methods for each data type
                    case STRING:
                        statement.setString(i, (String)cs.field.get(obj));
                        break;
                    case ID:
                        String id = (String)cs.field.get(obj);
                        if (id == null || id.equals("")) {
                           statement.setString(i, UUID.randomUUID().toString()); // If the user didn't fill in the UUID field of obj, create a new one here.
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

            statement.executeUpdate(); // Run the now-prepared statement

        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update a record from the table.
     * We do not currently give them the UUID from the database, so we need to input it as a parameter for now
     * @param obj
     * @param uuid
     */
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

    /**
     * Find the record, delete the record.
     * @param id
     */
    public void deleteRecordById(String id) {
        try {
            PreparedStatement statement = conn.prepareStatement(this.deleteQuery);
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Holds nodes to join the Class's fields to the annotation data
     */
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