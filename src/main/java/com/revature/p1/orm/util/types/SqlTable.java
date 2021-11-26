package com.revature.p1.orm.util.types;

import java.util.List;
import com.revature.p1.orm.util.types.Column;

/**
 * Contains information required to define a table in SQL.
 */
public class Table {
    private String name;
    private List<Column> columns;

    public Table(String name, List<Column> columns) {
        this.name = name;
        this.columns = columns;
    }

    public Table(){
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }
}
