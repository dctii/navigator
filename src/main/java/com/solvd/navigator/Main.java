package com.solvd.navigator;

import com.solvd.navigator.bin.*;
import com.solvd.navigator.dao.*;

import com.solvd.navigator.math.RouteCalculator;
import com.solvd.navigator.math.RouteCalculatorImpl;
import com.solvd.navigator.math.RoutePlan;

import com.solvd.navigator.math.graph.WeightedGraph;
import com.solvd.navigator.math.util.JsonDataStore;
import com.solvd.navigator.math.util.OrderConstants;
import com.solvd.navigator.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.solvd.navigator.math.util.JsonDataStore.allStorages;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private static OrderRecipientDAO orderRecipientDAO;
    private static StorageDAO storageDAO;
    private static LocationDAO locationDAO;
    private static WeightedGraph graph;
    private static DriverDAO driverDAO;
    private static OrderDAO orderDAO;
    
    public static void main(String[] args) {


        DriverDAO driverDAO = DAOFactory.createDAO(ClassConstants.DRIVER_DAO);
        locationDAO = DAOFactory.createDAO(ClassConstants.LOCATION_DAO);
        OrderDAO orderDAO = DAOFactory.createDAO(ClassConstants.ORDER_DAO);
        storageDAO = DAOFactory.createDAO(ClassConstants.STORAGE_DAO);
        orderRecipientDAO = DAOFactory.createDAO(ClassConstants.ORDER_RECIPIENT_DAO);


        // Get all storages
        List<Storage> allStorages = storageDAO.getAll();


        Scanner scanner = new Scanner(System.in);
        int option;
        do {
            printMenu();
            option = getUserChoice(scanner);

            switch (option) {
                case 1:
                    Driver driverOne = driverDAO.getById(1);
                    processDriverDay(driverOne, allStorages, orderDAO, locationDAO);
                    break;
                case 2:
                    generateRoutes(allStorages, orderDAO, locationDAO);
                    break;
                case 0:
                    LOGGER.info("Exiting program.");
                    break;
                default:
                    LOGGER.error("Invalid option. Please try again.");
            }

        } while (option != 0);

        scanner.close();
    }

    private static void printMenu() {
        LOGGER.info("Menu:");
        LOGGER.info("[1] Start Driver's Day");
        LOGGER.info("[2] Generate Routes");
        LOGGER.info("[0] Exit");

    }

    private static int getUserChoice(Scanner scanner) {
        LOGGER.info("Enter your choice: ");
        return scanner.nextInt();
    }

    private static void processDriverDay(Driver driver, List<Storage> allStorages, OrderDAO orderDAO, LocationDAO locationDAO) {
        int totalWorkingMinutes = 8 * 60;
        int currentMinutes = 0;


        // Main loop for the driver's working day
        while (currentMinutes < totalWorkingMinutes) {
            // Find the nearest storage
            Storage nearestStorage = findNearestStorage(driver.getCurrentLocation(), allStorages);

            // Get maximum 8 orders from the nearest storage
            List<Order> awaitingOrders = orderDAO.getLimitedAwaitingOrdersByStorageId(nearestStorage.getStorageId(), Math.min(OrderConstants.DRIVER_ORDER_LIMIT, totalWorkingMinutes - currentMinutes));

            // Update order statuses to "In Transit"
            updateOrderStatuses(orderDAO, awaitingOrders, OrderConstants.ORDER_STATUS_IN_TRANSIT);

            // Calculate route for the driver
            RoutePlan routePlan = calculateRoute(nearestStorage, awaitingOrders, locationDAO);

            // Deliver orders and update statuses
            deliverOrders(driver, orderDAO, routePlan);

            // Update current working time
            currentMinutes += routePlan.getTotalRouteMinutes();
        }

        LOGGER.info("Driver's 8-hour working day completed.");
    }

    private static void generateRoutes(List<Storage> allStorages, OrderDAO orderDAO, LocationDAO locationDAO) {
        int numCars = 2;
        int ordersPerCar = 8;
        int totalWorkingMinutes = 8 * 60;

        for (int carIndex = 0; carIndex < numCars; carIndex++) {

            // Creatingh a driver assuming driverId is unique
            Driver driver = driverDAO.getById(carIndex + 1);

            int currentMinutes = 0;
            while (currentMinutes < totalWorkingMinutes) {
                Storage nearestStorage = findNearestStorage(driver.getCurrentLocation(), allStorages);
                List<Order> awaitingOrders = orderDAO.getLimitedAwaitingOrdersByStorageId(nearestStorage.getStorageId(), Math.min(ordersPerCar, totalWorkingMinutes - currentMinutes));

                updateOrderStatuses(orderDAO, awaitingOrders, OrderConstants.ORDER_STATUS_IN_TRANSIT);

                RoutePlan routePlan = calculateRoute(nearestStorage, awaitingOrders, locationDAO);

                deliverOrders(driver, orderDAO, routePlan);

                currentMinutes += routePlan.getTotalRouteMinutes();
            }

            LOGGER.info("Car {}'s 8-hour working day completed.", carIndex + 1);
        }
    }



    // Method to find the nearest storage from the current location

    private static Storage findNearestStorage(Location currentLocation, List<Storage> allStorages) {
        return allStorages.stream().min(Comparator.comparingDouble(storage -> calculateDistance(currentLocation, locationDAO.getById(storage.getLocationId())))).orElse(null);


    }


    // Method to update order statuses
    private static void updateOrderStatuses(OrderDAO orderDAO, List<Order> orders, String newStatus) {
        for (Order order : orders) {
            order.setOrderStatus(newStatus);
            orderDAO.update(order);
        }
    }

    // Method to calculate route for the driver
    private static RoutePlan calculateRoute(Storage originStorage, List<Order> orders, LocationDAO locationDAO) {
        Location initialLocation = locationDAO.getById(originStorage.getLocationId());
        List<Location> orderLocations = orders.stream().map(order -> {
            OrderRecipient orderRecipient = orderRecipientDAO.getById(order.getOrderRecipientId());
            return locationDAO.getById(orderRecipient.getLocationId());
        }).collect(Collectors.toList());

        RouteCalculator routeCalculator = new RouteCalculatorImpl.Builder().setLocations(JsonDataStore.allLocations).setStorages(allStorages).buildShortestPathsMatrix(graph).build();

        RoutePlan routePlan = new RoutePlan.Builder().setOriginLocation(initialLocation).setDeliveryLocations(orderLocations).calculateRouteDetails(routeCalculator).build();

        LOGGER.info("Route plan built successfully");

        Location terminalLocationWithCoordinates = locationDAO.getById(routePlan.getTerminalLocation().getLocationId());
        routePlan.setTerminalLocation(terminalLocationWithCoordinates);

        LOGGER.info("{}Terminal location with coordinates{}{}", AnsiCodes.YELLOW, routePlan.getTerminalLocation().toString(), AnsiCodes.RESET_ALL);

        routePlan.printRoute();
        LOGGER.info("{}Total Distance: {} km{}", AnsiCodes.GREEN, routePlan.getTotalDistance(), AnsiCodes.RESET_ALL);
        LOGGER.info("{}Total Travel Time in Minutes: {} minutes{}", AnsiCodes.GREEN, routePlan.getTotalRouteMinutes(), AnsiCodes.RESET_ALL);

        LOGGER.info(StringConstants.NEWLINE);

        LOGGER.info("Updating order statuses in DB from '{}' to '{}'", OrderConstants.ORDER_STATUS_IN_TRANSIT, OrderConstants.ORDER_STATUS_DELIVERED);

        IntStream.range(0, orders.size()).forEach(i -> {
            Order orderToUpdate = orders.get(i);
            orderToUpdate.setOrderStatus(OrderConstants.ORDER_STATUS_DELIVERED);
            orderDAO.update(orderToUpdate);
        });

        LOGGER.info(StringConstants.NEWLINE);
        LOGGER.info("Next storage location: " + routePlan.getTerminalLocation().toString());
        LOGGER.info(StringConstants.NEWLINE);

        int terminalLocationId = routePlan.getTerminalLocation().getLocationId();
        Storage newTargetStorage = storageDAO.getStorageByLocationId(terminalLocationId);

        LOGGER.info(newTargetStorage.toString());

        return routePlan;
    }

    // Method to deliver orders and update statuses
    private static void deliverOrders(Driver driver, OrderDAO orderDAO, RoutePlan routePlan) {
        List<Order> deliveredOrders = new ArrayList<>();

        for (Location deliveryLocation : routePlan.getDeliveryLocations()) {
            Order order = getOrderForLocation(deliveryLocation, routePlan.getOrders());

            // Perform actions for order delivery
            order.setOrderStatus(OrderConstants.ORDER_STATUS_DELIVERED);
            order.setDeliveryTime(LocalDateTime.now());
            order.setDriverId(driver.getDriverId());

            // Update the order in the database
            orderDAO.update(order);

            // Add the delivered order to the list
            deliveredOrders.add(order);
        }

        LOGGER.info("Orders delivered successfully:");

        for (Order deliveredOrder : deliveredOrders) {
            LOGGER.info("- Order ID: {}, Delivered at: {}", deliveredOrder.getOrderId(), deliveredOrder.getDeliveryTime());
        }
    }

    private static Order getOrderForLocation(Location location, List<Order> orders) {
        // Find the order corresponding to the given delivery location
        return orders.stream().filter(order -> {
            OrderRecipient orderRecipient = orderRecipientDAO.getById(order.getOrderRecipientId());
            Location orderLocation = locationDAO.getById(orderRecipient.getLocationId());
            return orderLocation.equals(location);
        }).findFirst().orElse(null);
    }
}
