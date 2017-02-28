package util;

import java.util.Date;

/**
 * Created by jun.
 */
public class Key<T> {
    final String name;
    final Class<T> targetClass;

    Key(String name, Class<T> targetClass) {
        this.name = name;
        this.targetClass = targetClass;
    }

    public static <T> Key<T> key(String name, Class<T> targetClass) {
        return new Key<>(name, targetClass);
    }

    public static Key<Integer> intKey(String name) {
        return key(name, Integer.class);
    }

    public static Key<Double> doubleKey(String name) {
        return key(name, Double.class);
    }

    public static Key<Long> longKey(String name) {
        return key(name, Long.class);
    }

    public static Key<String> stringKey(String name) {
        return key(name, String.class);
    }

    public static Key<Date> dateKey(String name) {
        return key(name, Date.class);
    }

    public static Key<Boolean> booleanKey(String name) {
        return key(name, Boolean.class);
    }

    public String name() {
        return name;
    }

    public Class<T> targetClass() {
        return targetClass;
    }

    @Override
    public String toString() {
        return String.format("(name=%s, class=%s)", name, targetClass.getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Key)) return false;
        Key that = (Key) obj;
        return name.equals(that.name)
                && targetClass.equals(that.targetClass);
    }

    @Override
    public int hashCode() {
        return 31 * name.hashCode() + targetClass.hashCode();
    }
}
