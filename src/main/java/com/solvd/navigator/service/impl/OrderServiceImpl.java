package com.solvd.navigator.service.impl;

import com.solvd.navigator.bin.Order;
import com.solvd.navigator.bin.Storage;
import com.solvd.navigator.dao.OrderDAO;
import com.solvd.navigator.math.util.OrderConstants;
import com.solvd.navigator.service.OrderStatus;
import com.solvd.navigator.util.DAOFactory;
import com.solvd.navigator.util.FilepathConstants;
import com.solvd.navigator.util.JacksonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderStatus {


    private static final Logger LOGGER = LogManager.getLogger(OrderServiceImpl.class);
    //OrderDAO orderDAO = DAOFactory.createDAO(OrderDAO.class);
    private OrderDAO orderDAO;

    public OrderServiceImpl(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    @Override
    public List<Order> getAwaitingOrders(Storage storage, int orderLimit) {
        return orderDAO.getLimitedAwaitingOrdersByStorageId(storage.getStorageId(), orderLimit);
    }

    @Override
    public void updateOrderStatus(List<Order> orders, String fromStatus, String toStatus) {
        orders.forEach(order -> {
            if (order.getOrderStatus().equals(fromStatus)) {
                order.setOrderStatus(toStatus);
                orderDAO.update(order);
                LOGGER.info("Order status updated: {}", order);
            }
        });
    }



    public void simulateOrderProcessing(Order order) {
        LOGGER.info("Processing order: {}", order);
        updateOrderStatus(List.of(order), OrderConstants.ORDER_STATUS_AWAITING_DELIVERY, OrderConstants.ORDER_STATUS_IN_TRANSIT);
        LOGGER.info("Order processed: {}", order);
    }
}