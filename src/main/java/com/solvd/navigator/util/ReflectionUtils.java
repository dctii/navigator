package com.solvd.navigator.util;

import com.solvd.navigator.exception.UnableToCreateWithReflectionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class ReflectionUtils {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.REFLECTION_UTILS);

    public static <T> T createObject(Class<T> clazz) {
        try {

            Constructor<T> constructor = clazz.getDeclaredConstructor();
            return constructor.newInstance();

        } catch (ReflectiveOperationException e) {
            final String CREATE_WITH_REFLECTION_EXCEPTION_MESSAGE_PREFIX =
                    "Unable to create a new Object instantiation via reflect: ";

            LOGGER.warn(CREATE_WITH_REFLECTION_EXCEPTION_MESSAGE_PREFIX + e);
            throw new UnableToCreateWithReflectionException(CREATE_WITH_REFLECTION_EXCEPTION_MESSAGE_PREFIX + e);
        }
    }

    public static Object getField(Object obj, String fieldName) {
        Class<?> current = obj.getClass();

        while (current != null) {
            try {
                Field field = current.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(obj);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            } catch (IllegalAccessException e) {
                return null;
            }
        }
        return null;
    }

    private ReflectionUtils() {
        ExceptionUtils.preventUtilityInstantiation();
    }
}
