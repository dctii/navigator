package com.solvd.navigator;


import com.solvd.navigator.util.AnsiCodes;
import com.solvd.navigator.util.StringConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class BaseDataLoader {
    private static final Logger LOGGER = LogManager.getLogger(BaseDataLoader.class);

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
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
                    // Loading should be done in this order:
//                    LoaderUtils.loadLocationsData(FilepathConstants.LOCATIONS_JSON);
                    LOGGER.info(StringConstants.NEWLINE);
//                    LoaderUtils.loadStoragesData(FilepathConstants.LOCATIONS_JSON);
                    LOGGER.info(StringConstants.NEWLINE);
                    // Generate with javafaker
//                    LoaderUtils.loadPersonsData();
                    LOGGER.info(StringConstants.NEWLINE);
                    // Employees must be personId=1 and personId=2
//                    LoaderUtils.loadEmployeesData(FilepathConstants.EMPLOYEES_JSON);
                    LOGGER.info(StringConstants.NEWLINE);
//                    LoaderUtils.loadVehiclesData(FilepathConstants.VEHICLES_JSON);
                    LOGGER.info(StringConstants.NEWLINE);
                    // driver are employeeId=1 and employeeId=2; and vehicleId=1 and vehicleId=2
//                    LoaderUtils.loadDriversData(FilepathConstants.DRIVERS_JSON);
                    LOGGER.info(StringConstants.NEWLINE);
                    // Generate, map on to persons
                    // order recipients must be all personId > 2
//                    LoaderUtils.loadOrderRecipientsData();
                    LOGGER.info(StringConstants.NEWLINE);
                    // Generate, map on to generated order recipients
//                    LoaderUtils.loadOrdersData();
                    break;
                case 2:
                    LOGGER.info("Uploading Locations...");
//                    LoaderUtils.loadLocationsData(FilepathConstants.LOCATIONS_JSON);
                    break;
                case 3:
                    LOGGER.info("Uploading Storages...");
//                    LoaderUtils.loadStoragesData(FilepathConstants.LOCATIONS_JSON);
                    break;
                case 4:
                    LOGGER.info("Uploading Persons...");
                    // 'airports.csv' (2024/01/07) from https://ourairports.com/data/
//                    LoaderUtils.loadPersonsData();
                    break;
                case 5:
                    LOGGER.info("Uploading Employees...");
//                    LoaderUtils.loadEmployeesData(FilepathConstants.EMPLOYEES_JSON);
                    break;
                case 6:
                    LOGGER.info("Uploading Vehicles...");
//                    LoaderUtils.loadVehiclesData(FilepathConstants.VEHICLES_JSON);
                    break;
                case 7:
                    LOGGER.info("Uploading Drivers...");
//                    LoaderUtils.loadDriversData(FilepathConstants.DRIVERS_JSON);
                    break;
                case 8:
                    LOGGER.info("Uploading Order Recipients...");
//                    LoaderUtils.loadOrderRecipientsData();
                    break;
                case 9:
                    LOGGER.info("Uploading Orders...");
//                    LoaderUtils.loadOrdersData();
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
