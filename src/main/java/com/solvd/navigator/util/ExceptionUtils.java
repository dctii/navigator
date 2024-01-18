package com.solvd.navigator.util;

import com.solvd.navigator.exception.InvalidDateFormatException;
import com.solvd.navigator.exception.InvalidDecimalException;
import com.solvd.navigator.exception.StringLengthException;
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

    private ExceptionUtils() {
        preventUtilityInstantiation();
    }
}
