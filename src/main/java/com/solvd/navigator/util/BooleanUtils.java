package com.solvd.navigator.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Objects;

public class BooleanUtils {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.BOOLEAN_UTILS);

    // check if array is empty or null, or if an array full of null items
    public static boolean isEmptyOrNullArray(Object[] array) {
        return array == null
                || array.length == 0
                || Arrays.stream(array).allMatch(Objects::isNull);
    }

    public static boolean isNotEmptyOrNullArray(Object[] array) {
        return !isEmptyOrNullArray(array);
    }

    public static boolean isBlankOrEmptyString(String string) {
        return StringUtils.isBlank(string) || StringUtils.isEmpty(string);
    }

    public static boolean isNotBlankOrEmptyString(String string) {
        return !isBlankOrEmptyString(string);
    }


    public static boolean isSameDate(LocalDate comparans, LocalDate comparandum) {
        return comparans.isEqual(comparandum);
    }

    public static boolean isSameDate(ZonedDateTime comparans, ZonedDateTime comparandum) {
        return comparans.isEqual(comparandum);
    }

    public static boolean isWeekend(DayOfWeek dayOfWeek) {
        final DayOfWeek[] weekends = {
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
        };

        return Arrays
                .asList(weekends)
                .contains(dayOfWeek);
    }

    public static boolean isValidEmail(String email) {
        int atSignPosition = email.indexOf(StringConstants.AT_SIGN);
        int dotPosition = email.lastIndexOf(StringConstants.FULL_STOP);

        return atSignPosition > 0
                && dotPosition > atSignPosition + 1
                && dotPosition < email.length() - 1;
    }


    private BooleanUtils() {
        ExceptionUtils.preventUtilityInstantiation();
    }
}
