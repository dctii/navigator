package com.solvd.navigator.util;

import com.solvd.navigator.exception.GetDaoFailureException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public final class DataAccessProvider {

    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.DATA_ACCESS_PROVIDER);
    private static final String databaseImpl;


    static {
        Properties config = ConfigLoader.loadProperties(
                ClassConstants.DATA_ACCESS_PROVIDER,
                ConfigConstants.CONFIG_PROPS_FILE_NAME
        );
        databaseImpl = config.getProperty(ConfigConstants.DATABASE_IMPLEMENTATION);
    }

    public static <T> T getDAO(Class<T> daoInterface) {
        String implClassName = getImplClassName(daoInterface);

        try {
            Class<?> implClass = Class.forName(implClassName);
            return daoInterface.cast(implClass.getDeclaredConstructor().newInstance());
        } catch (
                ClassNotFoundException
                | InstantiationException
                | IllegalAccessException
                | NoSuchMethodException
                | InvocationTargetException e) {
            throw new GetDaoFailureException("Error creating DAO implementation for " + daoInterface.getName() + e);
        }
    }

    private static <T> String getImplClassName(Class<T> daoInterface) {
        String daoInterfaceSimpleName = daoInterface.getSimpleName();
        if (daoInterfaceSimpleName.contains(ConfigConstants.DAO_CLASS_SUBSTRING)) {
            daoInterfaceSimpleName = daoInterfaceSimpleName
                    .replace(
                            ConfigConstants.DAO_CLASS_SUBSTRING,
                            StringConstants.EMPTY_STRING
                    );
        }

        return ConfigConstants.DATABASE_IMPLEMENTATION_VAL_MYBATIS.equals(databaseImpl)
                ? ConfigConstants.DAO_MYBATIS_IMPL_PACKAGE + daoInterfaceSimpleName + ConfigConstants.MYBATIS_IMPL_SUFFIX
                : ConfigConstants.DAO_JDBC_IMPL_PACKAGE + daoInterfaceSimpleName + ConfigConstants.JDBC_IMPL_SUFFIX;
    }

    private DataAccessProvider() {
        ExceptionUtils.preventConstantsInstantiation();
    }
}
