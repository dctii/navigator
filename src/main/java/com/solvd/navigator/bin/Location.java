package com.solvd.navigator.bin;

import java.util.Objects;

public class Location {
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
        return "Locations{" +
                "locationId=" + locationId +
                ", coordinateX=" + coordinateX +
                ", coordinateY=" + coordinateY +
                '}';
    }
}

