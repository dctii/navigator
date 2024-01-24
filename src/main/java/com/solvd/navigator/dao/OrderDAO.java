package com.solvd.navigator.dao;

import com.solvd.navigator.bin.Order;

public interface OrderDAO extends AbstractDAO<Order> {
    @Override
    int create(Order order) ;

    @Override
    Order getById(int OrderId) ;

    @Override
   void update(Order order) ;

    @Override
    void delete(int orderId);
}
