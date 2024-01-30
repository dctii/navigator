package com.solvd.navigator.util;

import com.solvd.navigator.bin.Driver;
import com.solvd.navigator.bin.Location;
import com.solvd.navigator.bin.Order;
import com.solvd.navigator.bin.Storage;
import com.solvd.navigator.math.RoutePlan;
import com.solvd.navigator.math.util.OrderConstants;
import com.solvd.navigator.servicetwo.DriverService;
import com.solvd.navigator.servicetwo.LocationService;
import com.solvd.navigator.servicetwo.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DriverUtils {
    private static final Logger LOGGER = LogManager.getLogger(DriverUtils.class);

    public static void announceDriverCheckIn(DriverConnection connection) {
        LOGGER.info(
                "{}Employee checked in. Work day starting for {}.{}",
                AnsiCodes.GREEN + AnsiCodes.BOLD,
                StringFormatters.nestInSingleQuotations(connection.getDriverName()),
                AnsiCodes.RESET_ALL
        );
    }

    public static List<Order> pickUpAwaitingOrders(Driver driver, OrderService orderService,
                                                   Storage originStorage, int orderLimit) {
        List<Order> awaitingOrders = orderService.getAwaitingOrdersFromStorage(
                originStorage,
                orderLimit
        );

        IntStream.range(0, awaitingOrders.size())
                .forEach(i -> {
                    Order orderToUpdate = awaitingOrders.get(i);
                    orderService.updateOrderPickedUp(orderToUpdate, driver);
                });

        return awaitingOrders;
    }

    public static List<Location> getLocations(OrderService orderService, List<Order> awaitingOrders) {
        return orderService.getLocationsListForOrders(awaitingOrders);
    }

    public static RoutePlan buildRoutePlan(
            Driver driver,
            DriverService driverService,
            Location originLocation,
            List<Location> orderLocations
    ) {
        RoutePlan driverRoute;
        driverRoute = driverService.buildDriverRoute(driver, originLocation, orderLocations);
        driverService.addRoutePlanToDriver(driver, driverRoute);
        return driverRoute;
    }

    public static void deliverOrders(
            Driver driver,
            RoutePlan driverRoute,
            DriverService driverService,
            List<Order> awaitingOrders,
            OrderService orderService
    ) {
        // start at 1, because it is after the terminal location
        // end at `routePlan.getRoute().size() - 1` because the last item is the terminal location
        int firstOrderLocationIndex = 1;
        int lastOrderLocationIndex = driverRoute.getRoute().size() - 1;
        IntStream.range(firstOrderLocationIndex, lastOrderLocationIndex)
                .forEach(i -> {
                    // 3.1 Get departing and arriving locations; get distance and time between locations
                    Location departingLocation = driverRoute.getRoute().get(i - 1);
                    Location arrivingLocation = driverRoute.getRoute().get(i);
                    double distanceBetweenLocations =
                            driverService.getDistanceBetweenSequentialLocations(driverRoute, i);
                    double timeBetweenLocations =
                            driverService.getTimeBetweenSequentialLocations(driverRoute, i);

                    // departure message
                    announceDepartureToNextRouteLocation(driver, departingLocation, arrivingLocation, distanceBetweenLocations, timeBetweenLocations);

                    // 3.2 Get orders for next location, notify them as delivered
                    // filter locations because sometimes more than one order is at a single
                    // location
                    List<Order> ordersForNextLocation =
                            getOrdersSharingLocation(awaitingOrders, orderService, arrivingLocation);

                    // emulate path from departing location to arriving location
                    emulateDrivingOnPath(driver, driverService, departingLocation, arrivingLocation);

                    // update order information after arriving
                    updateOrdersAsDelivered(orderService, ordersForNextLocation);
                });
    }

    private static void updateOrdersAsDelivered(OrderService orderService, List<Order> ordersForNextLocation) {
        ordersForNextLocation.forEach(order ->
                orderService.updateOrderDelivered(
                        order,
                        new Timestamp(System.currentTimeMillis()))
        );
    }

    private static void emulateDrivingOnPath(Driver driver, DriverService driverService, Location departingLocation, Location arrivingLocation) {
        List<Location> currPath = driverService.buildPath(departingLocation, arrivingLocation);
        currPath.forEach(pathLocation -> {
            LOGGER.info(
                    "{}'Driver {}' at 'Location {} ({}, {})'{}",
                    // make non-storage locations MAGENTA, make departing RED,
                    // arriving GREEN
                    !pathLocation.equals(arrivingLocation)
                            ? !pathLocation.equals(departingLocation)
                            ? AnsiCodes.MAGENTA
                            : AnsiCodes.RED + AnsiCodes.BOLD
                            : AnsiCodes.GREEN + AnsiCodes.BOLD,
                    driver.getDriverId(),
                    pathLocation.getLocationId(),
                    pathLocation.getCoordinateX(),
                    pathLocation.getCoordinateY(),
                    AnsiCodes.RESET_ALL
            );
        });
    }

    private static void announceDepartureToNextRouteLocation(Driver driver, Location departingLocation, Location arrivingLocation, double distanceBetweenLocations, double timeBetweenLocations) {
        LOGGER.info(
                "{}{}'Driver {}' Departing from 'Location {} ({}, {})' to 'Location {} ({}, {})'.{} {} --> Distance: {} km. {} --> Time: {} minutes.",
                StringConstants.NEWLINE,
                AnsiCodes.YELLOW,
                driver.getDriverId(),
                departingLocation.getLocationId(), departingLocation.getCoordinateX(), departingLocation.getCoordinateY(),
                arrivingLocation.getLocationId(), arrivingLocation.getCoordinateX(), arrivingLocation.getCoordinateY(),
                AnsiCodes.RESET_ALL,
                StringConstants.NEWLINE,
                distanceBetweenLocations,
                StringConstants.NEWLINE,
                timeBetweenLocations
        );
    }

    private static List<Order> getOrdersSharingLocation(List<Order> awaitingOrders, OrderService orderService, Location arrivingLocation) {
        return awaitingOrders.stream()
                .filter(
                        order -> {
                            // retain orders if they have the same location as the
                            // next arriving location
                            Location currOrderLocation = orderService.getOrderLocation(order);
                            return arrivingLocation.equals(currOrderLocation);
                        })
                .collect(Collectors.toList());
    }

    public static void declareEndOfRoute(
            Driver driver,
            Storage originStorage,
            RoutePlan driverRoute,
            LocationService locationService
    ) {
        Location originStorageLocation =
                locationService.getStorageLocation(originStorage);

        LOGGER.info("{}{}{}All packages for RoutePlan ID# {} have been delivered by 'Driver {}'{}. {} --> Going to the nearest storage, '{}' at 'Location {} ({}, {})'{}",
                StringConstants.NEWLINE,
                AnsiCodes.GREEN,
                AnsiCodes.BOLD,
                driverRoute.getId(),
                driver.getDriverId(),
                AnsiCodes.RESET_ALL,
                StringConstants.NEWLINE,
                originStorage.getName(),
                originStorageLocation.getLocationId(), originStorageLocation.getCoordinateX(), originStorageLocation.getCoordinateY(),
                StringConstants.NEWLINE
        );
    }

    public static void announceDriverDayStats(Driver driver, DriverService driverService, double totalMinutesDelivering) {
        double totalDistanceTravelled = driverService.calculateTotalDistanceTravelled(driver);
        double overtimeInMinutes = NumberUtils.roundToScale(totalMinutesDelivering - OrderConstants.MAX_WORK_HOURS_IN_MINUTES, 2);
        int amountOfRoutesCompleted = driver.getRoutePlans().size();
        int amountOfOrdersDelivered = amountOfRoutesCompleted * 8;

        LOGGER.info("{}'Driver {}' -- {} hour workday fulfilled.{}" +
                        "--> Overtime in Minutes: {}{}" +
                        "{}Total minutes spent delivering: {} minutes{}" +
                        "Total routes completed: {} routes{}" +
                        "Total orders delivered: {} orders delivered{}" +
                        "Total distance travelled: {} km{}",
                // driver info and hours worked
                StringConstants.NEWLINE + AnsiCodes.GREEN + AnsiCodes.BOLD, driver.getDriverId(), OrderConstants.MAX_WORK_HOURS, AnsiCodes.RESET_ALL + StringConstants.NEWLINE,
                overtimeInMinutes, StringConstants.NEWLINE,
                // Total minutes spent delivering
                AnsiCodes.BLUE + AnsiCodes.BOLD, NumberUtils.roundToScale(totalMinutesDelivering, 2), StringConstants.NEWLINE,
                // total orders delivered
                amountOfRoutesCompleted, StringConstants.NEWLINE,
                // total orders delivered
                amountOfOrdersDelivered, StringConstants.NEWLINE,
                // total distance travelled
                totalDistanceTravelled, AnsiCodes.RESET_ALL
        );
    }

    private DriverUtils() {
        ExceptionUtils.preventUtilityInstantiation();
    }
}
