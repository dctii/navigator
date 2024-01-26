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

    public static final String CONFIG_PROPERTIES = "config.properties";
    public static final String DATA_DIR_IN_RESOURCES_FILEPATH = "data/";

    /*
        *.json file paths
     */
    public static final String DRIVERS_JSON = DATA_DIR_IN_RESOURCES_FILEPATH + "drivers.json";
    public static final String EMPLOYEES_JSON = DATA_DIR_IN_RESOURCES_FILEPATH + "employees.json";
    public static final String ITEMS_JSON = DATA_DIR_IN_RESOURCES_FILEPATH + "items.json";
    public static final String LOCATIONS_JSON = DATA_DIR_IN_RESOURCES_FILEPATH + "locations.json";
    public static final String ORDER_ITEMS_JSON = DATA_DIR_IN_RESOURCES_FILEPATH + "orderItems.json";
    public static final String ORDER_RECIPIENTS_JSON = DATA_DIR_IN_RESOURCES_FILEPATH + "orderRecipients.json";
    public static final String ORDERS_JSON = DATA_DIR_IN_RESOURCES_FILEPATH + "orders.json";
    public static final String PERSONS_JSON = DATA_DIR_IN_RESOURCES_FILEPATH + "persons.json";
    public static final String STORAGES_JSON = DATA_DIR_IN_RESOURCES_FILEPATH + "storages.json";
    public static final String VEHICLES_JSON = DATA_DIR_IN_RESOURCES_FILEPATH + "vehicles.json";


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
