package com.solvd.navigator.service;

import com.solvd.navigator.bin.Location;

import java.util.List;

public interface DriverService {

    List<Location> getDriverRoute(int driverId);

    void updateDriverLocation(int driverId, Location newLocation);


}
