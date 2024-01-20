package com.solvd.navigator.bin;
import java.sql.Timestamp;
public class Order {
    private int orderId;
    private String orderNumber;
    private String orderStatus;
    private Timestamp orderDate;
    private Timestamp deliveryDate;
    private int storageId;
    private int orderRecipientId;
    private int driverId;


    public Order(int orderId, String orderNumber, String orderStatus, Timestamp orderDate, Timestamp deliveryDate, int storageId, int orderRecipientId, int driverId) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.storageId = storageId;
        this.orderRecipientId = orderRecipientId;
        this.driverId = driverId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public Timestamp getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Timestamp deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public int getStorageId() {
        return storageId;
    }

    public void setStorageId(int storageId) {
        this.storageId = storageId;
    }

    public int getOrderRecipientId() {
        return orderRecipientId;
    }

    public void setOrderRecipientId(int orderRecipientId) {
        this.orderRecipientId = orderRecipientId;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    @Override
    public String toString() {
        return "Order{" +
               "orderId=" + orderId +
               ", orderNumber='" + orderNumber + '\'' +
               ", orderStatus='" + orderStatus + '\'' +
               ", orderDate=" + orderDate +
               ", deliveryDate=" + deliveryDate +
               ", storageId=" + storageId +
               ", orderRecipientId=" + orderRecipientId +
               ", driverId=" + driverId +
               '}';
    }
}
