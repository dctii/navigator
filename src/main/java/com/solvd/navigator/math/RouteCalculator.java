package com.solvd.navigator.math;

import com.solvd.navigator.bin.Location;
import com.solvd.navigator.math.graph.ShortestPathsMatrix;

import java.util.List;

public interface RouteCalculator {

    List<Location> findFastestRoute(
            Location startingLocation,
            List<Location> locationsForRoute
    );

    Location findNearestStorageLocation(
            Location lastLocation,
            ShortestPathsMatrix matrix
    );

    double calculateTotalDistance(List<Location> route);

    double getDistanceBetweenLocations(Location locationFrom, Location locationTo);

    double getDistanceBetweenLocations(int locationFromId, int locationToId);

}
