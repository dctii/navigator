package com.solvd.navigator;


import com.solvd.navigator.bin.Location;
import com.solvd.navigator.math.util.JsonDataStore;
import com.solvd.navigator.util.AnsiCodes;
import com.solvd.navigator.util.FilepathConstants;
import com.solvd.navigator.util.LoadUtils;
import com.solvd.navigator.util.StringConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BaseDataLoader {
    private static final Logger LOGGER = LogManager.getLogger(BaseDataLoader.class);

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        List<Location> currentAvailableLocations = new ArrayList<>(JsonDataStore.allAvailableLocations);

        LOGGER.info(
                "{}Current Amount of Available Locations: {}{}",
                AnsiCodes.YELLOW,
                currentAvailableLocations.size(),
                AnsiCodes.RESET_ALL
        );

        while (true) {
            LOGGER.info(StringConstants.NEWLINE);

            LOGGER.info("{}{}=== Airport Base Data Loading Tools: ==={}",
                    AnsiCodes.BOLD, AnsiCodes.YELLOW, AnsiCodes.RESET_ALL);
            LOGGER.info("[0] Exit");
            LOGGER.info("{}{}[1] Execute all 'Load' options{}", AnsiCodes.BLUE, AnsiCodes.BOLD, AnsiCodes.RESET_ALL);
            LOGGER.info("[2] Load Locations Data");
            LOGGER.info("[3] Load Storages Data");
            LOGGER.info("[4] Load Persons Data");
            LOGGER.info("[5] Load Employees Data");
            LOGGER.info("[6] Load Vehicles Data");
            LOGGER.info("[7] Load Drivers Data");
            LOGGER.info("[8] Load OrderRecipients Data");
            LOGGER.info("[9] Load Orders Data");

            LOGGER.info(StringConstants.NEWLINE + "Enter your choice: ");

            int choice = scanner.nextInt();
            LOGGER.info(StringConstants.NEWLINE);

            scanner.nextLine();

            switch (choice) {
                case 1:
                    // Loading should be done in the order that each LoadUtils.* method is in below in 'case 1':
                    LoadUtils.loadLocationsData(FilepathConstants.LOCATIONS_JSON);
                    LOGGER.info(StringConstants.NEWLINE);

                    currentAvailableLocations =
                            LoadUtils.loadStoragesData(FilepathConstants.STORAGES_JSON, currentAvailableLocations);

                    LOGGER.info(
                            "{}Current Amount of Available Locations: {}{}",
                            AnsiCodes.YELLOW,
                            currentAvailableLocations.size(),
                            AnsiCodes.RESET_ALL
                    );

                    LOGGER.info(StringConstants.NEWLINE);

                    // generate first and last name with javafaker
                    LoadUtils.loadPersonsData();
                    LOGGER.info(StringConstants.NEWLINE);

                    // Employees must be personId=1 and personId=2
                    LoadUtils.loadEmployeesData(FilepathConstants.EMPLOYEES_JSON);
                    LOGGER.info(StringConstants.NEWLINE);

                    LoadUtils.loadVehiclesData(FilepathConstants.VEHICLES_JSON);
                    LOGGER.info(StringConstants.NEWLINE);

                    // driver are employeeId=1 and employeeId=2; and vehicleId=1 and vehicleId=2
                    LoadUtils.loadDriversData(FilepathConstants.DRIVERS_JSON);
                    LOGGER.info(StringConstants.NEWLINE);

                    // generate, map on to persons
                    // order recipients must be all personId > 2

                    currentAvailableLocations =
                            LoadUtils.loadOrderRecipientsData(currentAvailableLocations);

                    LOGGER.info(
                            "{}Current Amount of Available Locations: {}{}",
                            AnsiCodes.YELLOW,
                            currentAvailableLocations.size(),
                            AnsiCodes.RESET_ALL
                    );

                    LOGGER.info(StringConstants.NEWLINE);

                    // generate 960 (number or orders per storage) * 4 (number of storages), map on to generated order recipients
                    LoadUtils.loadOrdersData();
                    break;
                case 2:
                    LOGGER.info("Uploading Locations...");
                    LoadUtils.loadLocationsData(FilepathConstants.LOCATIONS_JSON);
                    break;
                case 3:
                    LOGGER.info("Uploading Storages...");
                    currentAvailableLocations =
                            LoadUtils.loadStoragesData(
                                    FilepathConstants.STORAGES_JSON,
                                    currentAvailableLocations
                            );

                    LOGGER.info(
                            "{}Current Amount of Available Locations: {}{}",
                            AnsiCodes.YELLOW,
                            currentAvailableLocations.size(),
                            AnsiCodes.RESET_ALL
                    );
                    break;
                case 4:
                    LOGGER.info("Uploading Persons...");
                    LoadUtils.loadPersonsData();
                    break;
                case 5:
                    LOGGER.info("Uploading Employees...");
                    LoadUtils.loadEmployeesData(FilepathConstants.EMPLOYEES_JSON);
                    break;
                case 6:
                    LOGGER.info("Uploading Vehicles...");
                    LoadUtils.loadVehiclesData(FilepathConstants.VEHICLES_JSON);
                    break;
                case 7:
                    LOGGER.info("Uploading Drivers...");
                    LoadUtils.loadDriversData(FilepathConstants.DRIVERS_JSON);
                    break;
                case 8:
                    LOGGER.info("Uploading Order Recipients...");
                    currentAvailableLocations =
                            LoadUtils.loadOrderRecipientsData(currentAvailableLocations);

                    LOGGER.info(
                            "{}Current Amount of Available Locations: {}{}",
                            AnsiCodes.YELLOW,
                            currentAvailableLocations.size(),
                            AnsiCodes.RESET_ALL
                    );
                    break;
                case 9:
                    LOGGER.info("Uploading Orders...");
                    LoadUtils.loadOrdersData();
                    break;
                case 0:
                    LOGGER.info("Exiting...");
                    System.exit(0);
                default:
                    LOGGER.info("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}
