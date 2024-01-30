package com.solvd.navigator;

import com.solvd.navigator.util.JsonDataStore;
import com.solvd.navigator.servicetwo.LocationService;
import com.solvd.navigator.servicetwo.StorageService;
import com.solvd.navigator.servicetwo.impl.LocationServiceImpl;
import com.solvd.navigator.servicetwo.impl.StorageServiceImpl;
import com.solvd.navigator.util.ClassConstants;
import com.solvd.navigator.util.DriverConnectionPool;
import com.solvd.navigator.util.DriverThreadManager;
import com.solvd.navigator.util.MenuUtils;
import com.solvd.navigator.util.ScannerUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.MAIN_ENTRYPOINT);
    private static final int NUMBER_OF_DRIVERS = 2;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int option;

        while (true) {
            LOGGER.info("Menu:");
            LOGGER.info("[1] Start Workday for {} Drivers", NUMBER_OF_DRIVERS);
            LOGGER.info("[0] Exit");

            String input = ScannerUtils.getInput(scanner, "Enter your choice: ");
            option = MenuUtils.parseIntOption(input);
            if (option == -1) {
                continue;
            }

            switch (option) {
                case 1:
                    // set global services
                    StorageService storageService = new StorageServiceImpl();
                    LocationService locationService = new LocationServiceImpl();

                    // instantiate driver pool
                    DriverConnectionPool driverConnectionPool =
                            DriverConnectionPool.getInstance(JsonDataStore.allDrivers);

                    // create new thread manager pool, thread runnable (driver workday logic) generated here
                    DriverThreadManager driverThreadManager = new DriverThreadManager(
                            driverConnectionPool,
                            locationService,
                            storageService
                    );

                    // start driver thread runs
                    driverThreadManager.startThreads(
                            JsonDataStore.allDrivers,
                            NUMBER_OF_DRIVERS
                    );


                    /* "Deadlock in Java Multithreading"
                        https://www.geeksforgeeks.org/deadlock-in-java-multithreading/

                        Below helps exit deadlock
                     */
                    try {
                        driverThreadManager.waitForThreadsToComplete();
                    } catch (InterruptedException e) {
                        LOGGER.error("Thread was interrupted: " + e.getMessage());
                        Thread.currentThread().interrupt();
                    }

                    break;
                case 0:
                    LOGGER.info("Exiting program.");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    LOGGER.error("Invalid option.");
            }
        }
    }
}
