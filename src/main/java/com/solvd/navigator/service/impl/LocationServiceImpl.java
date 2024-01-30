//package com.solvd.navigator.service.impl;
//
//import com.solvd.navigator.bin.Location;
//import com.solvd.navigator.service.LocationService;
//
//import java.util.List;
//
//public class LocationServiceImpl implements LocationService {
//    private final Location location;
//
//    public LocationServiceImpl(Location location) {
//        this.location = location;
//    }
//
//    @Override
//    public Location getLocationById(int locationId) {
//       return location.findById(locationId);
//    }
//
//    @Override
//    public double calculateDistance(Location source, Location destination) {
//
//        //  calculate distance between two locations
//
//        double deltaX = source.getCoordinateX() - destination.getCoordinateX();
//        double deltaY = source.getCoordinateY() - destination.getCoordinateY();
//        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
//    }
//
//    @Override
//    public List<Location> getLocationsOnRoute(String routeId) {
//        // logic to retrieve locations on a specific route identified by routeId
//
//      return location.getLocationsOnRoute(routeId);
//   }
//
//    @Override
//    public Location getCurrentUserLocation() {
//
//         return location.getCurrentUserLocation();
//    }
//    }
//
