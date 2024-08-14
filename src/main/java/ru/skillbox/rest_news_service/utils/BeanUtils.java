package ru.skillbox.rest_news_service.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;

@UtilityClass
public class BeanUtils {
    @SneakyThrows
    public void copyNonNullProperties(Object source, Object destination) {
        Class<?> sourceClass = source.getClass();
        Class<?> destinationClass = destination.getClass();

        // Проверяем, что классы совпадают
        if (!sourceClass.equals(destinationClass)) {
            throw new IllegalArgumentException("Source and destination must be of the same class");
        }

        Field[] fields = sourceClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // Делаем поле доступным для рефлексии
            Object value = field.get(source);
            if (value != null) {
                // Устанавливаем значение в поле назначения, если оно не null
                Field destinationField = destinationClass.getDeclaredField(field.getName());
                destinationField.setAccessible(true);
                destinationField.set(destination, value);
            }
        }
    }
}