package com.solvd.airport.util;

import com.solvd.airport.exception.UnsuccessfulAuthConnectionRetrievalException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class AuthConnection {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.AUTH_CONNECTION);
    private java.sql.Connection authConnection;


    public AuthConnection() {
        GetAuthConnection();
    }

    private void GetAuthConnection() {
        try {
            Properties properties = ConfigLoader.loadProperties(
                    ClassConstants.AUTH_CONNECTION,
                    ConfigConstants.CONFIG_PROPS_FILE_NAME
            );

            String url = properties.getProperty(ConfigConstants.JDBC_URL_KEY);
            String user = properties.getProperty(ConfigConstants.JDBC_USERNAME_KEY);
            String password = properties.getProperty(ConfigConstants.JDBC_PASSWORD_KEY);

            this.authConnection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            LOGGER.info("SQLException occurred. Unsuccessful attempt to retrieve authorized connection", e);
            throw new UnsuccessfulAuthConnectionRetrievalException("SQLException occurred. Unsuccessful attempt to retrieve authorized connection" + e);
        }
    }

    public java.sql.Connection getConnection() {
        return this.authConnection;
    }

    @Override
    public String toString() {
        Class<?> currClass = ClassConstants.AUTH_CONNECTION;
        String[] fieldNames = {};

        String fieldsString =
                StringFormatters.buildFieldsString(this, fieldNames);

        return StringFormatters.buildToString(currClass, fieldNames, fieldsString);
    }
}
