package com.solvd.navigator.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MenuUtils {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.MENU_UTILS);

    public static int parseIntOption(String input) {
        int parsedOption = -1;
        try {
            parsedOption = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            LOGGER.error("Invalid input. Please enter a number.");
        }
        return parsedOption;
    }

    private MenuUtils() {
        ExceptionUtils.preventUtilityInstantiation();
    }
}
