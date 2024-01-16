package com.solvd.navigator.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegExpConstants {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.REG_EXP_CONSTANTS);
    public static final String LOWERCASE_Y_OR_N = "[yn]";
    public static final String DECIMAL_WITH_SCALE_OF_0_OR_2 = "\\d+(\\.\\d{1,2})?";


    private RegExpConstants() {
        ExceptionUtils.preventConstantsInstantiation();
    }
}
