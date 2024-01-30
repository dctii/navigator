package com.solvd.navigator.bin;

import com.solvd.navigator.util.ClassConstants;
import com.solvd.navigator.util.StringFormatters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class OrderRecipient {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.ORDER_RECIPIENT);
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
        Class<?> currClass = ClassConstants.ORDER_RECIPIENT;
        String[] fieldNames = {
                "orderRecipientId",
                "personId",
                "locationId"
        };

        String fieldsString =
                StringFormatters.buildFieldsString(this, fieldNames);

        return StringFormatters.buildToString(currClass, fieldNames, fieldsString);
    }

}
