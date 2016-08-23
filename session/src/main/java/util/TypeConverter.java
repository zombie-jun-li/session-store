package util;

/**
 * Created by li on 2016/8/3.
 */
public abstract class TypeConverter {

    public static <T> T fromString(String textValue, Class<T> targetClass) {
        if (String.class.equals(targetClass)) return (T) textValue;
        if (!StringUtils.hasText(textValue)) return null;
        if (Boolean.class.equals(targetClass)) return (T) Boolean.valueOf(textValue);
        if (Character.class.equals(targetClass)) return (T) Character.valueOf(textValue.charAt(0));
        if (Enum.class.isAssignableFrom(targetClass)) return (T) Enum.valueOf((Class<Enum>) targetClass, textValue);

        return JSON.fromJson(textValue, targetClass);
    }
}
