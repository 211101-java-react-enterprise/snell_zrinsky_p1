package com.revature.p1.orm.util;

import com.revature.p1.orm.annotations.Column;
import com.revature.p1.orm.annotations.Table;
import com.revature.p1.orm.util.types.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

// Create the sqlTable here?

public class AnnotationParserBeta {

    Class<?> object;
    ClassSchema classSchema;
    /**
     * Reads and interprets annotations inside of a Model class
     * Returns an object we use in the creation of SQL queries
     *
     * Takes an Object, returns a SqlTable Object
     * Is aware of annotations in the Object, we use that info to do stuff
     * @param object
     */
//    public AnnotationParser(Object object) {
//        this.object = object.getClass();
//        System.out.println(this.object.getName());
//    }

    public AnnotationParserBeta() {

    }
    public ClassSchema createTable() {

        ClassSchema newClassSchema = new ClassSchema();

        return null;
    }

    //TODO: Refactor to provide directly or be involved in the creation of the TableService
    // as actual end user interface(?)
    /**
     * Returns wrapper around user provided model representing an implementation which can be used to generate
     * SQL queries matching the model's annotations.
     * @return SqlTable;
     */
    public ClassSchema generateSqlTable() {https://github.com/211101-java-react-enterprise/snell_zrinsky_p1/tree/dev
        // Set the internal SqlTable parameter
        /* TODO - fix
        // ClassSchema newClassSchema = new ClassSchema();
        // ArrayList<SqlColumn> newSqlColumns = new ArrayList<SqlColumn>();
        try {
            // Get Table annotation defined over the passed in user object
            Table table = (Table)object.getAnnotation(Table.class);
            // Get Fields defined in the passed in user object
            // Set table name to the name passed into the Table() annotation
            newSqlTable.setName(table.name());
            // Loop over fields to get Column annotations to build Column objects
            for (Field field: this.object.getDeclaredFields()) {
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = (Column)field.getAnnotation(Column.class);
                    SqlColumn newColumn = new SqlColumn();
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
        */

        return null;
    }


}
