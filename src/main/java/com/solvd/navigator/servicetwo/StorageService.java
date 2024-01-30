package com.solvd.navigator.servicetwo;


import com.solvd.navigator.bin.Storage;
import com.solvd.navigator.math.RoutePlan;

import java.util.List;

public interface StorageService {
    List<Storage> getAllStorages();

    Storage getRandomStorage();

    Storage getStorageByLocationId(int locationId);

    Storage getTerminalStorage(RoutePlan routePlan);
}
