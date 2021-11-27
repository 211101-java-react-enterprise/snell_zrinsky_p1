package com.revature.p1.orm.util;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.revature.p1.orm.annotations.Column;
import com.revature.p1.orm.annotations.Table;

/**
 * Contains information required to define a table in SQL.
 * Returns concrete values
 */
public class ClassSchema {

    private static Connection conn;
    private Class<?> reflectedClass;
    private Table tableDefinition;
    private ArrayList<ColumnSchema> columnSchemas;

    public ClassSchema(Class<?> reflectedClass) {
        this.reflectedClass = reflectedClass;
        this.tableDefinition = this.reflectedClass.getDeclaredAnnotation(Table.class);
        for (Field field : this.reflectedClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                try {
                    assert false;
                    this.columnSchemas.add(new ColumnSchema(field, field.getDeclaredAnnotation(Column.class)));
                    //TODO: LOG THIS LOOOOG
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void setConnection(Connection conn) {
        ClassSchema.conn = conn;
    }

    public void getValuesFromExistingInstance(Object object) throws IllegalAccessException {
        //TODO: verify that the object is of the correct type
//        for (Field field : this.fieldDefinitions) {
//            Object fieldValue = field.get(object);
//        }
    }

    public Object getNewInstanceFromIdInDatabase(Object obj, String uuid) {
        try {
            String sql = String.format("select * from %s where id = %s", this.tableDefinition.name(), uuid);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                for (ColumnSchema cs : this.columnSchemas) {
                    //QUESTION: Why tho this smell doe?
                    cs.field.set(obj, rs.getObject(cs.column.name()));
                }
            }
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private class ColumnSchema {

        private Field field;
        private Column column;

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