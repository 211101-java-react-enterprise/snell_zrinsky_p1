package com.revature.p1.orm.util;

import com.revature.p1.orm.annotations.Column;
import com.revature.p1.orm.annotations.Table;

import java.lang.reflect.Field;

public class AnnotationParser {
/**
    public static ClassSchema parse(Class<?> reflectedClass) {// Object that contains our table definition
        // ClassSchema newClassSchema = new ClassSchema();
        // ArrayList<SqlColumn> newSqlColumns = new ArrayList<SqlColumn>();
        try {
            // Get Table annotation defined over the passed in user object
            Table table = (Table)reflectedClass.getAnnotation(Table.class);
            // Get Fields defined in the passed in user object
            // Set table name to the name passed into the Table() annotation
            // Loop over fields to get Column annotations to build Column objects
            for (Field field: this.object.getDeclaredFields()) {
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = (Column)field.getAnnotation(Column.class);
                     newColumn = new SqlColumn();
                    newColumn.setName(column.name());
                    newColumn.setUnique(column.isUnique());
                    newColumn.setNullable(column.isNullable());
                    newColumn.setType(column.type());
                    newSqlColumns.add(newColumn);
                }
            }
            newSqlTable.setColumns(newSqlColumns);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Table table = (Table)reflectedClass.getAnnotation(Table.class);
        ClassSchema classSchema = new ClassSchema();
        classSchema.setName(table.name());





        return null;
    }
 */
}
