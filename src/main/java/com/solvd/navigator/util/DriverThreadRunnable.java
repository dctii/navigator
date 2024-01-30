package com.solvd.navigator.util;

import com.solvd.navigator.bin.Driver;
import com.solvd.navigator.bin.Location;
import com.solvd.navigator.bin.Order;
import com.solvd.navigator.bin.Storage;
import com.solvd.navigator.math.RoutePlan;
import com.solvd.navigator.math.util.OrderConstants;
import com.solvd.navigator.servicetwo.DriverService;
import com.solvd.navigator.servicetwo.LocationService;
import com.solvd.navigator.servicetwo.OrderListener;
import com.solvd.navigator.servicetwo.OrderService;
import com.solvd.navigator.servicetwo.StorageService;
import com.solvd.navigator.servicetwo.impl.DriverServiceImpl;
import com.solvd.navigator.servicetwo.impl.OrderListenerImpl;
import com.solvd.navigator.servicetwo.impl.OrderServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class DriverThreadRunnable implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(DriverThreadRunnable.class);
    private DriverConnectionPool driverConnectionPool;
    private LocationService locationService;
    private StorageService storageService;
    private Driver driver;

    public DriverThreadRunnable(
            DriverConnectionPool driverConnectionPool,
            LocationService locationService,
            StorageService storageService,
            Driver driver
    ) {
        this.driverConnectionPool = driverConnectionPool;
        this.locationService = locationService;
        this.storageService = storageService;
        this.driver = driver;
    }

    @Override
    public void run() {
        DriverConnection connection = driverConnectionPool.getConnection();

        try {

            DriverUtils.announceDriverCheckIn(connection);

            DriverService driverService = new DriverServiceImpl();
            OrderService orderService = new OrderServiceImpl();

            Storage originStorage = this.storageService.getRandomStorage();
            RoutePlan driverRoute;
            double totalMinutesDelivering = 0.0;


            do {
                /*
                    Register an order listener to log the status of orders
                    - onOrderPickedUp()
                    - onOrderDelivered()
                 */
                OrderListener orderListener = new OrderListenerImpl();
                orderService.registerOrderListener(orderListener);

                /* 1. Arrive at storage and pick up orders
                    - The driver will pick up a set amount of orders.
                    - The orders' status will be assigned to 'In Transit'
                    - The orders' driver will assigned to this.driver.getDriverId()
                 */
                List<Order> awaitingOrders = DriverUtils.pickUpAwaitingOrders(
                        this.driver,
                        orderService,
                        originStorage,
                        OrderConstants.DRIVER_ORDER_LIMIT
                );
                List<Location> orderLocations = // get the Location for each awaiting order to prepared to build the RoutePlan
                        orderService.getLocationsListForOrders(awaitingOrders);

                /* 2. Build the RoutePlan and add to the driver's List<RoutePlan>
                    RoutePlan driverRoute = new RoutePlan.Builder()
                        .setOriginLocation(originLocation)
                        .setDeliveryLocations(orderLocations)
                        .calculateRouteDetails(routeCalculator)
                        .build();
                 */
                driverRoute = // build the driver's RoutePlan and add to RoutePlan list of Driver
                        DriverUtils.buildRoutePlan(
                                this.driver,
                                driverService,
                                this.locationService.getStorageLocation(originStorage),
                                orderLocations
                        );

                /* 3. Deliver the awaiting orders
                    - Emulates delivery of the orders and announce
                    a point-by-point path from one location on the
                    route to another.
                    -

                 */
                DriverUtils.deliverOrders(
                        this.driver,
                        driverRoute,
                        driverService,
                        awaitingOrders,
                        orderService
                );

                // 4. Go to the nearest storage and update it as the next origin point for a route
                originStorage = // nearest storage was calculated as the terminal current RoutePlan
                        this.storageService.getTerminalStorage(driverRoute);
                DriverUtils.declareEndOfRoute( // announce all packages have been delivered
                        this.driver,
                        originStorage,
                        driverRoute,
                        locationService
                );

                // 5. Update the amount of minutes spent delivering
                totalMinutesDelivering += driverRoute.getTotalRouteMinutes();


                orderService.unregisterOrderListener(orderListener);
            } while (totalMinutesDelivering <= OrderConstants.MAX_WORK_HOURS_IN_MINUTES);

            /*
                Announce driver workday >= 8 hours and some stats:
                - overtime minutes,
                - total minutes delivering
                - number of routes completed
                - number of orders delivered
                - total distance travelled in km
             */
            DriverUtils.announceDriverDayStats(
                    this.driver,
                    driverService,
                    totalMinutesDelivering
            );

        } finally {
            driverConnectionPool.releaseConnection(connection);
        }
    }


    public DriverConnectionPool getDriverConnectionPool() {
        return driverConnectionPool;
    }

    public void setDriverConnectionPool(DriverConnectionPool driverConnectionPool) {
        this.driverConnectionPool = driverConnectionPool;
    }

    public LocationService getLocationService() {
        return locationService;
    }

    public void setLocationService(LocationService locationService) {
        this.locationService = locationService;
    }

    public StorageService getStorageService() {
        return storageService;
    }

    public void setStorageService(StorageService storageService) {
        this.storageService = storageService;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    @Override
    public String toString() {
        Class<?> currClass = DriverThreadRunnable.class;
        String[] fieldNames = {
                "driverConnectionPool",
                "locationService",
                "storageService",
                "driver"
        };

        String fieldsString =
                StringFormatters.buildFieldsString(this, fieldNames);

        return StringFormatters.buildToString(currClass, fieldNames, fieldsString);
    }
}
