package com.solvd.navigator.dao;

import com.solvd.navigator.bin.OrderRecipient;
import com.solvd.navigator.bin.Vehicle;

import java.util.List;

public interface OrderRecipientDAO extends AbstractDAO<OrderRecipient> {

    @Override
    int create(OrderRecipient orderRecipient);

    @Override
    OrderRecipient getById(int orderRecipientId);
    List<OrderRecipient> getAll();

    @Override
    void update(OrderRecipient orderRecipient);

    @Override
    void delete(int orderRecipientId);
}


