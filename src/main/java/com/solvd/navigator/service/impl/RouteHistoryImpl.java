package com.solvd.navigator.service.impl;

import com.solvd.navigator.bin.Location;
import com.solvd.navigator.bin.Order;
import com.solvd.navigator.bin.Storage;
import com.solvd.navigator.dao.*;
import com.solvd.navigator.math.RouteCalculator;
import com.solvd.navigator.math.RoutePlan;
import com.solvd.navigator.math.util.OrderConstants;
import com.solvd.navigator.service.RouteService;
import com.solvd.navigator.util.ClassConstants;
import com.solvd.navigator.util.DAOFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;


public class RouteHistoryImpl implements RouteService {
    private static final Logger LOGGER = LogManager.getLogger(RouteHistoryImpl.class);
    private final RoutePlan routePlan;
    DriverDAO driverDAO = DAOFactory.createDAO(ClassConstants.DRIVER_DAO);
    LocationDAO locationDAO = DAOFactory.createDAO(ClassConstants.LOCATION_DAO);
    OrderDAO orderDAO = DAOFactory.createDAO(ClassConstants.ORDER_DAO);
    OrderRecipientDAO orderRecipientDAO = DAOFactory.createDAO(ClassConstants.ORDER_RECIPIENT_DAO);
    StorageDAO storageDAO = DAOFactory.createDAO(ClassConstants.STORAGE_DAO);

    public RouteHistoryImpl(RoutePlan routePlan, StorageDAO storageDAO, OrderDAO orderDAO, LocationDAO locationDAO, OrderRecipientDAO orderRecipientDAO, DriverDAO driverDAO) {
        this.routePlan = routePlan;
        this.storageDAO = storageDAO;
        this.orderDAO = orderDAO;
        this.locationDAO = locationDAO;
        this.orderRecipientDAO = orderRecipientDAO;
        this.driverDAO = driverDAO;
    }

    @Override
    public List<RoutePlan> getRouteHistory() {
        return routePlan.getRoutePlan();
    }


    public DriverStatistics getDriverStatistics(int driverId) {
        List<RoutePlan> routesForDriver = routePlan.getRoutePlan().stream()
                .filter(plan -> plan.getDriverId() == driverId)
                .collect(Collectors.toList());

        double totalDistance = routesForDriver.stream().mapToDouble(RoutePlan::getTotalDistance).sum();

        double totalDeliveryTime = routesForDriver.stream().mapToDouble(RoutePlan::getTotalRouteMinutes).sum();

        return new DriverStatistics(driverId, totalDistance, totalDeliveryTime);
    }

    public void processRoutes() {
        List<Storage> allStorages = storageDAO.getAll();

        for (RoutePlan routePlan : routePlan.getRoutePlan()) {
            int terminalLocationId = routePlan.getTerminalLocation().getLocationId();
            Storage nextStorage = storageDAO.getStorageByLocationId(terminalLocationId);

            List<Order> newOrdersFromNextStorage = orderDAO.getLimitedAwaitingOrdersByStorageId(nextStorage.getStorageId(), 8);


            for (Order order : newOrdersFromNextStorage) {

                LOGGER.info("Picking up order {} from storage {}", order.getOrderId(), nextStorage.getStorageId());


                order.setOrderStatus(OrderConstants.ORDER_STATUS_IN_TRANSIT);
                orderDAO.update(order);


                LOGGER.info("Delivering order {} to location {}", order.getOrderId(), order.getDeliveryLocation().toString());


                order.setOrderStatus(OrderConstants.ORDER_STATUS_DELIVERED);
                orderDAO.update(order);
            }

        }
    }

    public void processNewOrdersAndMoveToNextStorage(Storage originStorage, RouteCalculator routeCalculator) {


        int driverId = 1; // Assuming driverId=1 for this example
        double totalMinutesDelivering = 0.0;

        do {
            //  Get orders from the current storage
            List<Order> awaitingOrders = orderDAO.getLimitedAwaitingOrdersByStorageId(originStorage.getStorageId(), OrderConstants.DRIVER_ORDER_LIMIT);

            if (awaitingOrders.isEmpty()) {
                LOGGER.info("No more awaiting orders at this storage.");
                break;
            }

            // Update order statuses to "In Transit"
            LOGGER.info("Updating order statuses in DB from '{}' to '{}'", OrderConstants.ORDER_STATUS_AWAITING_DELIVERY, OrderConstants.ORDER_STATUS_IN_TRANSIT);

            awaitingOrders.forEach(order -> {
                order.setOrderStatus(OrderConstants.ORDER_STATUS_IN_TRANSIT);
                order.setDriverId(driverId); // Assign the driver to the order
                orderDAO.update(order);
            });

            // Build route plan for the current set of orders
            List<Location> orderLocations = awaitingOrders.stream().map(order -> locationDAO.getById(orderRecipientDAO.getById(order.getOrderRecipientId()).getLocationId())).collect(Collectors.toList());

            RoutePlan routePlan = new RoutePlan.Builder().setOriginLocation(locationDAO.getById(originStorage.getLocationId())).setDeliveryLocations(orderLocations).calculateRouteDetails(routeCalculator).build();

            LOGGER.info("Route plan built successfully");
            routePlan.printRoute();

            //  Update order statuses to "Delivered"
            LOGGER.info("Updating order statuses in DB from '{}' to '{}'", OrderConstants.ORDER_STATUS_IN_TRANSIT, OrderConstants.ORDER_STATUS_DELIVERED);

            awaitingOrders.forEach(order -> {
                order.setOrderStatus(OrderConstants.ORDER_STATUS_DELIVERED);
                orderDAO.update(order);
            });

            // Update originStorage for the next iteration
            double minDistance = Double.MAX_VALUE;
            Storage nearestStorage = null;

            for (Storage storage : storageDAO.getAll()) {
                double distance = routeCalculator.calculateDistance(originStorage.getLocation(), storage.getLocation());
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestStorage = storage;
                }
            }

            // Update `originStorage` to the nearest storage
            originStorage = nearestStorage;

            // Log the information
            LOGGER.info("Moving to the nearest storage: {}", originStorage.toString());

            // Add to how many minutes it has been delivering
            totalMinutesDelivering += routePlan.getTotalRouteMinutes();

        } while (totalMinutesDelivering <= OrderConstants.MAX_WORK_HOURS_IN_MINUTES);

        LOGGER.info("Total minutes spent delivering: {} minutes", totalMinutesDelivering);
    }}
