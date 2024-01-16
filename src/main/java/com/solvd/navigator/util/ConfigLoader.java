package com.solvd.navigator.util;

import com.solvd.navigator.exception.UnableToLoadConfigPropertiesException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.CONFIG_LOADER);

    public static Properties loadProperties(Class<?> clazz, String pathToResourceFile) {
        Properties properties = new Properties();
        try (InputStream input =
                     clazz.getClassLoader().getResourceAsStream(pathToResourceFile)
        ) {

            if (input == null) {
                final String UNABLE_TO_FIND_FILE_MSG =
                        "Unable to find "
                                + StringFormatters.nestInSingleQuotations(pathToResourceFile);
                throw new IllegalStateException(UNABLE_TO_FIND_FILE_MSG);
            }
            properties.load(input);

        } catch (IOException e) {
            final String UNABLE_TO_LOAD_PROPERTIES_FILE_MSG =
                    "Failed to load properties file "
                            + StringFormatters.nestInSingleQuotations(pathToResourceFile);
            throw new UnableToLoadConfigPropertiesException(UNABLE_TO_LOAD_PROPERTIES_FILE_MSG + e);
        }
        return properties;
    }

    private ConfigLoader() {
        ExceptionUtils.preventUtilityInstantiation();
    }
}
