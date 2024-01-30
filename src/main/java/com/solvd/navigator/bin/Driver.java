package com.solvd.navigator.bin;

import com.solvd.navigator.math.RoutePlan;
import com.solvd.navigator.util.BooleanUtils;
import com.solvd.navigator.util.ClassConstants;
import com.solvd.navigator.util.StringFormatters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Driver {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.DRIVER);
    private int driverId;
    private int employeeId;
    private int vehicleId;
    private List<RoutePlan> routePlans;


    public Driver() {
        this.routePlans = new ArrayList<>();
    }

    private Driver(Builder builder) {
        this.driverId = builder.driverId;
        this.employeeId = builder.employeeId;
        this.vehicleId = builder.vehicleId;
        this.routePlans = new ArrayList<>();
    }


    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    // Builder class
    public static class Builder {
        private int driverId;
        private int employeeId;
        private int vehicleId;

        // Setter methods for each parameter
        public Builder setDriverId(int driverId) {
            this.driverId = driverId;
            return this;
        }

        public Builder setEmployeeId(int employeeId) {
            this.employeeId = employeeId;
            return this;
        }

        public Builder setVehicleId(int vehicleId) {
            this.vehicleId = vehicleId;
            return this;
        }

        // Build method to create an instance of Drivers
        public Driver build() {
            return new Driver(this);
        }
    }

    public List<RoutePlan> getRoutePlans() {
        return routePlans;
    }

    public void setRoutePlans(List<RoutePlan> routePlans) {
        if (BooleanUtils.isEmptyOrNullCollection(routePlans)) {
            LOGGER.error("Route plans cannot be empty or null to set");
            throw new IllegalStateException("Route plans cannot be empty or null to set");
        }
        this.routePlans = routePlans;
    }

    public void addRoutePlanToList(RoutePlan newRoutePlan) {
        if (routePlans == null) {
            LOGGER.error("Route plan cannot be null");
            throw new IllegalStateException("Route plan is empty");
        }
        routePlans.add(newRoutePlan);
    }

    public void removeRoutePlanFromList(RoutePlan newRoutePlan) {
        if (routePlans == null) {
            LOGGER.error("Route plan cannot be null for removal");
            throw new IllegalStateException("Route plan cannot be null for removal");
        }
        routePlans.remove(newRoutePlan);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Driver driver = (Driver) obj;
        return driverId == driver.driverId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(driverId);
    }

    @Override
    public String toString() {
        Class<?> currClass = ClassConstants.DRIVER;
        String[] fieldNames = {
                "driverId",
                "employeeId",
                "vehicleId",
                "routePlans"
        };

        String fieldsString = StringFormatters.buildFieldsString(this, fieldNames);
        return StringFormatters.buildToString(currClass, fieldNames, fieldsString);
    }
}
