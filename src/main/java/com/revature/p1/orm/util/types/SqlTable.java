package com.revature.p1.orm.util.types;

import java.util.List;

/**
 * Contains information required to define a table in SQL.
 */
public class SqlTable {
    private String name;
    private List<SqlColumn> sqlColumns;

    public SqlTable(String name, List<SqlColumn> sqlColumns) {
        this.name = name;
        this.sqlColumns = sqlColumns;
    }

    public SqlTable(){
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SqlColumn> getColumns() {
        return sqlColumns;
    }

    public void setColumns(List<SqlColumn> sqlColumns) {
        this.sqlColumns = sqlColumns;
    }
}
