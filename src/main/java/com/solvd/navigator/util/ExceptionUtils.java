package com.solvd.navigator.util;

import com.solvd.navigator.exception.EmptyVerticesMapException;
import com.solvd.navigator.exception.InvalidAbscissaException;
import com.solvd.navigator.exception.InvalidCoordinatesException;
import com.solvd.navigator.exception.InvalidDateFormatException;
import com.solvd.navigator.exception.InvalidDecimalException;
import com.solvd.navigator.exception.InvalidEdgeException;
import com.solvd.navigator.exception.InvalidOrdinateException;
import com.solvd.navigator.exception.InvalidStringException;
import com.solvd.navigator.exception.InvalidVertexException;
import com.solvd.navigator.exception.InvalidVertexOptionException;
import com.solvd.navigator.exception.PointTypeIsNullException;
import com.solvd.navigator.exception.StringLengthException;
import com.solvd.navigator.math.graph.GraphConstants;
import com.solvd.navigator.math.graph.Point;
import com.solvd.navigator.math.graph.Vertex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

public class ExceptionUtils {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.EXCEPTION_UTILS);

    public static void preventUtilityInstantiation() {
        final String NO_UTILITY_CLASS_INSTANTIATION_MESSAGE =
                "This is a utility class and instances cannot be made of it.";

        preventClassInstantiation(NO_UTILITY_CLASS_INSTANTIATION_MESSAGE);
    }

    public static void preventFactoryInstantiation() {
        final String NO_FACTORY_CLASS_INSTANTIATION_MESSAGE =
                "This is a factory class and instances cannot be made of it.";

        preventClassInstantiation(NO_FACTORY_CLASS_INSTANTIATION_MESSAGE);
    }

    public static void preventConstantsInstantiation() {
        final String NO_CONSTANTS_INSTANTIATION_MESSAGE =
                "This is a constants class and instances cannot be made of it.";

        preventClassInstantiation(NO_CONSTANTS_INSTANTIATION_MESSAGE);
    }

    public static void preventClassInstantiation(String instantiationNotAllowedMessage) {
        throw new UnsupportedOperationException(instantiationNotAllowedMessage);
    }


    public static void isStringLengthValid(String string, int maxLength) {
        if (string.length() > maxLength) {
            final String stringLengthMessage =
                    "String length is invalid. Cannot be more than " + maxLength + " characters";
            throw new StringLengthException(stringLengthMessage);
        }
    }

    public static void isNullOrStringLengthValid(String string, int maxLength) {
        if (string == null) return;

        if (string.length() > maxLength) {
            final String stringLengthMessage =
                    "String length is invalid. Cannot be more than " + maxLength + " characters";
            throw new StringLengthException(stringLengthMessage);
        }
    }

    public static void areStringLengthsValid(Map<String, Integer> map) {
        map.forEach(ExceptionUtils::isStringLengthValid);
    }

    public static void isDecimalValid(Number number, int precision, int scale) {
        String[] splitter = String.valueOf(number).split("\\.");
        int integerPlaces = splitter[0].length();
        int decimalPlaces = splitter.length > 1 ? splitter[1].length() : 0;

        if (integerPlaces + decimalPlaces > precision || decimalPlaces > scale) {
            final String invalidDecimalExceptionMsg =
                    "Invalid decimal: " + number + " for DECIMAL" + StringFormatters.nestInParentheses(precision + "," + scale);
            throw new InvalidDecimalException(invalidDecimalExceptionMsg);
        }
    }

    public static void areDecimalsValid(Map<Number, int[]> map) {
        map.forEach((number, precisionAndScale) -> {
            int precision = precisionAndScale[0];
            int scale = precisionAndScale[1];
            isDecimalValid(number, precision, scale);
        });
    }

    public static void isValidDateString(String dateString, String datePattern) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
            LocalDate date = LocalDate.parse(dateString, formatter);
        } catch (
                DateTimeParseException e) {
            final String INVALID_DATE_FORMAT_EXCEPTION_MSG =
                    "Invalid date format or invalid date. Please enter a date in YYYY-MM-DD format.";
            LOGGER.error(INVALID_DATE_FORMAT_EXCEPTION_MSG);
            throw new InvalidDateFormatException(INVALID_DATE_FORMAT_EXCEPTION_MSG);
        }
    }

    public static void areValidDates(Map<String, String> dateStringsAndDatePatterns) {
        dateStringsAndDatePatterns.forEach(ExceptionUtils::isValidDateString);
    }

    public static void isValidTimestamp(String timestampString, String timestampPattern) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(timestampPattern);
            formatter.setLenient(false);
            Timestamp timestamp = new Timestamp(formatter.parse(timestampString).getTime());
        } catch (ParseException e) {
            final String INVALID_TIMESTAMP_FORMAT_EXCEPTION_MSG =
                    "Invalid timestamp format or invalid timestamp. Please enter a timestamp in the specified format: YYYY-MM-DD HH:MM:SS.";
            LOGGER.error(INVALID_TIMESTAMP_FORMAT_EXCEPTION_MSG);
            throw new InvalidDateFormatException(INVALID_TIMESTAMP_FORMAT_EXCEPTION_MSG);
        }
    }

    public static void areValidTimestamps(Map<String, String> timestampStringsAndPatterns) {
        timestampStringsAndPatterns.forEach(ExceptionUtils::isValidTimestamp);
    }

    public static void areValidCoordinates(double x, double y) {
        if (
                x < GraphConstants.ABSCISSA_MIN
                        || x > GraphConstants.ABSCISSA_MAX
                        || y < GraphConstants.ORDINATE_MIN
                        || y > GraphConstants.ORDINATE_MAX
        ) {
            final String INVALID_COORDINATES_EXCEPTION_MSG =
                    "Coordinates out of valid range.";
            LOGGER.error(INVALID_COORDINATES_EXCEPTION_MSG);
            throw new InvalidCoordinatesException("Coordinates out of valid range.");
        }
    }

    public static void isValidAbscissa(double x) {
        if (x < GraphConstants.ABSCISSA_MIN) {
            final String INVALID_ABSCISSA_EXCEPTION_MSG =
                    "Abscissa, 'x', out of valid range for Point.";
            LOGGER.error(INVALID_ABSCISSA_EXCEPTION_MSG);
            throw new InvalidAbscissaException(INVALID_ABSCISSA_EXCEPTION_MSG);
        }
    }

    public static void isValidOrdinate(double y) {
        if (y < GraphConstants.ORDINATE_MIN) {
            final String INVALID_ORDINATE_EXCEPTION_MSG =
                    "Ordinate, 'y', out of valid range for this Point.";
            LOGGER.error(INVALID_ORDINATE_EXCEPTION_MSG);
            throw new InvalidOrdinateException(INVALID_ORDINATE_EXCEPTION_MSG);
        }
    }

    public static void isValidString(String pointLabel, String message) {
        if (BooleanUtils.isBlankString(pointLabel)) {
            LOGGER.error(message);
            throw new InvalidStringException(message);
        }
    }

    private ExceptionUtils() {
        preventUtilityInstantiation();
    }

    public static void areValidVertices(Vertex vertex1, Vertex vertex2) {
        final String INVALID_EDGE_EXCEPTION_MSG =
                "Both vertices must be non-null and have vertexIds to create an edge.";
        areValidVertices(vertex1, vertex2, INVALID_EDGE_EXCEPTION_MSG);
    }

    public static void areValidVertices(Vertex vertex1, Vertex vertex2, String message) {
        if (BooleanUtils.areInvalidVertices(vertex1, vertex2)) {
            LOGGER.error(message);
            throw new InvalidEdgeException(message);
        }
    }

    public static void isValidVertex(Vertex vertex, String message) {
        if (BooleanUtils.isInvalidVertex(vertex)) {
            LOGGER.error(message);
            throw new InvalidEdgeException(message);
        }
    }

    public static void isValidVertexOption() {
        final String INVALID_VERTEX_TARGET_EXCEPTION_MSG =
                "Invalid vertex number. Only '1' for this edge's vertex1 and '2' for vertex2 available.";
        LOGGER.error(INVALID_VERTEX_TARGET_EXCEPTION_MSG);
        throw new InvalidVertexOptionException(INVALID_VERTEX_TARGET_EXCEPTION_MSG);
    }

    public static void isValidVerticesMap(Map<String, Vertex> vertices) {
        if (BooleanUtils.isEmptyOrNullMap(vertices)) {
            LOGGER.error("Vertices map cannot be null or empty.");
            throw new EmptyVerticesMapException("Vertices map cannot be null or empty.");
        }
    }

    public static void isValidVertexId(String vertexId) {
        if (BooleanUtils.isBlankString(vertexId)) {
            final String INVALID_VERTEX_ID_EXCEPTION_MSG = "Vertex identifier must not be null or empty.";
            LOGGER.error(INVALID_VERTEX_ID_EXCEPTION_MSG);
            throw new InvalidVertexException(INVALID_VERTEX_ID_EXCEPTION_MSG);
        }
    }

    public static void isValidPoint(Point point, String message) {
        if (point == null) {
            LOGGER.error(message);
            throw new PointTypeIsNullException(message);
        }
    }
}
