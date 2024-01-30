package com.solvd.navigator.bin;
import com.solvd.navigator.util.ClassConstants;
import com.solvd.navigator.util.StringFormatters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.util.Objects;

public class Order {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.ORDER);
    private int orderId;
    private String orderNumber;
    private String orderStatus;
    private Timestamp orderDate;
    private Timestamp deliveryDate;
    private int storageId;
    private int orderRecipientId;
    private int driverId;
    public Order() {

    }


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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Order order = (Order) obj;
        return orderId == order.orderId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }

    @Override
    public String toString() {
        Class<?> currClass = ClassConstants.ORDER;
        String[] fieldNames = {
                "orderId",
                "orderNumber",
                "orderStatus",
                "orderDate",
                "deliveryDate",
                "storageId",
                "orderRecipientId",
                "driverId"
        };

        String fieldsString =
                StringFormatters.buildFieldsString(this, fieldNames);

        return StringFormatters.buildToString(currClass, fieldNames, fieldsString);
    }
}
