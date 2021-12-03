package com.revature.p1.orm.util.accessor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Alex Snell
 */
public class SetAccessor extends Accessor {

    protected SetAccessor(String fieldName, Method accessor) {
        super(fieldName, accessor);
    }

    /**
     * Creates a new SetAccessor for the given field.
     * @param field Field object reflected from a target class field the accessor will be built for.
     * @return Accessor Generated interface for accessing a typically defined setter method named set<FieldName> from target class.
     * @throws NoSuchMethodException Thrown if the field does not have a setter method defined with the format set<FieldName>
     */
    public static Accessor from(Field field) throws NoSuchMethodException {
        String fieldName = field.getName();
        String methodName = "set" + fieldName.substring(0, 1).toUpperCase()
                            + fieldName.substring(1);
        Method accessor = field.getDeclaringClass().getDeclaredMethod(methodName, field.getType());
        return new SetAccessor(fieldName, accessor);
    }
}

