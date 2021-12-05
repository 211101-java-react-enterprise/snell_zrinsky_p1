package com.revature.p1.orm.mapped;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Alex Snell
 */
public class MappedProperty<T> {

    private final Class<T> type;
    private final Field field;
    private final Method getter;
    private final Method setter;
    private final List<Annotation> annotations;

    private MappedProperty(Class<T> type, Field field, Method getter, Method setter, List<Annotation> annotations) {
        this.type = type;
        this.field = field;
        this.getter = getter;
        this.setter = setter;
        this.annotations = annotations;
    }

    /**
     * Static factory method for creating new MappedProperty able to reflectively access and construct instances
     * of the mapped property.
     * @param field Reflected interface providing dynamic access to runtime instance of the mapped property.
     */
    @SuppressWarnings("unchecked")
    public static MappedProperty<Type> from(Field field) {
        Class<Type> type = (Class<Type>) reflectType(field);
        Method getter = reflectAccessor("get", field);
        Method setter = reflectAccessor("set", field);
        List<Annotation> annotations = Arrays.asList(field.getDeclaredAnnotations());
        return new MappedProperty<>(
                type,
                field,
                getter,
                setter,
                annotations
        );
    }

    private static Type reflectType(Field field) {
        return ((ParameterizedType) field
                .getType()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    private static Method reflectAccessor(String prefix, Field field)  {
        String methodName = prefix + field
                .getName()
                .substring(0, 1)
                .toUpperCase() + field
                                    .getName()
                                    .substring(1);
        try {
            return field.getDeclaringClass()
                        .getDeclaredMethod(methodName, field.getType());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public T getIn(Object object) throws IllegalAccessException, InvocationTargetException {
        if (object.getClass() == this.getClass()) {
            return (T)this.getter.invoke(object, this.type);
        } else {
            throw new IllegalArgumentException("Object is instance of unmapped class " + object.getClass());
        }
    }

    public <V> void setIn(Object object, V value) throws IllegalAccessException, InvocationTargetException {
        if (object.getClass() == this.getClass()) {
            this.setter.invoke(object, value);
        } else {
            throw new IllegalArgumentException("Object is instance of unmapped class " + object.getClass());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        MappedProperty<?> that = (MappedProperty<?>) o;
        return this.type.equals(that.type) && this.field.equals(that.field) && this.annotations.equals(that.annotations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.field, this.annotations);
    }

    @Override
    public String toString() {
        return "MappedProperty{" +
               "type=" + this.type +
               ", field=" + this.field +
               ", getter=" + this.getter +
               ", setter=" + this.setter +
               ", annotations=" + this.annotations +
               '}';
    }
}
