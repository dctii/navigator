package com.solvd.airport.util;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.solvd.airport.exception.InvalidDateFormatException;
import com.solvd.airport.exception.InvalidDecimalException;
import com.solvd.airport.exception.InvalidPhoneNumberException;
import com.solvd.airport.exception.InvalidPhoneNumberExtensionException;
import com.solvd.airport.exception.InvalidTimeZoneException;
import com.solvd.airport.exception.NotInternationalPhoneNumberException;
import com.solvd.airport.exception.StringLengthException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class ExceptionUtils {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.EXCEPTION_UTILS);

    public static void preventUtilityInstantiation() {
        final String NO_UTILITY_CLASS_INSTANTIATION_MESSAGE =
                "This is a utility class and instances cannot be made of it.";

        throw new UnsupportedOperationException(NO_UTILITY_CLASS_INSTANTIATION_MESSAGE);
    }

    public static void preventConstantsInstantiation() {
        final String NO_CONSTANTS_INSTANTIATION_MESSAGE =
                "This is a constants class and instances cannot be made of it.";

        throw new UnsupportedOperationException(NO_CONSTANTS_INSTANTIATION_MESSAGE);
    }

    public static void isValidTimeZone(String timezone) {
        if (!Arrays.asList(TimeZone.getAvailableIDs()).contains(timezone)) {
            throw new InvalidTimeZoneException("Invalid timezone: " + timezone);
        }
    }

    public static void isNullOrValidTimeZone(String timezone) {
        if (timezone == null) return; // null is acceptable, so we return immediately
        isValidTimeZone(timezone);
    }

    public static void isValidCountryCode(String countryCode) {
        if (!Arrays.asList(Locale.getISOCountries()).contains(countryCode)) {
            throw new InvalidPhoneNumberException("Invalid country code: " + countryCode);
        }
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

    // Add to phone number instances so using dashes and parenthesis can be accepted.
    public static String sanitizeAndCheckPhoneNumberString(String phoneNumber) {
        isInternationalPhoneNumberFormat(phoneNumber);
        String sanitizedPhoneNumber = StringFormatters.sanitizePhoneNumber(phoneNumber);
        isValidPhoneNumber(sanitizedPhoneNumber);

        return sanitizedPhoneNumber;
    }

    public static void isValidPhoneNumber(String phoneNumber) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            com.google.i18n.phonenumbers.Phonenumber.PhoneNumber numberProto =
                    phoneUtil.parse(phoneNumber, null);
            if (!phoneUtil.isValidNumber(numberProto)) {
                throw new InvalidPhoneNumberException("Invalid phone number format");
            }
        } catch (NumberParseException e) {
            throw new InvalidPhoneNumberException("Unable to parse phone number string" + e.toString());
        }
    }

    public static void isValidPhoneNumberExtension(String phoneNumberExtension) {
        if (phoneNumberExtension != null && (
                phoneNumberExtension.isEmpty() ||
                        !phoneNumberExtension.matches(RegExpConstants.PHONE_NUMBER_EXTENSION_VALID_CHARS_AND_ORDER)
        )) {
            throw new InvalidPhoneNumberExtensionException("Invalid phone number extension format, can only include alphanumeric characters and an optional '#' or '*' at the end");
        }
    }


    public static void isInternationalPhoneNumberFormat(String phoneNumber) {
        if (!phoneNumber.startsWith("+")) {
            throw new NotInternationalPhoneNumberException("Invalid phone number, must be in international format. It must start with '+'");
        }
    }

    public static void isValidSex(String sex) {
        if (
                !sex.equalsIgnoreCase("M")
                        && !sex.equalsIgnoreCase("F")
        ) {
            final String SEX_MUST_BE_M_OR_F_MSG =
                    "The value for the field 'sex' must be 'M' or 'F'";
            throw new IllegalArgumentException(SEX_MUST_BE_M_OR_F_MSG);
        }
    }

    private ExceptionUtils() {
        preventUtilityInstantiation();
    }
}
