package com.solvd.navigator.dao;

import com.solvd.navigator.bin.OrderRecipient;
import com.solvd.navigator.bin.Vehicle;

public interface OrderRecipientDAO extends AbstractDAO<OrderRecipient> {

    @Override
    int create(OrderRecipient orderRecipient);

    @Override
    OrderRecipient getById(int orderRecipientId);

    @Override
    void update(OrderRecipient orderRecipient);

    @Override
    void delete(int orderRecipientId);
}


