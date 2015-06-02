package utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/*
 * Created by demidovskiy-r on 31.05.2015.
 */
public abstract class ConfigLoader {
    public ConfigLoader() {
    }

    public <T, P> void load(T object, P properties) {
        Class clazz = object.getClass();
        Object dest = object;
        if(object instanceof Class) {
            clazz = (Class)object;
            dest = null;
        }

        Iterator i$ = getInheritedFields(clazz).iterator();

        while(i$.hasNext()) {
            Field field = (Field)i$.next();
            ConfigParam annotation = (ConfigParam)field.getAnnotation(ConfigParam.class);
            if(annotation != null) {
                String configValue = this.getConfigValue(properties, annotation.value());
                if(configValue != null) {
                    try {
                        field.setAccessible(true);
                        Object e = this.parseField(field, configValue, annotation.splitBy());
                        field.set(dest, e);
                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }
                }
            }
        }
    }

    public static List<Field> getInheritedFields(Class<?> type) {
        ArrayList fields = new ArrayList();

        for(Class c = type; c != null && c != Object.class; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }

        return fields;
    }

    private Object parseField(Field field, String value, String splitBy) throws Exception {
        Class clazz = field.getType();
        return clazz.isArray() ? this.instantiateArray(clazz, value, splitBy) : this.instantiateClass(clazz, value);
    }

    private Object instantiateClass(Class clazz, String value) throws Exception {
        value = value.trim();
        if(clazz.isPrimitive()) {
            if(Integer.TYPE == clazz) {
                return Integer.valueOf(Integer.parseInt(value));
            } else if(Boolean.TYPE == clazz) {
                return Boolean.valueOf(Boolean.parseBoolean(value));
            } else if(Byte.TYPE == clazz) {
                return Byte.valueOf(Byte.parseByte(value));
            } else if(Character.TYPE == clazz) {
                return Character.valueOf(value.charAt(0));
            } else if(Double.TYPE == clazz) {
                return Double.valueOf(Double.parseDouble(value));
            } else if(Float.TYPE == clazz) {
                return Float.valueOf(Float.parseFloat(value));
            } else if(Long.TYPE == clazz) {
                return Long.valueOf(Long.parseLong(value));
            } else if(Short.TYPE == clazz) {
                return Short.valueOf(Short.parseShort(value));
            } else {
                throw new IllegalStateException();
            }
        } else {
            return clazz.getConstructor(new Class[]{String.class}).newInstance(new Object[]{value});
        }
    }

    private Object instantiateArray(Class clazz, String value, String splitBy) throws Exception {
        String[] values = value.split(splitBy);
        Class componentType = clazz.getComponentType();
        Object arr = Array.newInstance(componentType, values.length);

        for(int i = 0; i < values.length; ++i) {
            Object obj = this.instantiateClass(componentType, values[i]);
            Array.set(arr, i, obj);
        }

        return arr;
    }

    public abstract <T> String getConfigValue(T var1, String var2);
}