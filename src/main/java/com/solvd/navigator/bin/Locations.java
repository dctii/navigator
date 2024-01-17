package com.solvd.navigator.bin;

public class Locations {
    private int locationId;
    private float coordinateX;
    private float coordinateY;

    public Locations(int locationId, float coordinateX, float coordinateY) {
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
    public String toString() {
        return "Locations{" +
               "locationId=" + locationId +
               ", coordinateX=" + coordinateX +
               ", coordinateY=" + coordinateY +
               '}';
    }
}

