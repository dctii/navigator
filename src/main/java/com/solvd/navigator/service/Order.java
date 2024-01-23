package com.solvd.navigator.service;

import com.solvd.navigator.bin.Location;

import java.util.List;

public interface Order {

    List<Location> getOrderRoute(int orderId);

    void updateOrderStatus(int orderId, String newStatus);

    void notifyOrderDelay(int orderId, double estimatedDelay);


}
