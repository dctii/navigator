package com.solvd.navigator.bin;

import com.solvd.navigator.util.ClassConstants;
import com.solvd.navigator.util.StringFormatters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class OrderItem {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.ORDER_ITEM);
    private int orderItemId;
    private int itemQuantity;
    private int orderId;
    private int itemId;
    public OrderItem() {

    }

    public OrderItem(int orderItemId, int itemQuantity, int orderId, int itemId) {
        this.orderItemId = orderItemId;
        this.itemQuantity = itemQuantity;
        this.orderId = orderId;
        this.itemId = itemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        OrderItem orderItem = (OrderItem) obj;
        return orderItemId == orderItem.orderItemId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderItemId);
    }

    @Override
    public String toString() {
        Class<?> currClass = ClassConstants.ORDER_ITEM;
        String[] fieldNames = {
                "orderItemId",
                "itemQuantity",
                "orderId",
                "itemId"
        };

        String fieldsString =
                StringFormatters.buildFieldsString(this, fieldNames);

        return StringFormatters.buildToString(currClass, fieldNames, fieldsString);
    }
}

