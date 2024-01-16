package com.solvd.navigator.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class ScannerUtils {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.SCANNER_UTILS);


    public static String getInput(Scanner scanner, String prompt) {
        String input;
        LOGGER.info("{}" + prompt + "{}", AnsiCodes.BLUE, AnsiCodes.RESET_ALL);
        input = scanner.nextLine();
        return input;
    }

    public static String getAndCleanInput(Scanner scanner, String prompt,
                                          UnaryOperator<String> formatter) {
        String input;
        LOGGER.info("{}" + prompt + "{}", AnsiCodes.BLUE, AnsiCodes.RESET_ALL);
        input = scanner.nextLine();
        if (formatter != null) {
            input = formatter.apply(input);
        }
        return input;
    }


    public static String getAndCheckInput(Scanner scanner, String prompt, Predicate<String> validator) {
        String input;
        do {
            LOGGER.info("{}" + prompt + "{}", AnsiCodes.BLUE, AnsiCodes.RESET_ALL);
            input = scanner.nextLine();
        } while (validator != null && !validator.test(input));
        return input;
    }

    public static String checkAndCleanInput(Scanner scanner, String prompt,
                                            UnaryOperator<String> formatter,
                                            Predicate<String> validator) {
        String input;
        do {
            LOGGER.info("{}" + prompt + "{}", AnsiCodes.BLUE, AnsiCodes.RESET_ALL);
            input = scanner.nextLine();
            if (formatter != null) {
                input = formatter.apply(input);
            }
        } while (validator != null && !validator.test(input));
        return input;
    }


    public static Date getDateInput(Scanner scanner, String prompt, String datePattern) {
        String input;
        LocalDate date;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);

        while (true) {
            try {
                LOGGER.info("{}" + prompt + "{}", AnsiCodes.BLUE, AnsiCodes.RESET_ALL);
                input = scanner.nextLine();
                date = LocalDate.parse(input, formatter);
                break;
            } catch (DateTimeParseException e) {
                LOGGER.error("Invalid date format or invalid date. Please enter a date in YYYY-MM-DD format.");
            }
        }
        /*
            "Converting Between LocalDate and SQL Date"
            https://www.baeldung.com/java-convert-localdate-sql-date
        */
        return java.sql.Date.valueOf(date);
    }

    private ScannerUtils() {
        ExceptionUtils.preventUtilityInstantiation();
    }
}
