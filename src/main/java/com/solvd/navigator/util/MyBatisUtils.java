package com.solvd.navigator.util;

import com.solvd.navigator.exception.SqlSessionFactoryInitFailureException;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

public class MyBatisUtils {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.MYBATIS_UTILS);
    private static final SqlSessionFactory sqlSessionFactory;

    static {
        InputStream inputStream;
        try {
            inputStream = Resources.getResourceAsStream(ConfigConstants.MYBATIS_XML_CONFIG_FILEPATH);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            throw new SqlSessionFactoryInitFailureException("Error initializing SqlSessionFactory: " + e.getMessage());
        }
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    private MyBatisUtils() {
        ExceptionUtils.preventUtilityInstantiation();
    }
}
