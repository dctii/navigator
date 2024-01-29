package com.solvd.navigator.service;

import com.solvd.navigator.math.RoutePlan;

import java.util.List;

public interface RouteService {

 List<RoutePlan> getRoutePlan();

    DriverStatistic getDriverStatistics(int driverId);

}
