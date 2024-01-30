package com.solvd.navigator.servicetwo;

import com.solvd.navigator.bin.Location;
import com.solvd.navigator.bin.Order;

public interface OrderListener {
    void onOrderPickedUp(Order order, int driverId);
    void onOrderDelivered(Order order, int driverId, Location location);
}
