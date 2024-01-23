package com.solvd.navigator.bin;

public class Vehicle {
    private int vehicleId;
    private int year;
    private String make;
    private String model;
    private String trimLevel;
    private String licensePlateNumber;
    public Vehicle() {

    }

    public Vehicle(int vehicleId, int year, String make, String model, String trimLevel, String licensePlateNumber) {
        this.vehicleId = vehicleId;
        this.year = year;
        this.make = make;
        this.model = model;
        this.trimLevel = trimLevel;
        this.licensePlateNumber = licensePlateNumber;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTrimLevel() {
        return trimLevel;
    }

    public void setTrimLevel(String trimLevel) {
        this.trimLevel = trimLevel;
    }

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    @Override
    public String toString() {
        return "Vehicles{" +
               "vehicleId=" + vehicleId +
               ", year=" + year +
               ", make='" + make + '\'' +
               ", model='" + model + '\'' +
               ", trimLevel='" + trimLevel + '\'' +
               ", licensePlateNumber='" + licensePlateNumber + '\'' +
               '}';
    }

}
