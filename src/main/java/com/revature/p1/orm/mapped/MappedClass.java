package com.revature.p1.orm.mapped;

import com.revature.p1.orm.annotations.Column;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class book {
    @Column(name = "id")
}

public class MappedClass<T> {

    private final Class<T> type;
    private final List<MappedProperty<Type>> properties;

    private MappedClass(Class<T> type, List<MappedProperty<Type>> properties) {
        this.type = type;
        this.properties = properties;
    }

    /** Using static method factory pattern for clazz instantiation
    *  - to separate the responsibilities of parameter validation and configuration from object instantiation
    *  - provide users with a declarative interface extensible interface for object configuration
    *  - configuration and branching logic directly benefits from the wider scope of the static context
    *      - as the configuration of anyone new object can be based on the state of previously instantiated objects
    *      - i.e. caching
    */
    public static MappedClass<Class<Type>> from(Class<?> clazz) throws NoSuchMethodException  {
        Class<Type> type = (Class<Type>)reflectType(clazz);
        List<MappedProperty<Type>> mappedProperties = Arrays.stream(clazz.getDeclaredFields())
                                                            .map(MappedProperty::from)
                                                            .collect(Collectors.toList());
        return new MappedClass<Object>(
                type,
                mappedProperties
                );
    }

    private static Type reflectType(Class<?> type) {
        return ((ParameterizedType) type.getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        MappedClass<?> that = (MappedClass<?>) o;
        return this.type.equals(that.type) && this.properties.equals(that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.properties);
    }

    @Override
    public String toString() {
        return "MappedClass{" +
               "type=" + this.type +
               ", properties=" + this.properties +
               '}';
    }
}
