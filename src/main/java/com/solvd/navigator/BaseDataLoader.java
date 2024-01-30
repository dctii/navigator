package com.solvd.navigator;


import com.solvd.navigator.bin.Location;
import com.solvd.navigator.util.JsonDataStore;
import com.solvd.navigator.util.AnsiCodes;
import com.solvd.navigator.util.ClassConstants;
import com.solvd.navigator.util.LoadUtils;
import com.solvd.navigator.util.StringConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BaseDataLoader {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.BASE_DATA_LOADER_ENTRYPOINT);

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

            LOGGER.info(
                    "{}{}{}=== Navigator Base Data Loading Tools: ==={}",
                    StringConstants.NEWLINE, AnsiCodes.BOLD, AnsiCodes.YELLOW, AnsiCodes.RESET_ALL
            );
            LOGGER.info("[0] Exit");
            LOGGER.info(
                    "{}{}[1] Load all data: locations, persons, employees, vehicles, drivers, order recipients, and orders{}{}",
                    AnsiCodes.BLUE, AnsiCodes.BOLD, AnsiCodes.RESET_ALL, StringConstants.NEWLINE
            );

            LOGGER.info("{}Enter your choice: {}", StringConstants.NEWLINE, StringConstants.NEWLINE);

            int choice = scanner.nextInt();

            scanner.nextLine();

            switch (choice) {
                case 1:
                    LoadUtils.loadAllData(currentAvailableLocations);
                    LOGGER.info("{}All of the data has been successfully loaded, exiting program.{}", AnsiCodes.BLUE, AnsiCodes.RESET_ALL);
                    System.exit(0);
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
