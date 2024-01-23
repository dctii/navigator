package com.solvd.navigator.bin;

public class Driver {
    private int driverId;
    private int employeeId;
    private int vehicleId;


    public Driver() {

    }

    private Driver(Builder builder) {
        this.driverId = builder.driverId;
        this.employeeId = builder.employeeId;
        this.vehicleId = builder.vehicleId;
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
}
