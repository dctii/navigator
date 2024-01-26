package com.solvd.navigator.math;

import com.solvd.navigator.bin.Location;
import com.solvd.navigator.bin.Storage;
import com.solvd.navigator.math.graph.ShortestPathsMatrix;
import com.solvd.navigator.math.graph.WeightedGraph;
import com.solvd.navigator.math.util.RouteUtils;
import com.solvd.navigator.util.ClassConstants;
import com.solvd.navigator.util.StringFormatters;

import java.util.ArrayList;
import java.util.List;

public class RouteCalculatorImpl implements RouteCalculator {
    private ShortestPathsMatrix shortestPathsMatrix;
    private List<Storage> storages;
    private List<Location> locations;

    private RouteCalculatorImpl(Builder builder) {
        this.shortestPathsMatrix = builder.shortestPathsMatrix;
        this.storages = builder.storages;
        this.locations = builder.locations;
    }

    public static class Builder {
        private ShortestPathsMatrix shortestPathsMatrix;
        private List<Storage> storages = new ArrayList<>();
        private List<Location> locations = new ArrayList<>();

        public Builder setStorages(List<Storage> storages) {
            if (storages != null && !storages.isEmpty()) {
                this.storages = storages;
            } else {
                throw new IllegalStateException(
                        "List of storages cannot be null or empty"
                );
            }
            return this;
        }

        public Builder setLocations(List<Location> locations) {
            if (locations != null && !locations.isEmpty()) {
                this.locations = locations;
            } else {
                throw new IllegalStateException(
                        "List of locations cannot be null or empty"
                );
            }
            return this;
        }

        public Builder buildShortestPathsMatrix(WeightedGraph graph) {
            if (this.locations != null && graph != null) {
                this.shortestPathsMatrix = RouteUtils.calculateShortestPaths(graph, this.locations);
            } else {
                throw new IllegalStateException(
                        "Locations and graph must be set with setLocations in this builder chain before building ShortestPathsMatrix"
                );
            }
            return this;
        }

        public Builder buildShortestPathsMatrix(WeightedGraph graph, List<Location> locations) {
            if (locations != null && !locations.isEmpty() && graph != null) {
                this.shortestPathsMatrix = RouteUtils.calculateShortestPaths(graph, locations);
            } else {
                throw new IllegalStateException(
                        "Graph cannot be null and the locations list cannot be null or empty to build the ShortestPathsMatrix"
                );
            }
            return this;
        }

        public RouteCalculatorImpl build() {
            // Here you could add the logic to map location IDs to vertex IDs and build the ShortestPathsMatrix
            return new RouteCalculatorImpl(this);
        }
    }


    @Override
    public List<Location> findFastestRoute(
            Location startingLocation,
            List<Location> locationsForRoute
    ) {
        return RouteUtils.findFastestRoute(
                startingLocation,
                locationsForRoute,
                this.storages,
                this.shortestPathsMatrix
        );
    }

    public List<Location> findFastestRoute(
            Location startingLocation,
            List<Location> locationsForRoute,
            ShortestPathsMatrix matrix
    ) {
        return RouteUtils.findFastestRoute(
                startingLocation,
                locationsForRoute,
                this.storages,
                matrix
        );
    }

    @Override
    public Location findNearestStorageLocation(
            Location lastLocation,
            ShortestPathsMatrix matrix
    ) {
        return RouteUtils.findNearestStorageLocation(
                lastLocation,
                storages,
                matrix
        );
    }

    @Override
    public double calculateTotalDistance(List<Location> route) {
        return RouteUtils.calculateTotalDistance(route, shortestPathsMatrix);
    }


    public ShortestPathsMatrix getShortestPathsMatrix() {
        return shortestPathsMatrix;
    }

    public void setShortestPathsMatrix(ShortestPathsMatrix shortestPathsMatrix) {
        this.shortestPathsMatrix = shortestPathsMatrix;
    }

    public List<Storage> getStorages() {
        return storages;
    }

    public void setStorages(List<Storage> storages) {
        this.storages = storages;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public double getDistanceBetweenLocations(Location locationFrom, Location locationTo) {
        return getDistanceBetweenLocations(
                locationFrom.getLocationId(),
                locationTo.getLocationId()
        );
    }

    public double getDistanceBetweenLocations(int locationFromId, int locationToId) {
        return RouteUtils.getDistanceBetweenLocations(
                this.shortestPathsMatrix,
                locationFromId,
                locationToId
        );
    }

    @Override
    public String toString() {
        Class<?> currClass = ClassConstants.ROUTE_CALCULATOR_IMPL;
        String[] fieldNames = {
                "shortestPathsMatrix",
                "storages",
                "locations"

        };

        String fieldsString = StringFormatters.buildFieldsString(this, fieldNames);
        return StringFormatters.buildToString(currClass, fieldNames, fieldsString);
    }
}
