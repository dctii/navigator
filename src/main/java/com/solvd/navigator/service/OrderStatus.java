package com.solvd.navigator.service;

import com.solvd.navigator.bin.Order;
import com.solvd.navigator.bin.Storage;

import java.util.List;

public interface OrderStatus {
    List<Order> getAwaitingOrders(Storage storage, int orderLimit);

    void updateOrderStatus(List<Order> orders, String fromStatus, String toStatus);
}
