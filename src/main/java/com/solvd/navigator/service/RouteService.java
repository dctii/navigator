package com.solvd.navigator.service;

import java.util.List;

public interface RouteService {

    List<RoutePlan> getRouteHistory();

    DriverStatistics getDriverStatistics(int driverId);

}
