package com.solvd.navigator.service.impl;

import com.solvd.navigator.bin.Location;
import com.solvd.navigator.service.DriverService;

import java.util.List;

public class DriverServiceImpl implements DriverService {
    private final DriverRepository driverRepository;

    public DriverServiceImpl(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public List<Location> getDriverRoute(int driverId) {

        return driverRepository.getDriverRoute(driverId);
    }

    @Override
    public void updateDriverLocation(int driverId, Location newLocation) {

        driverRepository.updateDriverLocation(driverId, newLocation);
    }
}
