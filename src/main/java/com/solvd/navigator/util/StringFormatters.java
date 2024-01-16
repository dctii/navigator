package com.solvd.airport.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringFormatters {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.STRING_FORMATTERS);

    public static String removeEdges(String string) {
        int stringLength = string.length();
        int firstCharPosition = 1;
        int lastCharPosition = stringLength - 1;

        if (BooleanUtils.isNotBlankOrEmptyString(string) && stringLength > 1) {

            // returns the string without its first and last character
            return StringUtils
                    .substring(
                            string,
                            firstCharPosition,
                            lastCharPosition
                    );
        }
        return string;
    }

    public static String nestInChars(String leftBoundaryMarker, String rightBoundaryMarker, String stringToWrap) {
        return leftBoundaryMarker + stringToWrap + rightBoundaryMarker;
    }

    public static String nestInParentheses(String string) {
        return nestInChars(
                StringConstants.OPENING_PARENTHESIS,
                StringConstants.CLOSING_PARENTHESIS,
                string
        );
    }

    public static String nestInSingleQuotations(String string) {
        return nestInChars(
                StringConstants.SINGLE_QUOTATION,
                StringConstants.SINGLE_QUOTATION,
                string
        );
    }

    public static String nestInCurlyBraces(String string) {
        return nestInChars(
                StringConstants.OPENING_CURLY_BRACE,
                StringConstants.CLOSING_CURLY_BRACE,
                string
        );
    }

    public static String stateEquivalence(Object leftOperand, Object rightOperand) {
        String leftOperandString = leftOperand.toString();
        String rightOperandString = rightOperand.toString();

        return leftOperandString + StringConstants.EQUALS_OPERATOR + rightOperandString;
    }

    public static String stateEquivalence(Object leftOperand, Object[] rightOperand) {
        String leftOperandString = leftOperand.toString();
        String rightOperandString = Arrays.toString(rightOperand);

        return leftOperandString + StringConstants.EQUALS_OPERATOR + rightOperandString;
    }

    public static String collectionToString(Collection<?> collection) {
        return collection.stream()
                .map(Object::toString)
                .collect(Collectors.joining(StringConstants.COMMA_DELIMITER));
    }

    public static String mapToString(Map<?, ?> targetMap) {
        return targetMap.entrySet().stream()
                .map(StringFormatters::stringifyMapEntry)
                .collect(Collectors.joining(StringConstants.COMMA_DELIMITER));
    }

    private static String stringifyMapEntry(Map.Entry<?, ?> entry) {
        Object key = entry.getKey();
        Object value = entry.getValue();
        String valueString;

        if (value instanceof Collection) {
            valueString = collectionToString((Collection<?>) value);
        } else if (value != null) {
            valueString = value.toString();
        } else {
            valueString = StringConstants.NULL_STRING;
        }

        return StringUtils.joinWith(StringConstants.COLON_DELIMITER, key, valueString);
    }


    public static String buildFieldsString(Object object, String[] fieldNames) {
        return Arrays.stream(fieldNames)
                .map(fieldName -> {
                    Object fieldValue = ReflectionUtils.getField(object, fieldName);

                    if (fieldValue == null) {
                        return stateEquivalence(
                                fieldName,
                                StringConstants.NULL_STRING
                        );
                    } else {
                        if (fieldValue.getClass().isArray()) {
                            return stateEquivalence(
                                    fieldName,
                                    (Object[]) fieldValue
                            );
                        } else if (fieldValue instanceof Map) {
                            return stateEquivalence(
                                    fieldName,
                                    mapToString((Map<?, ?>) fieldValue)
                            );
                        } else if (fieldValue instanceof Collection) {
                            return stateEquivalence(
                                    fieldName,
                                    collectionToString((Collection<?>) fieldValue)
                            );
                        } else {
                            return stateEquivalence(
                                    fieldName,
                                    fieldValue
                            );
                        }
                    }
                })
                .collect(Collectors.joining(StringConstants.COMMA_DELIMITER));
    }

    public static String buildToString(Class<?> currClass, String[] fieldNames,
                                       String parentToString,
                                       String fieldsString) {
        String className = currClass.getSimpleName();

        // removes the parent class name as header and removes curly braces wrapping body
        parentToString = StringFormatters.cleanParentToString(currClass, parentToString);

        if (fieldNames.length != 0) {
            // check if parentToString is empty after cleaning, if so, do not use delimiter
            String finalToStringBody;
            if (BooleanUtils.isBlankOrEmptyString(parentToString)) {
                finalToStringBody = fieldsString;
            } else {
                finalToStringBody = parentToString
                        + StringConstants.COMMA_DELIMITER
                        + fieldsString;
            }

            return className + StringFormatters.nestInCurlyBraces(finalToStringBody);

        } else {

            if (BooleanUtils.isBlankOrEmptyString(parentToString)) {
                return buildToString(currClass);
            } else {
                return buildToString(currClass, parentToString);
            }

        }
    }

    public static String buildToString(Class<?> currClass, String[] fieldNames,
                                       String fieldsString) {
        String className = currClass.getSimpleName();

        if (BooleanUtils.isEmptyOrNullArray(fieldNames)) {
            fieldsString = StringConstants.EMPTY_STRING;
        }
        return className + StringFormatters.nestInCurlyBraces(fieldsString);
    }

    public static String buildToString(Class<?> currClass, String parentToString) {
        String className = currClass.getSimpleName();
        parentToString = StringFormatters.cleanParentToString(currClass, parentToString);

        return className
                + StringFormatters.nestInCurlyBraces(parentToString);
    }

    public static String buildToString(Class<?> currClass) {
        String className = currClass.getSimpleName();

        return className
                + StringFormatters.nestInCurlyBraces(StringConstants.EMPTY_STRING);
    }


    public static String cleanParentToString(Class<?> currClass, String parentToString) {
        String parentName = currClass.getSuperclass().getSimpleName();

        parentToString = StringUtils.replace(
                parentToString,
                parentName,
                StringConstants.EMPTY_STRING
        );

        parentToString = removeEdges(parentToString);

        return parentToString;
    }

    public static String toReadableDateString(LocalDate datetime) {
        return datetime.getMonth()
                + StringConstants.DASH_STRING + datetime.getDayOfMonth()
                + StringConstants.DASH_STRING + datetime.getYear();
    }

    public static String toReadableDateString(ZonedDateTime datetime) {
        return datetime.getMonth()
                + StringConstants.DASH_STRING + datetime.getDayOfMonth()
                + StringConstants.DASH_STRING + datetime.getYear();

    }

    public static String toUSD(Number cost) {
        /*
            "USD Currency Formatting in Java"
            https://stackoverflow.com/questions/3075743/usd-currency-formatting-in-java

            "Class Locale: US"
            https://docs.oracle.com/javase/8/docs/api/java/util/Locale.html#US
        */
        NumberFormat usdFormat = NumberFormat.getCurrencyInstance(Locale.US);
        return usdFormat.format(cost);
    }

    public static String createBaggageCode(String flightCode, String destinationAirportCode, String bookingNumber) {
        return StringUtils.joinWith(
                StringConstants.DASH_STRING,
                flightCode, destinationAirportCode, bookingNumber
        );
    }

    public static String sanitizePhoneNumber(String phoneNumber) {
        return phoneNumber
                .replaceAll(StringConstants.SINGLE_WHITESPACE, StringConstants.EMPTY_STRING)
                .replaceAll(StringConstants.DASH_STRING, StringConstants.EMPTY_STRING)
                .replaceAll(Pattern.quote(StringConstants.OPENING_PARENTHESIS), StringConstants.EMPTY_STRING)
                .replaceAll(Pattern.quote(StringConstants.CLOSING_PARENTHESIS), StringConstants.EMPTY_STRING);
    }



    private StringFormatters() {
        ExceptionUtils.preventUtilityInstantiation();
    }
}
