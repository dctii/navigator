package com.solvd.navigator.bin;

import java.util.Objects;

public class Storage {
    private int storageId;
    private String name;
    private int locationId;

    public Storage() {

    }

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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Storage storage = (Storage) obj;
        return storageId == storage.storageId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(storageId);
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
