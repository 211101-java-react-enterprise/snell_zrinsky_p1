package com.revature.p1.orm.util;

import com.revature.p1.orm.annotations.Column;
import com.revature.p1.orm.annotations.types.ColumnType;
import com.revature.p1.orm.util.logging.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class TableSchema {

    private final Logger logger = Logger.getInstance();

    private final String tableName;
    private final ColumnMapper primaryKey;
    private final ArrayList<ColumnMapper> columnSchemas;

    protected class ColumnMapperBuilder {

            private ColumnMapper columnMapper;

            private String columnName;
            private boolean columnIsNullable;
            private boolean columnIsUnique;
            private boolean columnIsPrimaryKey;

            private Type fieldType;
            private Method fieldGetter;
            private Method fieldSetter;

            public Column(String name, ColumnType type) {
                this.columnMapper = new ColumnMapper();
                this.columnMapper.name = name;
                this.columnMapper.type = type;
            }

            public ColumnM isNullable(boolean isNullable) {
                this.columnMapper.isNullable = isNullable;
                return this;
            }

            public ColumnM fieldGetter(Method fieldGetter) {
                this.columnMapper.fieldGetter = fieldGetter;
                return this;
            }

            public ColumnM fieldSetter(Method fieldSetter) {
                this.columnMapper.fieldSetter = fieldSetter;
                return this;
            }

            public ColumnMapper build() {
                return this.columnMapper;
            }
        }

        protected ColumnMapper(Column column, Field field) {
            this.name = column.name();
            this.type = column.type();
            this.isNullable = column.isNullable();

            String fieldGetterName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
            String fieldSetterName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);

            this.fieldGetter = field.getDeclaringClass().getDeclaredMethod(fieldGetterName);
        }

        private class Builder {
            private String name;
            private ColumnType type;
            private boolean isNullable;

            public Builder setName(String name) {
                this.name = name;
                return this;
            }

            public Builder setType(ColumnType type) {
                this.type = type;
                return this;
            }

            public Builder setIsNullable(boolean isNullable) {
                this.isNullable = isNullable;
                return this;
            }

            public ColumnMapper build(Column column, Field field) {

                return new ColumnMapper(this);
            }
        }


    }
}
