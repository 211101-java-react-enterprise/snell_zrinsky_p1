package com.revature.p1.orm.util;

import com.revature.p1.orm.util.accessor.Accessor;
import com.revature.p1.orm.util.accessor.GetAccessor;
import com.revature.p1.orm.util.accessor.SetAccessor;

import java.lang.reflect.*;
import java.lang.reflect.Type;

/** Wraps a Field extracted from an annotated parameter inside a @Table annotated class,
 * storing and providing access to reflected information about the field.
 * @author : Alex Snell
 */
public class FieldInfo {
    private final String fieldName;
    private final Type fieldType;
    private FieldAccessManager<Type> fieldAccessor;

    public FieldInfo(Field field, Class<?> parentClass) throws NoSuchMethodException {
        this.fieldName = field.getName();
        this.fieldType = ((ParameterizedType) field.getType().getGenericSuperclass()).getActualTypeArguments()[0];
        this.fieldAccessor = new FieldAccessManager<>(field, (Class<Type>) this.fieldType);
    }

    private static class FieldAccessManager<T> {

        private final Class<T> fieldType;
        private final Accessor fieldGetter;
        private final Accessor fieldSetter;

        FieldAccessManager(Field field, Class<T> fieldType) throws NoSuchMethodException {
            this.fieldType = fieldType;
            this.fieldGetter = GetAccessor.from(field);
            this.fieldSetter = SetAccessor.from(field);
        }

        public T getFieldFrom(Object classInstance) throws InvocationTargetException, IllegalAccessException {
            return this.fieldType.cast(this.fieldGetter.getAccessor().invoke(classInstance, this.fieldType));
        }

        public <V> void setFieldIn(Object classInstance, V value) throws InvocationTargetException, IllegalAccessException {
            this.fieldSetter.getAccessor().invoke(classInstance, value);
        }
    }

}

