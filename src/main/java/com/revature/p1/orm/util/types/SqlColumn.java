package com.revature.p1.orm.util.types;

import com.revature.p1.orm.annotations.types.ColumnType;

public class SqlColumn {
    private String name;
    // TODO: Replace with enum(?)
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

    public void setUnique(boolean valueIsUnique) {
        this.isUnique = valueIsUnique;
    }
}
