package com.nicko.anticheat.util;

import lombok.val;

import java.lang.reflect.Field;

public class ReflectionUtil {

    private static Field getField(final Class<?> clazz, final String name, final Class<?> type) {
        try {
            val field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            if (field.getType() != type) {
                throw new IllegalStateException("Invalid action for field '" + name + "' (expected " + type.getName() + ", got " + field.getType().getName() + ")");
            }
            return field;
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to get field '" + name + "'");
        }
    }

    public static <T> T getFieldValue(Class<?> clazz, String fieldName, Class<?> type, Object instance) {
        val field = getField(clazz, fieldName, type);
        field.setAccessible(true);
        try {
            return (T) field.get(instance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to get value of field '" + field.getName() + "'");
        }
    }
}
