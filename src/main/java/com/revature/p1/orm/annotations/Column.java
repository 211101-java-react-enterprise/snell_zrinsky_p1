package com.revature.p1.orm.annotations;

import com.revature.p1.orm.annotations.types.ColumnType;

import java.lang.annotation.*;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    String name() default "";
    ColumnType type() default ColumnType.STRING;
    boolean isNullable() default false;
    boolean isUnique() default false;
}

