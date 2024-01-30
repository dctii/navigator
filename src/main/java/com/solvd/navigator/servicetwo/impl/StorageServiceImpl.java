package com.solvd.navigator.servicetwo.impl;

import com.solvd.navigator.bin.Storage;
import com.solvd.navigator.dao.LocationDAO;
import com.solvd.navigator.dao.StorageDAO;
import com.solvd.navigator.math.RoutePlan;
import com.solvd.navigator.servicetwo.StorageService;
import com.solvd.navigator.util.ClassConstants;
import com.solvd.navigator.util.CollectionUtils;
import com.solvd.navigator.util.DAOFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class StorageServiceImpl implements StorageService {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.STORAGE_SERVICE_IMPL);
    private final StorageDAO storageDAO = DAOFactory.createDAO(ClassConstants.STORAGE_DAO);
    private final LocationDAO locationDAO = DAOFactory.createDAO(ClassConstants.LOCATION_DAO);

    @Override
    public List<Storage> getAllStorages() {
        return storageDAO.getAll();
    }

    @Override
    public Storage getRandomStorage() {
        List<Storage> allStorages = getAllStorages();
        return CollectionUtils.getRandomItemFromList(allStorages);
    }

    @Override
    public Storage getStorageByLocationId(int locationId) {
        return storageDAO.getStorageByLocationId(locationId);
    }


    @Override
    public Storage getTerminalStorage(RoutePlan routePlan) {
        return storageDAO.getStorageByLocationId(routePlan.getTerminalLocation().getLocationId());
    }
}
