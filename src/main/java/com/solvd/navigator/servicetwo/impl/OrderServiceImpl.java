package com.solvd.navigator.servicetwo.impl;

import com.solvd.navigator.bin.Driver;
import com.solvd.navigator.bin.Location;
import com.solvd.navigator.bin.Order;
import com.solvd.navigator.bin.OrderRecipient;
import com.solvd.navigator.bin.Storage;
import com.solvd.navigator.dao.LocationDAO;
import com.solvd.navigator.dao.OrderDAO;
import com.solvd.navigator.dao.OrderRecipientDAO;
import com.solvd.navigator.dao.StorageDAO;
import com.solvd.navigator.math.util.OrderConstants;
import com.solvd.navigator.servicetwo.OrderListener;
import com.solvd.navigator.servicetwo.OrderService;
import com.solvd.navigator.util.ClassConstants;
import com.solvd.navigator.util.DAOFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {
    private final OrderDAO orderDAO =
            DAOFactory.createDAO(ClassConstants.ORDER_DAO);
    private final OrderRecipientDAO orderRecipientDAO =
            DAOFactory.createDAO(ClassConstants.ORDER_RECIPIENT_DAO);

    private final StorageDAO storageDAO =
            DAOFactory.createDAO(ClassConstants.STORAGE_DAO);

    private final LocationDAO locationDAO =
            DAOFactory.createDAO(ClassConstants.LOCATION_DAO);

    private final List<OrderListener> orderListeners = new ArrayList<>();

    @Override
    public List<Order> getAwaitingOrdersFromStorage(Storage storage, int limit) {
        return orderDAO.getLimitedAwaitingOrdersByStorageId(storage.getStorageId(), limit);
    }

    @Override
    public void registerOrderListener(OrderListener listener) {
        orderListeners.add(listener);
    }

    @Override
    public void unregisterOrderListener(OrderListener listener) {
        orderListeners.remove(listener);
    }


    private void notifyOrderDelivered(Order order, int driverId, Location location) {
        orderListeners.forEach(
                listener ->
                        listener.onOrderDelivered(order, driverId, location)
        );
    }


    private void notifyOrderPickedUp(Order order, int driverId) {
        Storage orderStorage = storageDAO.getById(order.getStorageId());

        orderListeners.forEach(
                listener ->
                        listener.onOrderPickedUp(order, driverId)
        );
    }

    @Override
    public void updateOrderDelivered(Order order, Timestamp deliveryTime) {
        order.setOrderStatus(OrderConstants.ORDER_STATUS_DELIVERED);
        order.setDeliveryDate(deliveryTime);
        orderDAO.update(order);

        if (order.getOrderStatus().equals(OrderConstants.ORDER_STATUS_DELIVERED)) {
            notifyOrderDelivered(
                    order,
                    order.getDriverId(),
                    getOrderLocation(order)
            );
        }
    }

    @Override
    public void updateOrderPickedUp(Order order, Driver driver) {
        order.setDriverId(driver.getDriverId());
        order.setOrderStatus(OrderConstants.ORDER_STATUS_IN_TRANSIT);
        orderDAO.update(order);

        if (
                order.getOrderStatus().equals(OrderConstants.ORDER_STATUS_IN_TRANSIT)
                && order.getDriverId() == driver.getDriverId()
        ) {
            notifyOrderPickedUp(order, order.getDriverId());
        }

    }

    @Override
    public OrderRecipient getRecipientOfOrder(Order order) {
        return orderRecipientDAO.getById(order.getOrderRecipientId());
    }

    @Override
    public Location getOrderLocation(Order order) {
        OrderRecipient orderRecipient = getRecipientOfOrder(order);
        return locationDAO.getById(orderRecipient.getLocationId());
    }

    @Override
    public List<Location> getLocationsListForOrders(List<Order> orders) {
        return orders.stream() // get locations for orders
                .map(this::getOrderLocation)
                .collect(Collectors.toList());
    }

}
