package com.solvd.navigator.util;

import com.solvd.navigator.exception.GetResourcesPathFailureException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URISyntaxException;

public final class FilepathConstants {

    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.FILEPATH_CONSTANTS);
    /*
        From `resources/`
    */
    public static final String RESOURCES_ABSOLUTE_PATH = getAbsolutePathOfResource();

    public static final String MYBATIS_XML_CONFIG_FILEPATH = "mybatis-config.xml";

    public static final String DATA_DIR_IN_RESOURCES_FILEPATH = "data/";


    private static String getAbsolutePathOfResource() {
        final String FILE_URI_SCHEME = "file:";
        final String MAVEN_COMPILATION_CLASSES_SUBPATH = "target/classes";
        final String SRC_MAIN_RESOURCES_SUBPATH = "src/main/resources";

        try {
            return ClassConstants.FILEPATH_CONSTANTS
                    .getClassLoader()
                    .getResource(StringConstants.EMPTY_STRING)
                    .toURI()
                    .toString()
                    .replace(FILE_URI_SCHEME, StringConstants.EMPTY_STRING)
                    .replace(MAVEN_COMPILATION_CLASSES_SUBPATH, SRC_MAIN_RESOURCES_SUBPATH);
        } catch (URISyntaxException e) {
            throw new GetResourcesPathFailureException("Failed to get absolute resources path:" + e);
        }
    }

    private FilepathConstants() {
        ExceptionUtils.preventConstantsInstantiation();
    }
}
