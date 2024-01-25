package com.solvd.navigator.math.util;

import com.solvd.navigator.bin.Location;
import com.solvd.navigator.bin.Storage;
import com.solvd.navigator.math.graph.ShortestPathsMatrix;
import com.solvd.navigator.math.graph.WeightedGraph;
import com.solvd.navigator.util.BooleanUtils;
import com.solvd.navigator.util.ClassConstants;
import com.solvd.navigator.util.ExceptionUtils;
import com.solvd.navigator.util.StringConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class RouteUtils {

    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.ROUTE_UTILS);

    public static List<Location> findFastestRoute(
            Location startingLocation,
            List<Location> locationsForRoute,
            List<Storage> storages,
            ShortestPathsMatrix matrix
    ) {
        double[][] shortestDistances = matrix.getShortestDistances();
        Map<Integer, Integer> locationToVertexIndexMap = matrix.getLocationToVertexIndexMap();

        List<Location> fastRoute = new ArrayList<>();
        Set<Integer> visitedLocationIds = new HashSet<>();

        int currentLocationId = startingLocation.getLocationId();
        fastRoute.add(startingLocation);
        visitedLocationIds.add(currentLocationId);

        while (fastRoute.size() <= locationsForRoute.size()) {
            double minDistance = Double.POSITIVE_INFINITY;
            Location closestLocation = null;
            int currentIndex = locationToVertexIndexMap.get(currentLocationId);

            for (Location nextLocation : locationsForRoute) {
                int nextLocationId = nextLocation.getLocationId();
                if (!visitedLocationIds.contains(nextLocationId)) {
                    int nextIndex = locationToVertexIndexMap.get(nextLocationId);
                    if (shortestDistances[currentIndex][nextIndex] < minDistance) {
                        minDistance = shortestDistances[currentIndex][nextIndex];
                        closestLocation = nextLocation;
                    }
                }
            }

            if (closestLocation != null) {
                visitedLocationIds.add(closestLocation.getLocationId());
                fastRoute.add(closestLocation);
                currentLocationId = closestLocation.getLocationId();
            }
        }

        // Find the nearest storage location after completing all deliveries
        Location lastDeliveryLocation = fastRoute.get(fastRoute.size() - 1);
        Location nearestStorageLocation = findNearestStorageLocation(lastDeliveryLocation, storages, matrix);

        if (nearestStorageLocation != null) {
            fastRoute.add(nearestStorageLocation);
        }

        return fastRoute;
    }

    public static Location findNearestStorageLocation(
            Location lastLocation,
            List<Storage> storages,
            ShortestPathsMatrix matrix
    ) {
        Map<Integer, Integer> locationToVertexIndexMap = matrix.getLocationToVertexIndexMap();
        double[][] shortestDistances = matrix.getShortestDistances();

        double minDistance = Double.POSITIVE_INFINITY;
        Location nearestStorageLocation = null;
        int lastDeliveryIndex = locationToVertexIndexMap.get(lastLocation.getLocationId());

        for (Storage storage : storages) {
            int storageIndex = locationToVertexIndexMap.get(storage.getLocationId());
            if (shortestDistances[lastDeliveryIndex][storageIndex] < minDistance) {
                minDistance = shortestDistances[lastDeliveryIndex][storageIndex];

                nearestStorageLocation = new Location(
                        storage.getLocationId()
                );
            }
        }

        return nearestStorageLocation;
    }

    public static double calculateTotalDistance(List<Location> route, ShortestPathsMatrix matrix) {
        double totalDistance = 0.0;
        Map<Integer, Integer> locationToVertexIndexMap = matrix.getLocationToVertexIndexMap();

        for (int i = 0; i < route.size() - 1; i++) {
            Location current = route.get(i);
            Location next = route.get(i + 1);
            int indexFrom = locationToVertexIndexMap.get(current.getLocationId());
            int indexTo = locationToVertexIndexMap.get(next.getLocationId());
            double distance = matrix.getShortestDistances()[indexFrom][indexTo];
            totalDistance += distance;
        }
        return totalDistance;
    }

    public static ShortestPathsMatrix calculateShortestPaths(WeightedGraph graph, List<Location> locations) {
        double[][] shortestDistances = GraphUtils.executeFloydWarshall(graph);

        // map location IDs to vertex IDs
        Map<Integer, String> locationIdToVertexIdMap = GraphUtils.mapLocationIdsToVertexIds(locations, graph);

        // create a map for vertex ID to index mapping
        Map<Integer, Integer> locationToVertexIndexMap = GraphUtils.createLocationToVertexIndexMap(locationIdToVertexIdMap, graph);

        // create a map from vertex IDs to their corresponding indices
        Map<String, Integer> vertexIdToIndexMap = GraphUtils.createVertexIdToIndexMap(graph);

        // create and return the ShortestPathsMatrix using the Builder
        return new ShortestPathsMatrix.Builder()
                .setShortestDistances(shortestDistances)
                .setVertexIdToIndexMap(vertexIdToIndexMap)
                .setLocationToVertexIndexMap(locationToVertexIndexMap)
                .build();
    }

    public static void printRoute(List<Location> route) {
        if (BooleanUtils.isEmptyOrNullCollection(route)) {
            LOGGER.info("No route available.");
            return;
        }

        // print the route information
        LOGGER.info("Route: ");

        IntStream.range(0, route.size()).forEach(i -> {
            LOGGER.info(StringConstants.NEWLINE + " -> " + route.get(i).toString());
        });
    }


    private RouteUtils() {
        ExceptionUtils.preventUtilityInstantiation();
    }
}
