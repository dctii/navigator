package com.solvd.navigator.bin;

public class OrderItem {
    private int orderItem;
    private int itemQuantity;
    private int orderId;
    private int itemId;

    public OrderItem(int orderItem, int itemQuantity, int orderId, int itemId) {
        this.orderItem = orderItem;
        this.itemQuantity = itemQuantity;
        this.orderId = orderId;
        this.itemId = itemId;
    }

    public void setOrderItem(int orderItem) {
        this.orderItem = orderItem;
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
    public String toString() {
        return "OrderItems{" +
               "orderItem=" + orderItem +
               ", itemQuantity=" + itemQuantity +
               ", orderId=" + orderId +
               ", itemId=" + itemId +
               '}';
    }
}

