package com.solvd.navigator.util;

import com.solvd.navigator.exception.GetDaoFailureException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public final class DAOFactory {

    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.DAO_FACTORY);
    private static final String databaseImpl;


    static {
        Properties config = ConfigLoader.loadProperties(
                ClassConstants.DAO_FACTORY,
                ConfigConstants.CONFIG_PROPS_FILE_NAME
        );
        databaseImpl = config.getProperty(ConfigConstants.DATABASE_IMPLEMENTATION);
    }

    public static <T> T createDAO(
            Class<T> daoClass
    ) {
        return createDAO(daoClass, ConfigConstants.JDBC_IMPL_SUFFIX);
    }

    public static <T> T createDAO(
            Class<T> daoClass,
            String implementationSuffix
    ) {
        String daoInterfaceSimpleName = daoClass.getSimpleName();
        String implClassName = getImplClassName(daoClass, implementationSuffix);

        try {
            Class<?> implClass = Class.forName(implClassName);
            return daoClass.cast(implClass.getDeclaredConstructor().newInstance());
        } catch (ClassNotFoundException e) {
            LOGGER.error("Class not found: " + implClassName);
            throw new GetDaoFailureException("Class not found for " + daoClass.getName());
        } catch (InstantiationException e) {
            LOGGER.error("Error instantiating class: " + implClassName);
            throw new GetDaoFailureException("Error instantiating DAO implementation for " + daoClass.getName());
        } catch (IllegalAccessException e) {
            LOGGER.error("Illegal access: " + implClassName);
            throw new GetDaoFailureException("Illegal access during DAO creation for " + daoClass.getName());
        } catch (NoSuchMethodException e) {
            LOGGER.error("No such method in class: " + implClassName);
            throw new GetDaoFailureException("No such constructor for " + daoClass.getName());
        } catch (InvocationTargetException e) {
            LOGGER.error("Invocation target exception in " + implClassName);
            throw new GetDaoFailureException("Error constructing DAO implementation for " + daoClass.getName());
        }
    }

    private static <T> String getImplClassName(Class<T> daoInterface, String implementationSuffix) {
        String daoInterfaceSimpleName = daoInterface.getSimpleName();

        if (daoInterfaceSimpleName.contains(ConfigConstants.DAO_CLASS_SUBSTRING)) {
            daoInterfaceSimpleName = daoInterfaceSimpleName.replace(
                    ConfigConstants.DAO_CLASS_SUBSTRING,
                    StringConstants.EMPTY_STRING
            );
        }

        // Ensure the package path here matches where your implementation classes are located
        return ConfigConstants.DAO_PACKAGE + implementationSuffix.toLowerCase().replace("impl", StringConstants.EMPTY_STRING)
                + "." + daoInterfaceSimpleName + implementationSuffix;
    }


    private DAOFactory() {
        ExceptionUtils.preventConstantsInstantiation();
    }
}
