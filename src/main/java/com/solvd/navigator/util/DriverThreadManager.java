package com.solvd.navigator.util;

import com.solvd.navigator.bin.Driver;
import com.solvd.navigator.exception.NotEnoughDriversException;
import com.solvd.navigator.servicetwo.LocationService;
import com.solvd.navigator.servicetwo.StorageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class DriverThreadManager {
    private static final Logger LOGGER = LogManager.getLogger(DriverThreadManager.class);
    private final DriverConnectionPool pool;
    private final LocationService locationService;
    private final StorageService storageService;
    private final List<Thread> threads = new ArrayList<>();

    public DriverThreadManager(DriverConnectionPool pool, LocationService locationService, StorageService storageService) {
        this.pool = pool;
        this.locationService = locationService;
        this.storageService = storageService;
    }

    public void startThreads(List<Driver> drivers) {
        startThreads(drivers, drivers.size());
    }

    public void startThreads(List<Driver> drivers, int numberOfDrivers) {
        if (drivers.size() < numberOfDrivers) {
            final String NOT_ENOUGH_DRIVERS_EXCEPTION_MSG =
                    "The amount drivers requested is greater than the number of drivers that exist";
            LOGGER.error(NOT_ENOUGH_DRIVERS_EXCEPTION_MSG);
            throw new NotEnoughDriversException(NOT_ENOUGH_DRIVERS_EXCEPTION_MSG);
        }

        IntStream.range(0, numberOfDrivers).forEach(i -> {
            Driver driver = drivers.get(i);
            Runnable task = new DriverThreadRunnable(pool, locationService, storageService, driver);
            Thread thread = new Thread(task, "Driver " + driver.getDriverId());
            threads.add(thread);
            thread.start();
        });
    }

    public void waitForThreadsToComplete() throws InterruptedException {
        threads.forEach(thread -> {
            try {
                thread.join(); // pauses the execution of the thread
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
