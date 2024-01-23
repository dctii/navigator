package com.solvd.navigator;

import com.solvd.navigator.bin.Driver;
import com.solvd.navigator.bin.Location;
import com.solvd.navigator.bin.Order;
import com.solvd.navigator.bin.Storage;
import com.solvd.navigator.math.RouteCalculatorImpl;
import com.solvd.navigator.math.RoutePlan;
import com.solvd.navigator.math.graph.GraphConstants;
import com.solvd.navigator.math.graph.GraphFactory;
import com.solvd.navigator.math.graph.GraphType;
import com.solvd.navigator.math.graph.WeightedGraph;
import com.solvd.navigator.math.util.JsonDataStore;
import com.solvd.navigator.math.util.OrderConstants;
import com.solvd.navigator.util.AnsiCodes;
import com.solvd.navigator.util.FilepathConstants;
import com.solvd.navigator.util.JacksonUtils;
import com.solvd.navigator.util.ScannerUtils;
import com.solvd.navigator.util.StringConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;


/* TODO:
    - need to implement the Floyd-Warshall Algorithm and find the most efficient route -- maybe ned to use the Travelling Salesman Problem
        - use GraphUtils.mapLocationIdToVertexId which uses GraphUtils.findMatchingVertexId to map locationId to the map.
        - need to see if need to populate FloydWarshallAlgorithm.class and RouteResult.class

*/


public class GraphTester {
    private static final Logger LOGGER = LogManager.getLogger(GraphTester.class);

    public static void main(String[] args) {


        // generate graph
        WeightedGraph graph = (WeightedGraph) GraphFactory.createGraph(
                GraphType.WEIGHTED_UNDIRECTED,
                GraphConstants.COORDINATE_MAX
        );

        RouteCalculatorImpl routeCalculator = new RouteCalculatorImpl.Builder()
                .setLocations(JsonDataStore.allLocations)
                .setStorages(JsonDataStore.allStorages)
                .buildShortestPathsMatrix(graph)
                .build();


        Scanner scanner = new Scanner(System.in);
        int option = -1;

        while (option != 0) {
            LOGGER.info("Menu:");
            LOGGER.info("[1] Print Graph Vertices and their Edges");
            LOGGER.info("[2] Print Shortest Distances Generated by Floyd-Warshall Algorithm");
            LOGGER.info("[3] Get the Fastest Routes and update orders with routes to 'Delivered'");
            LOGGER.info("[0] Exit");

            String input = ScannerUtils.getInput(scanner, "Enter your choice: ");
            try {
                option = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                LOGGER.error("Invalid input.");
                continue;
            }

            switch (option) {
                case 1:
                    graph.printGraph();
                    break;
                case 2:
                    routeCalculator.getShortestPathsMatrix().printShortestDistances(graph);
                    break;
                case 3:
                    Storage originStorage;
                    Location initialLocation;
                    RoutePlan routePlan;
                    double totalMinutesDelivering = 0.0;
                    List<Order> allCurrentOrders = JacksonUtils.extractItems(FilepathConstants.ORDERS_JSON, Order.class);

                    // 1. Initial storage that the driver begins at
                    originStorage = JacksonUtils.getRandomStorage(JsonDataStore.allStorages);

                    do {
                        // Get orders from storage location
                        // get Storage and the storage's corresponding Location. So we know where the starting point of the route is
                        initialLocation = JacksonUtils.getLocationByStorage(
                                originStorage,
                                JsonDataStore.allLocations
                        );


                        // 2. Pick up the orders
                        List<Order> awaitingOrders = JacksonUtils.getAwaitingOrdersFromStorage(
                                allCurrentOrders,
                                originStorage,
                                OrderConstants.DRIVER_ORDER_LIMIT
                        );

                        if (awaitingOrders.isEmpty()) {
                            LOGGER.info("No more awaiting orders at this storage.");
                            break;
                        }

                        // Change order status to "In Transit"

                        LOGGER.info(StringConstants.NEWLINE);
                        LOGGER.info("Updating order statuses in JSON from '{}' to '{}'",
                                OrderConstants.ORDER_STATUS_AWAITING_DELIVERY,
                                OrderConstants.ORDER_STATUS_IN_TRANSIT
                        );


                        IntStream.range(0, awaitingOrders.size())
                                .forEach(i -> {
                                    Order orderToUpdate = awaitingOrders.get(i);
                                    orderToUpdate.setOrderStatus(OrderConstants.ORDER_STATUS_DELIVERED);
                                    // update the specific order in the JSON file
                                    JacksonUtils.updateOrderInJsonById(FilepathConstants.ORDERS_JSON, orderToUpdate);
                                });


                        // Get Locations for orders
                        List<Location> orderLocations = JacksonUtils.extractAwaitingOrderLocationsForRoute(
                                originStorage,
                                awaitingOrders,
                                JsonDataStore.allOrderRecipients,
                                JsonDataStore.allLocations
                        );

                        // Set the driver of the orders to drive with driverId=1
                        Driver driverOne = JsonDataStore.allDrivers.get(0);

                        LOGGER.info("Updating driverId for picked up orders in JSON from '0' to '{}'", driverOne.getDriverId());


                        IntStream.range(0, awaitingOrders.size())
                                .forEach(i -> {
                                    Order orderToUpdate = awaitingOrders.get(i);
                                    orderToUpdate.setDriverId(driverOne.getDriverId());
                                    JacksonUtils.updateOrderInJsonById(FilepathConstants.ORDERS_JSON, orderToUpdate);
                                });


                        // 3. Plan the route and "deliver the packages"
                        routePlan = new RoutePlan.Builder()
                                .setOriginLocation(initialLocation)
                                .setDeliveryLocations(orderLocations)
                                .calculateRouteDetails(routeCalculator)
                                .build();

                        // Update the `terminalLocation`, which is the nearest storage Location, and the new starting location for the next route,
                        // so it also has coordinates because it only has a locationId as its Location*
                        // * because we need to query the `locations.json` to get the data since the `storage.json` only has the storages' locationId
                        routePlan.setTerminalLocation(
                                JacksonUtils.getLocationById(
                                        routePlan.getTerminalLocation().getLocationId(),
                                        JsonDataStore.allLocations
                                )
                        );

                        // 4. Print out the route
                        routePlan.printRoute();
                        LOGGER.info("{}Total Distance: {} km{}",
                                AnsiCodes.GREEN,
                                routePlan.getTotalDistance(),
                                AnsiCodes.RESET_ALL
                        );
                        LOGGER.info("{}Total Travel Time in Minutes: {} minutes{}",
                                AnsiCodes.GREEN,
                                routePlan.getTotalRouteMinutes(),
                                AnsiCodes.RESET_ALL
                        );

                        // 5. Consider the packages "delivered", so update the JSON and set all from "Awaiting Delivery" to "Delivered"
                        LOGGER.info(StringConstants.NEWLINE);
                        LOGGER.info("Updating order statuses in JSON from '{}' to '{}'",
                                OrderConstants.ORDER_STATUS_IN_TRANSIT,
                                OrderConstants.ORDER_STATUS_DELIVERED
                        );


                        IntStream.range(0, awaitingOrders.size())
                                .forEach(i -> {
                                    Order orderToUpdate = awaitingOrders.get(i);
                                    orderToUpdate.setOrderStatus(OrderConstants.ORDER_STATUS_DELIVERED);
                                    JacksonUtils.updateOrderInJsonById(FilepathConstants.ORDERS_JSON, orderToUpdate);
                                });


                        // 6. Repeat 1-5 with the Location terminalLocation as the next storage Location to start from

                        LOGGER.info(StringConstants.NEWLINE);
                        LOGGER.info("Next storage location: " + routePlan.getTerminalLocation().toString());
                        LOGGER.info(StringConstants.NEWLINE);
                        Storage newTargetStorage = JacksonUtils.getStorageByLocationId(
                                routePlan.getTerminalLocation().getLocationId(), JsonDataStore.allStorages
                        );
                        LOGGER.info(newTargetStorage.toString());

                        originStorage = JacksonUtils.getStorageByLocationId(
                                newTargetStorage.getLocationId(), JsonDataStore.allStorages
                        );

                        // add to how many minutes it has been delivering for
                        totalMinutesDelivering += routePlan.getTotalRouteMinutes();
                    } while (true);

                    LOGGER.info(
                            "Total minutes spent delivering: {} minutes",
                            totalMinutesDelivering
                    );

                    break;


                case 0:
                    LOGGER.info("Exiting program.");
                    break;
                default:
                    LOGGER.error("Invalid option.");
            }
        }

        scanner.close();
    }

}
