package com.solvd.navigator.service;

import com.solvd.navigator.bin.Location;

import java.util.List;

public interface LocationService {
    Location getLocationById(int locationId);

    double calculateDistance(Location source, Location destination);

    List<Location> getLocationsOnRoute(String routeId);

    Location getCurrentUserLocation();
}
