package com.solvd.navigator.bin;

public class Storage {
    private int storageId;
    private String name;
    private int locationId;

    public Storage(int storageId, String name, int locationId) {
        this.storageId = storageId;
        this.name = name;
        this.locationId = locationId;
    }

    public int getStorageId() {
        return storageId;
    }

    public void setStorageId(int storageId) {
        this.storageId = storageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    @Override
    public String toString() {
        return "Storages{" +
               "storageId=" + storageId +
               ", name='" + name + '\'' +
               ", locationId=" + locationId +
               '}';
    }

}
