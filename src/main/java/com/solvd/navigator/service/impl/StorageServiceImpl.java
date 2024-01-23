package com.solvd.navigator.service.impl;

import com.solvd.navigator.bin.Location;
import com.solvd.navigator.bin.Storage;
import com.solvd.navigator.service.LocationService;
import com.solvd.navigator.service.StorageService;

import java.util.ArrayList;
import java.util.List;

public abstract class StorageServiceImpl implements StorageService {

    private StorageService storageService;
    private LocationService locationService;

    public StorageServiceImpl(StorageService storageService, LocationService locationService) {
        this.storageService = storageService;
        this.locationService = locationService;

    }


    @Override
    public Location getStorageLocation(int storageId) {
        Storage storage = storageService.findById(storageId);
        return (storage != null) ? locationService.getLocationById(storage.getLocationId()) : null;
    }

    @Override
    public List<Location> getOptimalRouteToStorage(int storageId, Location destination) {
        Storage storage = storageService.findById(storageId);
        Location storageLocation = (storage != null) ? locationService.getLocationById(storage.getLocationId()) : null;


        List<Location> optimalRoute = new ArrayList<>();
        if (storageLocation != null) {

            // routing algorithm

            optimalRoute.add(storageLocation);
            optimalRoute.add(destination);
        }

        return optimalRoute;
    }
}
