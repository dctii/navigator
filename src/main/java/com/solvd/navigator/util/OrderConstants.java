package com.solvd.navigator.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public final class OrderConstants {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.ORDER_CONSTANTS);
    public static final List<Integer> ALL_STORAGE_WAREHOUSE_IDS = List.of(1, 2, 3, 4);
    public static final String ORDER_STATUS_AWAITING_DELIVERY = "Awaiting Delivery";
    public static final String ORDER_STATUS_IN_TRANSIT = "In Transit";
    public static final String ORDER_STATUS_DELIVERED = "Delivered";
    public static final int MINUTES_PER_KM = 10;
    public static final int DRIVER_ORDER_LIMIT = 8;

    public static final String DRIVER_ONE_FIRST_NAME = "Billy";
    public static final String DRIVER_ONE_LAST_NAME = "Driver";
    public static final String DRIVER_TWO_FIRST_NAME = "Milly";
    public static final String DRIVER_TWO_LAST_NAME = DRIVER_ONE_LAST_NAME;

    public static final int TOTAL_NUMBER_OF_DRIVERS = 2;
    public static final int TOTAL_NUMBER_OF_PERSONS = 81;
    public static final int TOTAL_NUMBER_OF_ORDER_RECIPIENTS =
            TOTAL_NUMBER_OF_PERSONS - TOTAL_NUMBER_OF_DRIVERS;

    public static final int TOTAL_NUMBER_OF_ORDERS_PER_WAREHOUSE = 1000;
    public static final int TOTAL_NUMBER_OF_ORDERS = TOTAL_NUMBER_OF_ORDERS_PER_WAREHOUSE * ALL_STORAGE_WAREHOUSE_IDS.size();

    public static final double MAX_WORK_HOURS = 8.0;
    public static final double MAX_WORK_HOURS_IN_MINUTES = NumberUtils.roundToScale(MAX_WORK_HOURS * 60, 2);


    private OrderConstants() {
        ExceptionUtils.preventConstantsInstantiation();
    }
}
