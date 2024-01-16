package com.solvd.airport.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArrayUtils {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.ARRAY_UTILS);

    public static int[] intArrayOf(int... items) {
        return items;
    }


    private ArrayUtils() {
        ExceptionUtils.preventUtilityInstantiation();
    }
}
