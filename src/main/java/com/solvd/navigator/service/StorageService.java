package com.solvd.navigator.service;

import com.solvd.navigator.bin.Item;
import com.solvd.navigator.bin.Location;
import com.solvd.navigator.bin.Storage;

import java.util.List;

public interface StorageService {
    Storage findById(int storageId);

    List<Storage> findAll();


    List<Item> findItemsInStorage(int storageId);

    Item findItemInStorage(int storageId, int itemId);

    Location getStorageLocation(int storageId);

    List<Location> getOptimalRouteToStorage(int storageId, Location destination);
}
