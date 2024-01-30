package com.solvd.navigator.servicetwo.impl;

import com.solvd.navigator.bin.Location;
import com.solvd.navigator.bin.Order;
import com.solvd.navigator.servicetwo.OrderListener;
import com.solvd.navigator.util.AnsiCodes;
import com.solvd.navigator.util.ClassConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OrderListenerImpl implements OrderListener {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.ORDER_LISTENER_IMPL);

    @Override
    public void onOrderPickedUp(Order order, int driverId) {
        LOGGER.info(
                "{}'Driver {}' picked up order '{}' from 'Storage {}'{}",
                AnsiCodes.GREEN,
                driverId,
                order.getOrderNumber(),
                order.getStorageId(),
                AnsiCodes.RESET_ALL
        );
    }

    @Override
    public void onOrderDelivered(Order order, int driverId, Location location) {
        LOGGER.info(
                "{}Order '{}' successfully delivered by Driver '{}' to 'Location {} ({}, {})'{}",
                AnsiCodes.GREEN,
                order.getOrderNumber(),
                driverId,
                location.getLocationId(), location.getCoordinateX(), location.getCoordinateY(),
                AnsiCodes.RESET_ALL
        );
    }
}
