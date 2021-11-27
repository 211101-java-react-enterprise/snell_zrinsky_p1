package com.revature.p1.orm.util;

import com.revature.p1.orm.annotations.types.ColumnType;
import com.revature.p1.orm.util.types.SqlColumn;

import java.util.List;

/**
 * Contains information required to define a table in SQL.
 * Returns concrete values
 */
public class ClassSchema {
    private String name;
    private List<SqlColumn> sqlColumns;

    public ClassSchema(String name, List<SqlColumn> sqlColumns) {
        this.name = name;
        this.sqlColumns = sqlColumns;
    }

    public ClassSchema(){
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

    // Implement a subclass to handle the columns
    public class Parameter {
        private String name;
        private ColumnType type;
        private String defaultValue;
        private boolean isNullable;
        private boolean isUnique;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ColumnType getType() {
            return type;
        }

        public void setType(ColumnType type) {
            this.type = type;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public boolean isNullable() {
            return isNullable;
        }

        public void setNullable(boolean nullable) {
            isNullable = nullable;
        }

        public boolean isUnique() {
            return isUnique;
        }

        public void setUnique(boolean unique) {
            isUnique = unique;
        }
    }
}
