package com.solvd.navigator.dao;

import com.solvd.navigator.bin.Order;
import com.solvd.navigator.bin.Storage;

import java.util.List;

public interface OrderDAO extends AbstractDAO<Order> {
    @Override
    int create(Order order) ;

    @Override
    Order getById(int OrderId) ;

    List<Order> getAllAwaitingOrdersByStorageId(int storageId);

    List<Order> getLimitedAwaitingOrdersByStorageId(int storageId, int orderLimit);

    @Override
   void update(Order order) ;

    @Override
    void delete(int orderId);
}
