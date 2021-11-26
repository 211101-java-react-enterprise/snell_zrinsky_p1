package com.revature.p1.orm.util.types;

public class SqlColumn {
    private String name;
    // TODO: Replace with enum(?)
    private String type;
    private String defaultValue;
    private boolean isNullable;
    private boolean valueIsUnique;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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

    public boolean isValueIsUnique() {
        return valueIsUnique;
    }

    public void setValueIsUnique(boolean valueIsUnique) {
        this.valueIsUnique = valueIsUnique;
    }
}
