package com.revature.p1.orm.util;

import com.revature.p1.orm.util.types.Table;
import com.revature.p1.orm.util.types.Column;

// Create the table here?

public class AnnotationParser {

    Object object;
    Table table;
    /**
     * Reads and interprets annotations inside of a Model class
     * Returns an object we use in the creation of SQL queries
     *
     * Takes an Object, returns a Table Object
     * Is aware of annotations in the Object, we use that info to do stuff
     * @param object
     */
    public AnnotationParser(Object object) {
        this.object = object;
    }

    public Table createTable() {

        Table newTable = new Table();

        return null;
    }

    private Table generateTable() {
        return null;
    }


}
