package com.solvd.navigator.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class DriverConnection {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.DRIVER_CONNECTION);
    private String driverName;

    public DriverConnection(String name) {
        this.driverName = name;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DriverConnection driverConnection = (DriverConnection) obj;
        return driverName.equals(driverConnection.driverName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(driverName);
    }


    @Override
    public String toString() {
        Class<?> currClass = ClassConstants.DRIVER_CONNECTION;
        String[] fieldNames = {
                "driverName"
        };

        String fieldsString =
                StringFormatters.buildFieldsString(this, fieldNames);

        return StringFormatters.buildToString(currClass, fieldNames, fieldsString);
    }
}
