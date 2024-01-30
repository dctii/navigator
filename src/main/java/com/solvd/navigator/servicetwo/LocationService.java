package com.solvd.navigator.servicetwo;

import com.solvd.navigator.bin.Location;
import com.solvd.navigator.bin.Storage;
import com.solvd.navigator.math.RoutePlan;

import java.util.List;
import java.util.Set;

public interface LocationService {
    Location getLocationById(int locationId);
    Location getOriginLocation(RoutePlan routePlan);
    Location getTerminalLocation(RoutePlan routePlan);
    Location getStorageLocation(Storage storage);
    List<Location> getAllLocationsFromFile(String resourcePath);

    List<Location> getAvailableLocationsFromAll(String resourcePath, Set<Integer> excludedLocationIds);

}
