package com.solvd.navigator.service;

import com.solvd.navigator.math.RoutePlan;

import java.util.List;

public interface RouteService {

    List<RoutePlan> getRouteHistory();

    DriverStatistic getDriverStatistics(int driverId);

}
