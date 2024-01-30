package com.solvd.navigator.servicetwo.impl;

import com.solvd.navigator.bin.Driver;
import com.solvd.navigator.bin.Location;
import com.solvd.navigator.dao.DriverDAO;
import com.solvd.navigator.dao.LocationDAO;
import com.solvd.navigator.dao.StorageDAO;
import com.solvd.navigator.math.RouteCalculatorImpl;
import com.solvd.navigator.math.RoutePlan;
import com.solvd.navigator.math.graph.GraphConstants;
import com.solvd.navigator.math.graph.GraphFactory;
import com.solvd.navigator.math.graph.GraphType;
import com.solvd.navigator.math.graph.ShortestPathsMatrix;
import com.solvd.navigator.math.graph.WeightedGraph;
import com.solvd.navigator.math.util.JsonDataStore;
import com.solvd.navigator.math.util.OrderConstants;
import com.solvd.navigator.servicetwo.DriverService;
import com.solvd.navigator.util.AnsiCodes;
import com.solvd.navigator.util.ClassConstants;
import com.solvd.navigator.util.DAOFactory;
import com.solvd.navigator.util.NumberUtils;
import com.solvd.navigator.util.StringConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DriverServiceImpl implements DriverService {
    private static final Logger LOGGER = LogManager.getLogger(DriverServiceImpl.class);
    private final DriverDAO driverDAO = DAOFactory.createDAO(ClassConstants.DRIVER_DAO);
    private final LocationDAO locationDAO = DAOFactory.createDAO(ClassConstants.LOCATION_DAO);
    private final StorageDAO storageDAO = DAOFactory.createDAO(ClassConstants.STORAGE_DAO);
    private final WeightedGraph graph = (WeightedGraph) GraphFactory.createGraph(
            GraphType.WEIGHTED_UNDIRECTED,
            GraphConstants.COORDINATE_MAX
    );

    RouteCalculatorImpl routeCalculator = new RouteCalculatorImpl.Builder()
            .setLocations(JsonDataStore.allLocations)
            .setStorages(storageDAO.getAll())
            .buildShortestPathsMatrix(graph)
            .build();

    @Override
    public Driver getDriverById(int driverId) {
        return driverDAO.getById(driverId);
    }

    @Override
    public RoutePlan buildDriverRoute(Driver driver, Location originLocation, List<Location> orderLocations) {
        RoutePlan driverRoute = new RoutePlan.Builder()
                .setOriginLocation(originLocation)
                .setDeliveryLocations(orderLocations)
                .calculateRouteDetails(routeCalculator)
                .build();

        // Update the `terminalLocation`, which is the nearest storage Location, and the new starting location for the next route,
        // so it also has coordinates because it only has a locationId as its Location*

        // update terminalLocation field
        Location terminalLocationWithCoordinates = locationDAO.getById(driverRoute.getTerminalLocation().getLocationId());
        driverRoute.setTerminalLocation(terminalLocationWithCoordinates);

        // update last location in `route` field
        int lastIndex = driverRoute.getRoute().size() - 1;
        driverRoute.getRoute().set(lastIndex, terminalLocationWithCoordinates);

        LOGGER.info("{}'Driver {}' embarking on delivery journey, RoutePlan #{} {} --> Departing from 'Location {} ({}, {})' " +
                        "{} --> Total Route Distance: {} km;" +
                        "{} --> Total Route Minutes: {} minutes" +
                        "{} --> Total Number of Orders: {} orders" +
                        "{} --> Total Locations on Route: {} locations {}"
                ,
                StringConstants.NEWLINE + StringConstants.NEWLINE + AnsiCodes.MAGENTA + AnsiCodes.BOLD,
                driver.getDriverId(),
                driverRoute.getId(),
                StringConstants.NEWLINE, originLocation.getLocationId(), originLocation.getCoordinateX(), originLocation.getCoordinateY(),
                StringConstants.NEWLINE, NumberUtils.roundToScale(driverRoute.getTotalDistance(), 2),
                StringConstants.NEWLINE, NumberUtils.roundToScale(driverRoute.getTotalRouteMinutes(), 2),
                StringConstants.NEWLINE, orderLocations.size(),
                StringConstants.NEWLINE, driverRoute.getRoute().size(), // if smaller than awaiting orders, means some orders are at one location
                AnsiCodes.RESET_ALL
        );

        LOGGER.info("{} --> Sequence of Deliveries for RoutePlan:{} ",
                AnsiCodes.MAGENTA + AnsiCodes.BOLD,
                AnsiCodes.RESET_ALL
        );
        driverRoute.getRoute().forEach(location -> {
            LOGGER.info("{} ----> 'Location {} ({}, {})';{}",
                    AnsiCodes.MAGENTA,
                    location.getLocationId(), location.getCoordinateX(), location.getCoordinateY(),
                    AnsiCodes.RESET_ALL
            );
        });

        return driverRoute;
    }

    @Override
    public List<Location> buildPath(Location fromLocation, Location toLocation) {
        ShortestPathsMatrix matrix = routeCalculator.getShortestPathsMatrix();


        Map<Integer, Integer> idToIndexMap = matrix.getLocationIdToIndexMap();

        List<Integer> pathOfLocationIds = matrix.getPath(fromLocation.getLocationId(), toLocation.getLocationId());
        List<Location> pathOfLocations = new ArrayList<>();

        pathOfLocationIds.forEach(locationId -> {
            Location locationOnList = locationDAO.getById(locationId);
            pathOfLocations.add(locationOnList);
        });

        return pathOfLocations;
    }


    @Override
    public double getDistanceBetweenSequentialLocations(RoutePlan routePlan, int locationToIndex) {
        Location departingLocation = routePlan.getRoute().get(locationToIndex - 1);
        Location arrivingLocation = routePlan.getRoute().get(locationToIndex);

        return NumberUtils.roundToScale(
                routePlan.getDistanceBetweenLocations(
                        routeCalculator,
                        departingLocation,
                        arrivingLocation
                ), 2);
    }

    @Override
    public double getTimeBetweenSequentialLocations(RoutePlan routePlan, int locationToIndex) {
        double distanceBetweenLocations = getDistanceBetweenSequentialLocations(routePlan, locationToIndex);

        return NumberUtils.roundToScale(
                distanceBetweenLocations * OrderConstants.MINUTES_PER_KM,
                2
        );
    }


    @Override
    public void addRoutePlanToDriver(Driver driver, RoutePlan routePlan) {
        driver.addRoutePlanToList(routePlan);
    }


    @Override
    public double calculateTotalDistanceTravelled(Driver driver) {
        return NumberUtils.roundToScale(
                driver.getRoutePlans().stream()
                        .mapToDouble(RoutePlan::getTotalDistance)
                        .sum(),
                2);
    }

    @Override
    public double calculateTotalTimeSpent(Driver driver) {
        return driver.getRoutePlans().stream()
                .mapToDouble(RoutePlan::getTotalRouteMinutes)
                .sum();
    }

    @Override
    public int getTotalAmountOfRoutes(Driver driver) {
        return driver.getRoutePlans().size();
    }

    @Override
    public int calculateTotalDeliveries(Driver driver) {
        return getTotalAmountOfRoutes(driver) * OrderConstants.DRIVER_ORDER_LIMIT;
    }
}
