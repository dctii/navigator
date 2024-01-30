package com.solvd.navigator.bin;

import com.solvd.navigator.util.ClassConstants;
import com.solvd.navigator.util.StringFormatters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class Vehicle {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.VEHICLE);
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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vehicle vehicle = (Vehicle) obj;
        return vehicleId == vehicle.vehicleId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicleId);
    }

    @Override
    public String toString() {
        Class<?> currClass = ClassConstants.VEHICLE;
        String[] fieldNames = {
                "vehicleId",
                "year",
                "make",
                "model",
                "trimLevel",
                "licensePlateNumber"
        };

        String fieldsString =
                StringFormatters.buildFieldsString(this, fieldNames);

        return StringFormatters.buildToString(currClass, fieldNames, fieldsString);
    }

}
