package com.solvd.navigator.service.impl;

import com.solvd.navigator.service.RouteService;

import java.util.stream.Collectors;


public class RouteHistoryImpl implements RouteService {

    public RouteHistoryImpl(RouteHistory routeHistory) {
        this.routeHistory = routeHistory;
    }

    @Override
    public List<RoutePlan> getRouteHistory() {
        return routeHistory.getRoutePlans();
    }

    public DriverStatistics getDriverStatistics(int driverId) {
        List<RoutePlan> routesForDriver = routeHistory.getRoutePlans().stream()
                .filter(routePlan -> routePlan.getDriverId() == driverId)
                .collect(Collectors.toList());

        double totalDistance = routesForDriver.stream()
                .mapToDouble(RoutePlan::getTotalDistance)
                .sum();

        double totalDeliveryTime = routesForDriver.stream()
                .mapToDouble(RoutePlan::getTotalRouteMinutes)
                .sum();



        return new DriverStatistics(driverId, totalDistance, totalDeliveryTime);
    }
}