package com.solvd.navigator.servicetwo;

import com.solvd.navigator.bin.Driver;
import com.solvd.navigator.bin.Location;
import com.solvd.navigator.bin.Order;
import com.solvd.navigator.bin.OrderRecipient;
import com.solvd.navigator.bin.Storage;

import java.sql.Timestamp;
import java.util.List;

public interface OrderService {
    List<Order> getAwaitingOrdersFromStorage(Storage storage, int limit);

    void updateOrderDelivered(Order order, Timestamp deliveryTime);

    void updateOrderPickedUp(Order order, Driver driver);


    OrderRecipient getRecipientOfOrder(Order order);

    Location getOrderLocation(Order order);

    List<Location> getLocationsListForOrders(List<Order> orders);

    void registerOrderListener(OrderListener listener);
    void unregisterOrderListener(OrderListener listener);

}
