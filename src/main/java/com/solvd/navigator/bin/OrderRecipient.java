package com.solvd.navigator.bin;

import java.util.Objects;

public class OrderRecipient {
    private int orderRecipientId;
    private int personId;
    private int locationId;
    public OrderRecipient() {

    }

    public OrderRecipient(int orderRecipientId, int personId, int locationId) {
        this.orderRecipientId = orderRecipientId;
        this.personId = personId;
        this.locationId = locationId;
    }

    public int getOrderRecipientId() {
        return orderRecipientId;
    }

    public void setOrderRecipientId(int orderRecipientId) {
        this.orderRecipientId = orderRecipientId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        OrderRecipient orderRecipient = (OrderRecipient) obj;
        return orderRecipientId == orderRecipient.orderRecipientId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderRecipientId);
    }

    @Override
    public String toString() {
        return "OrderRecipients{" +
               "orderRecipientId=" + orderRecipientId +
               ", personId=" + personId +
               ", locationId=" + locationId +
               '}';
    }

}
