package com.solvd.navigator.servicetwo;

import com.solvd.navigator.bin.Driver;
import com.solvd.navigator.bin.Location;
import com.solvd.navigator.math.RoutePlan;

import java.util.List;

public interface DriverService {
    Driver getDriverById(int driverId);

    RoutePlan buildDriverRoute(Driver driver, Location originLocation, List<Location> orderLocations);

    List<Location> buildPath(Location fromLocation, Location toLocation);

    void addRoutePlanToDriver(Driver driver, RoutePlan routePlan);

    double calculateTotalDistanceTravelled(Driver driver);

    double calculateTotalTimeSpent(Driver driver);

    int getTotalAmountOfRoutes(Driver driver);

    int calculateTotalDeliveries(Driver driver);

    double getDistanceBetweenSequentialLocations(RoutePlan routePlan, int locationToIndex);

    double getTimeBetweenSequentialLocations(RoutePlan routePlan, int locationToIndex);
}
