package com.solvd.airport.util;

import com.solvd.airport.exception.GetResourcesPathFailureException;
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

    public static final String DATA_FILEPATH = "data/";
    public static final String OUR_AIRPORTS_CSV_DATA_FILEPATH = DATA_FILEPATH + "ourairports-airports-data.csv";

    // use `gates.xml` with JAXB
    public static final String GATES_XML = DATA_FILEPATH + "gates.xml";

    // use `terminals.xml` and `airlineStaff.xml with StAX
    public static final String TERMINALS_XML = DATA_FILEPATH + "terminals.xml";
    public static final String AIRLINE_STAFF_XML = DATA_FILEPATH + "airlineStaff.xml";

    // use `airlines.json` and `bookings.json` with Jackson
    public static final String AIRLINES_JSON = DATA_FILEPATH + "airlines.json";
    public static final String BOOKINGS_JSON = DATA_FILEPATH + "bookings.json";


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
