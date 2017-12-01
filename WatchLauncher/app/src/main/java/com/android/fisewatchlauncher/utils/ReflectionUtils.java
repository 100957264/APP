package com.android.fisewatchlauncher.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtils {
    public static int getResourceId(String variableName, Class<?> c) {
        try {
            Field idField = c.getField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            // no field found
            return -1;
        }
    }
    public static Constructor<?> getClassConstructor(String packageName, Class<?>... parameterTypes)
            throws NoSuchMethodException, ClassNotFoundException {
        return Class.forName(packageName).getConstructor(parameterTypes);
    }

    public static Field getClassField(String packageName, String fieldName)
            throws NoSuchFieldException, ClassNotFoundException {
        return getClass(packageName).getField(fieldName);
    }

    public static Method getClassMethod(String packageName, String methodName, Class<?>... parameterTypes)
            throws NoSuchMethodException, ClassNotFoundException {
        return Class.forName(packageName).getMethod(methodName, parameterTypes);
    }

    public static Class<?> getClass(String packageName) throws ClassNotFoundException {
        return Class.forName(packageName);
    }

}
