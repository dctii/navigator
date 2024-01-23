package com.solvd.navigator.service;

import com.solvd.navigator.bin.Location;

import java.util.List;

public interface ItemService {

    Location getItemLocation(int itemId);

    List<Location> getOptimalRouteForItem(int itemId, Location destination);

    void notifyItemDelay(int itemId, double estimatedDelay);

}
