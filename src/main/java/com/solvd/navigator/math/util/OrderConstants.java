package com.solvd.navigator.math.util;

import com.solvd.navigator.util.ExceptionUtils;

public final class OrderConstants {
    public static final int[] ALL_STORAGE_WAREHOUSE_IDS = {1, 2, 3, 4};
    public static final String ORDER_STATUS_AWAITING_DELIVERY = "Awaiting Delivery";
    public static final String ORDER_STATUS_IN_TRANSIT = "In Transit";
    public static final String ORDER_STATUS_DELIVERED = "Delivered";
    public static final int MINUTES_PER_KM = 10;
    public static final int DRIVER_ORDER_LIMIT = 8;

    private OrderConstants() {
        ExceptionUtils.preventConstantsInstantiation();
    }
}
