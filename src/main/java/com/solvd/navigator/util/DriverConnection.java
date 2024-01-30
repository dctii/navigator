package com.solvd.navigator.util;

public class DriverConnection {
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
    public String toString() {
        Class<?> currClass = DriverConnection.class;
        String[] fieldNames = {
                "driverName"
        };

        String fieldsString =
                StringFormatters.buildFieldsString(this, fieldNames);

        return StringFormatters.buildToString(currClass, fieldNames, fieldsString);
    }
}
