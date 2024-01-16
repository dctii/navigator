package com.solvd.navigator.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ConfigConstants {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.CONFIG_CONSTANTS);

    public static final String CONFIG_PROPS_FILE_NAME = "config.properties";
    public static final String JDBC_URL_KEY = "jdbc.url";
    public static final String JDBC_USERNAME_KEY = "jdbc.username";
    public static final String JDBC_PASSWORD_KEY = "jdbc.password";
    public static final String DATABASE_IMPLEMENTATION = "database.implementation";
    public static final String DATABASE_IMPLEMENTATION_VAL_MYBATIS = "mybatis";
    public static final String MYBATIS_XML_CONFIG_FILEPATH = FilepathConstants.MYBATIS_XML_CONFIG_FILEPATH;

    public static final String DAO_CLASS_SUBSTRING = "DAO";
    public static final String MYBATIS_IMPL_SUFFIX = "MyBatisImpl";
    public static final String JDBC_IMPL_SUFFIX = "JDBCImpl";
    public static final String DAO_PACKAGE = "com.solvd.navigator.dao.";
    public static final String DAO_JDBC_IMPL_PACKAGE = DAO_PACKAGE + "jdbc" + StringConstants.FULL_STOP;
    public static final String DAO_MYBATIS_IMPL_PACKAGE = DAO_PACKAGE + "mybatis" + StringConstants.FULL_STOP;


    private ConfigConstants() {
        ExceptionUtils.preventConstantsInstantiation();
    }
}
