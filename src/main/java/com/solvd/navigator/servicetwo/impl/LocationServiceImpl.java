package com.solvd.navigator.servicetwo.impl;

import com.solvd.navigator.bin.Location;
import com.solvd.navigator.bin.Storage;
import com.solvd.navigator.dao.LocationDAO;
import com.solvd.navigator.math.RoutePlan;
import com.solvd.navigator.math.graph.GraphConstants;
import com.solvd.navigator.servicetwo.LocationService;
import com.solvd.navigator.util.ClassConstants;
import com.solvd.navigator.util.DAOFactory;
import com.solvd.navigator.util.JacksonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LocationServiceImpl implements LocationService {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.LOCATION_SERVICE_IMPL);
    private final LocationDAO locationDAO = DAOFactory.createDAO(ClassConstants.LOCATION_DAO);

    @Override
    public Location getLocationById(int locationId) {
        return locationDAO.getById(locationId);
    }

    @Override
    public Location getOriginLocation(RoutePlan routePlan) {
        return locationDAO.getById(routePlan.getOriginLocation().getLocationId());
    }

    @Override
    public Location getTerminalLocation(RoutePlan routePlan) {
        return locationDAO.getById(routePlan.getTerminalLocation().getLocationId());
    }

    @Override
    public List<Location> getAllLocationsFromFile(String resourcePath) {
        return JacksonUtils.extractItems(resourcePath, ClassConstants.LOCATION);
    }

    @Override
    public List<Location> getAvailableLocationsFromAll(String resourcePath, Set<Integer> excludedLocationIds) {
        return getAllLocationsFromFile(resourcePath).stream()
                .filter(location -> !excludedLocationIds.contains(location.getLocationId()))
                .collect(Collectors.toList());
    }


    public List<Location> getAvailableLocationsFromAll(String resourcePath) {
        return getAvailableLocationsFromAll(
                resourcePath,
                GraphConstants.EXCLUDED_LOCATION_IDS
        );
    }

    @Override
    public Location getStorageLocation(Storage storage) {
        return locationDAO.getById(storage.getLocationId());
    }


}
