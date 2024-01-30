package com.solvd.navigator.bin;

import com.solvd.navigator.util.ClassConstants;
import com.solvd.navigator.util.StringFormatters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class Location {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.LOCATION);
    private int locationId;
    private float coordinateX;
    private float coordinateY;

    public Location() {

    }

    public Location(int locationId) {
        this.locationId = locationId;
    }

    public Location(int locationId, float coordinateX, float coordinateY) {
        this.locationId = locationId;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public float getCoordinateX() {
        return coordinateX;
    }

    public void setCoordinateX(float coordinateX) {
        this.coordinateX = coordinateX;
    }

    public float getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateY(float coordinateY) {
        this.coordinateY = coordinateY;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Location location = (Location) obj;
        return locationId == location.locationId &&
                Float.compare(location.coordinateX, coordinateX) == 0 &&
                Float.compare(location.coordinateY, coordinateY) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationId, coordinateX, coordinateY);
    }

    @Override
    public String toString() {
        Class<?> currClass = ClassConstants.LOCATION;
        String[] fieldNames = {
                "locationId",
                "coordinateX",
                "coordinateY"
        };

        String fieldsString =
                StringFormatters.buildFieldsString(this, fieldNames);

        return StringFormatters.buildToString(currClass, fieldNames, fieldsString);
    }
}

