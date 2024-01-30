package com.solvd.navigator.bin;

import com.solvd.navigator.util.ClassConstants;
import com.solvd.navigator.util.StringFormatters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class Storage {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.STORAGE);
    private int storageId;
    private String name;
    private int locationId;

    public Storage() {

    }

    public Storage(int storageId, String name, int locationId) {
        this.storageId = storageId;
        this.name = name;
        this.locationId = locationId;
    }

    public int getStorageId() {
        return storageId;
    }

    public void setStorageId(int storageId) {
        this.storageId = storageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        Storage storage = (Storage) obj;
        return storageId == storage.storageId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(storageId);
    }

    @Override
    public String toString() {
        Class<?> currClass = ClassConstants.STORAGE;
        String[] fieldNames = {
                "storageId",
                "name",
                "locationId"
        };

        String fieldsString =
                StringFormatters.buildFieldsString(this, fieldNames);

        return StringFormatters.buildToString(currClass, fieldNames, fieldsString);
    }

}
