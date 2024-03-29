package com.solvd.navigator.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ConfigConstants {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.CONFIG_CONSTANTS);

    public static final String CONFIG_PROPS_FILE_NAME = FilepathConstants.CONFIG_PROPERTIES;
    public static final String JDBC_URL_KEY = "jdbc.url";
    public static final String JDBC_USERNAME_KEY = "jdbc.username";
    public static final String JDBC_PASSWORD_KEY = "jdbc.password";
    public static final String DATABASE_IMPLEMENTATION = "database.implementation";

    public static final String DAO_CLASS_SUBSTRING = "DAO";
    public static final String JDBC_IMPL_SUFFIX = "JDBCImpl";
    public static final String DAO_PACKAGE = "com.solvd.navigator.dao.";
    public static final String DAO_JDBC_IMPL_SUB_PACKAGE = "jdbc" + StringConstants.FULL_STOP;


    private ConfigConstants() {
        ExceptionUtils.preventConstantsInstantiation();
    }
}
