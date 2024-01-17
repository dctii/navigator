package com.solvd.navigator.bin;

public class Drivers {
    private int driverId;
    private int employeeId;
    private int vehicleId;


    public Drivers(int driverId, int employeeId, int vehicleId) {
        this.driverId = driverId;
        this.employeeId = employeeId;
        this.vehicleId = vehicleId;
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

    @Override
    public String toString() {
        return "Drivers{" +
               "driverId=" + driverId +
               ", employeeId=" + employeeId +
               ", vehicleId=" + vehicleId +
               '}';
    }

}