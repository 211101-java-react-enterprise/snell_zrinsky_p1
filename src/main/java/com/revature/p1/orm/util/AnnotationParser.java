package com.revature.p1.orm.util;

import com.revature.p1.orm.annotations.Column;
import com.revature.p1.orm.annotations.Table;
import com.revature.p1.orm.util.types.*;

import java.lang.reflect.Field;
import java.util.Arrays;

// Create the sqlTable here?

public class AnnotationParser {

    Class<?> object;
    SqlTable sqlTable;
    /**
     * Reads and interprets annotations inside of a Model class
     * Returns an object we use in the creation of SQL queries
     *
     * Takes an Object, returns a SqlTable Object
     * Is aware of annotations in the Object, we use that info to do stuff
     * @param object
     */
    public AnnotationParser(Object object) {
        this.object = object.getClass();
        System.out.println(this.object.getName());
    }

    public SqlTable createTable() {

        SqlTable newSqlTable = new SqlTable();

        return null;
    }

    //TODO: Refactor to provide directly or be involved in the creation of the TableService
    // as actual end user interface(?)
    /**
     * Returns wrapper around user provided model representing an implementation which can be used to generate
     * SQL queries matching the model's annotations.
     * @return SqlTable;
     */
    public SqlTable generateSqlTable() {
        // Set the internal SqlTable parameter
        SqlTable newSqlTable = new SqlTable();
        try {
            // Get Table annotation defined over the passed in user object
            Table table = (Table)object.getAnnotation(Table.class);
            // Get Fields defined in the passed in user object
            Field[] fields = this.object.getDeclaredFields();
            // Set table name to the name passed into the Table() annotation
            newSqlTable.setName(table.name());
            // Loop over fields to get Column annotations to build Column objects
            for (Field field: fields) {
                if(field.isAnnotationPresent(Column.class)) {
                    Column column = (Column) field.getAnnotation(Column.class);
                    System.out.println(column.name());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newSqlTable;
    }


}
