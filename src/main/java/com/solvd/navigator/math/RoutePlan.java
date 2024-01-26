package com.solvd.navigator.math;

import com.solvd.navigator.bin.Location;
import com.solvd.navigator.exception.InvalidRouteCalculator;
import com.solvd.navigator.exception.RouteNeedsLocationsException;
import com.solvd.navigator.math.util.OrderConstants;
import com.solvd.navigator.math.util.RouteUtils;
import com.solvd.navigator.util.ClassConstants;
import com.solvd.navigator.util.StringFormatters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class RoutePlan {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.ROUTE_PLAN);
    private final UUID id;
    private Location originLocation;
    private List<Location> deliveryLocations;
    private Location terminalLocation;
    private List<Location> route;
    private double totalDistance;
    private double totalRouteMinutes;

    public RoutePlan() {
        this.id = UUID.randomUUID();
    }

    private RoutePlan(Builder builder) {
        this.id = UUID.randomUUID();
        this.originLocation = builder.originLocation;
        this.deliveryLocations = builder.deliveryLocations;
        this.terminalLocation = builder.terminalLocation;
        this.route = builder.route;
        this.totalDistance = builder.totalDistance;
        this.totalRouteMinutes = builder.totalRouteMinutes;
    }

    public static class Builder {
        private Location originLocation;
        private List<Location> deliveryLocations;
        private Location terminalLocation;
        private List<Location> route;
        private double totalDistance;
        private double totalRouteMinutes;

        public Builder setOriginLocation(Location originLocation) {
            if (originLocation != null) {
                this.originLocation = originLocation;
            } else {
                final String ROUTE_NEEDS_LOCATION_EXCEPTION_MSG =
                        "The starting point of the route cannot be empty or null";
                LOGGER.error(ROUTE_NEEDS_LOCATION_EXCEPTION_MSG);
                throw new RouteNeedsLocationsException(ROUTE_NEEDS_LOCATION_EXCEPTION_MSG);
            }
            return this;
        }

        public Builder setDeliveryLocations(List<Location> deliveryLocations) {
            if (deliveryLocations != null
                    && !deliveryLocations.isEmpty()) {
                this.deliveryLocations = deliveryLocations;
            } else {
                final String ROUTE_NEEDS_LOCATIONS_EXCEPTION_MSG =
                        "The delivery locations cannot be empty or null null";
                LOGGER.error(ROUTE_NEEDS_LOCATIONS_EXCEPTION_MSG);
                throw new RouteNeedsLocationsException(ROUTE_NEEDS_LOCATIONS_EXCEPTION_MSG);
            }
            return this;
        }

        public Builder calculateRouteDetails(RouteCalculator calculator) {
            if (calculator == null) {
                final String INVALID_ROUTE_CALCULATOR_EXCEPTION_MSG =
                        "RouteCalculator cannot be null.";
                LOGGER.error(INVALID_ROUTE_CALCULATOR_EXCEPTION_MSG);
                throw new InvalidRouteCalculator(INVALID_ROUTE_CALCULATOR_EXCEPTION_MSG);
            }

            if (this.originLocation != null
                    && this.deliveryLocations != null
                    && !this.deliveryLocations.isEmpty()
            ) {
                this.route = calculator.findFastestRoute(this.originLocation, this.deliveryLocations);
                this.terminalLocation = this.route.get(this.route.size() - 1);
                this.totalDistance = calculator.calculateTotalDistance(this.route);
                this.totalRouteMinutes = this.totalDistance * OrderConstants.MINUTES_PER_KM;
            } else {
                final String ROUTE_NEEDS_LOCATIONS_EXCEPTION_MSG =
                        "The starting point of the route and the other delivery locations cannot be empty or null. You must setOriginLocation and setDeliveryLocations on this builder chain first.";
                LOGGER.error(ROUTE_NEEDS_LOCATIONS_EXCEPTION_MSG);
                throw new RouteNeedsLocationsException(ROUTE_NEEDS_LOCATIONS_EXCEPTION_MSG);
            }
            return this;
        }

        public RoutePlan build() {
            return new RoutePlan(this);
        }
    }

    public void printRoute() {
        RouteUtils.printRoute(route);
    }

    public double getDistanceBetweenLocations(
            RouteCalculator routeCalculator,
            Location locationFrom,
            Location locationTo
    ) {
        return routeCalculator.getDistanceBetweenLocations(locationFrom, locationTo);
    }

    public double getDistanceBetweenLocations(
            RouteCalculator routeCalculator,
            int locationFromId,
            int locationToId
    ) {
        return routeCalculator.getDistanceBetweenLocations(locationFromId, locationToId);
    }

    // Getter for id
    public UUID getId() {
        return id;
    }

    public Location getOriginLocation() {
        return originLocation;
    }

    public void setOriginLocation(Location originLocation) {
        this.originLocation = originLocation;
    }

    public List<Location> getDeliveryLocations() {
        return deliveryLocations;
    }

    public void setDeliveryLocations(List<Location> deliveryLocations) {
        this.deliveryLocations = deliveryLocations;
    }

    public Location getTerminalLocation() {
        return terminalLocation;
    }

    public void setTerminalLocation(Location terminalLocation) {
        this.terminalLocation = terminalLocation;
    }

    public List<Location> getRoute() {
        return route;
    }

    public void setRoute(List<Location> route) {
        this.route = route;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public double getTotalRouteMinutes() {
        return totalRouteMinutes;
    }

    public void setTotalRouteMinutes(double totalRouteMinutes) {
        this.totalRouteMinutes = totalRouteMinutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoutePlan routePlan = (RoutePlan) o;
        return Objects.equals(id, routePlan.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public String toString() {
        Class<?> currClass = ClassConstants.ROUTE_PLAN;
        String[] fieldNames = {
                "id",
                "originLocation",
                "deliveryLocations",
                "terminalLocation",
                "route",
                "totalDistance",
                "totalRouteMinutes"
        };

        String fieldsString = StringFormatters.buildFieldsString(this, fieldNames);
        return StringFormatters.buildToString(currClass, fieldNames, fieldsString);
    }
}
