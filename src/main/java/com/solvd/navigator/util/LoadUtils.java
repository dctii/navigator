package com.solvd.navigator.util;

import com.github.javafaker.Faker;
import com.solvd.navigator.bin.Driver;
import com.solvd.navigator.bin.Employee;
import com.solvd.navigator.bin.Location;
import com.solvd.navigator.bin.Order;
import com.solvd.navigator.bin.OrderRecipient;
import com.solvd.navigator.bin.Person;
import com.solvd.navigator.bin.Storage;
import com.solvd.navigator.bin.Vehicle;
import com.solvd.navigator.dao.DriverDAO;
import com.solvd.navigator.dao.EmployeeDAO;
import com.solvd.navigator.dao.LocationDAO;
import com.solvd.navigator.dao.OrderDAO;
import com.solvd.navigator.dao.OrderRecipientDAO;
import com.solvd.navigator.dao.PersonDAO;
import com.solvd.navigator.dao.StorageDAO;
import com.solvd.navigator.dao.VehicleDAO;
import com.solvd.navigator.exception.NoMoreAssignableLocationsException;
import com.solvd.navigator.math.util.OrderConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

public class LoadUtils {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.LOAD_UTILS);

    public static final String LOCATIONS_JSON_KEY = "locations";
    public static final String STORAGES_JSON_KEY = "storages";
    public static final String EMPLOYEES_JSON_KEY = "employees";
    public static final String VEHICLES_JSON_KEY = "vehicles";
    public static final String DRIVERS_JSON_KEY = "drivers";

    public static void loadAllData(List<Location> currentAvailableLocations) {
        final Map<String, String> jsonFilepathMap = Map.of(
                LOCATIONS_JSON_KEY, FilepathConstants.LOCATIONS_JSON,
                STORAGES_JSON_KEY, FilepathConstants.STORAGES_JSON,
                EMPLOYEES_JSON_KEY, FilepathConstants.EMPLOYEES_JSON,
                VEHICLES_JSON_KEY, FilepathConstants.VEHICLES_JSON,
                DRIVERS_JSON_KEY, FilepathConstants.DRIVERS_JSON
        );

        loadAllData(jsonFilepathMap, currentAvailableLocations);

    }

    public static void loadAllData(Map<String, String> jsonFilepathMap, List<Location> currentAvailableLocations) {
        loadLocationsData(jsonFilepathMap.get(LOCATIONS_JSON_KEY));

        currentAvailableLocations =
                loadStoragesData(jsonFilepathMap.get(STORAGES_JSON_KEY), currentAvailableLocations);

        LOGGER.info(
                "{}Current Amount of Available Locations: {}{}",
                AnsiCodes.YELLOW,
                currentAvailableLocations.size(),
                AnsiCodes.RESET_ALL
        );

        loadPersonsData();

        loadEmployeesData(jsonFilepathMap.get(EMPLOYEES_JSON_KEY));

        loadVehiclesData(jsonFilepathMap.get(VEHICLES_JSON_KEY));

        loadDriversData(jsonFilepathMap.get(DRIVERS_JSON_KEY));

        currentAvailableLocations =
                loadOrderRecipientsData(currentAvailableLocations);

        LOGGER.info(
                "{}Current Amount of Available Locations: {}{}",
                AnsiCodes.YELLOW,
                currentAvailableLocations.size(),
                AnsiCodes.RESET_ALL
        );

        LoadUtils.loadOrdersData();

    }

    public static void loadLocationsData(String jsonFilepath) {
        final LocationDAO locationDAO = DAOFactory.createDAO(ClassConstants.LOCATION_DAO);

        List<Location> locations = JacksonUtils.extractItems(
                jsonFilepath,
                ClassConstants.LOCATION
        );
        locations.forEach(locationDAO::create);
        LOGGER.info(
                "{}Locations data loaded successfully.{}",
                AnsiCodes.GREEN, AnsiCodes.RESET_ALL
        );
    }

    public static void loadStoragesData(String jsonFilepath) {
        final StorageDAO storageDAO = DAOFactory.createDAO(ClassConstants.STORAGE_DAO);


        List<Storage> storages = JacksonUtils.extractItems(
                jsonFilepath,
                ClassConstants.STORAGE
        );

        storages.forEach(storageDAO::create);
        LOGGER.info(
                "{}Storages data loaded successfully.{}",
                AnsiCodes.GREEN, AnsiCodes.RESET_ALL
        );

    }

    public static List<Location> loadStoragesData(String jsonFilepath, List<Location> availableLocations) {
        final StorageDAO storageDAO = DAOFactory.createDAO(ClassConstants.STORAGE_DAO);

        List<Storage> storages = JacksonUtils.extractItems(jsonFilepath, ClassConstants.STORAGE);

        // this list will keep track of locations that are still able to be assigned to ensure orderRecipients have unique locationIds
        List<Location> assignableLocations = new ArrayList<>(availableLocations);
        Random random = new Random();

        // assign locations to storages and update their locationId
        storages.forEach(storage -> {
            if (!assignableLocations.isEmpty()) {
                int randomIndex = random.nextInt(assignableLocations.size());
                Location assignedLocation = assignableLocations.remove(randomIndex);

                storage.setLocationId(assignedLocation.getLocationId());

                storageDAO.create(storage);
            } else {
                final String NO_MORE_LOCATIONS_EXCEPTION_MSG =
                        "There are no more locations that can be assigned to an order recipient";
                LOGGER.error(NO_MORE_LOCATIONS_EXCEPTION_MSG);
                throw new NoMoreAssignableLocationsException(NO_MORE_LOCATIONS_EXCEPTION_MSG);
            }
        });

        JacksonUtils.updateStoragesWithLocations(jsonFilepath, storages);

        LOGGER.info("{}Storages data loaded successfully.{}", AnsiCodes.GREEN, AnsiCodes.RESET_ALL);

        // return the locations that were not assigned
        return assignableLocations;
    }

    /* loadPersonsData()
        Generate fake people data.

        - The first 2 will be the drivers for this, as can be seen as the first conditions of the mapToObj in the Intstream.
        - The rest of the people will use `com.github.javafaker.Faker` to generate mock data
        - The number of people to generate is set to 81 because it includes 2 drivers and the 79 traversable locations of the total 100 on the WeightedGraph
            after accounting for the exclusion of locations that:
                - GraphConstants.RIVER, GraphConstants.BIG_BUILDING_ONE, GraphConstants.BIG_BUILDING_TWO which exclude 17 location points, and
                - the 4 storage warehouses occupy 4 location points,

            This leaves us with 79 locations for persons who can be OrderRecipients and we add the drivers which makes it 81.
    */
    public static void loadPersonsData() {
        final PersonDAO personDAO = DAOFactory.createDAO(ClassConstants.PERSON_DAO);
        Faker faker = new Faker();
        int numberOfPersonsToGenerate = 81;

        // add set that collects names, will be used as what to check against so that all names created for persons are unique
        Set<String> existingNames = new HashSet<>();

        IntStream.range(0, numberOfPersonsToGenerate)
                .mapToObj(i -> {
                    Person currentPerson = new Person();
                    String firstName, lastName;

                    // set names for drivers' 'Person' first
                    if (i == 0) {
                        firstName = OrderConstants.DRIVER_ONE_FIRST_NAME;
                        lastName = OrderConstants.DRIVER_ONE_LAST_NAME;
                    } else if (i == 1) {
                        firstName = OrderConstants.DRIVER_TWO_FIRST_NAME;
                        lastName = OrderConstants.DRIVER_TWO_LAST_NAME;
                    } else {
                        String fullName;

                        do {
                            firstName = faker.name().firstName();
                            lastName = faker.name().lastName();
                            fullName = StringUtils.joinWith(StringConstants.SINGLE_WHITESPACE, firstName, lastName);
                        } while (existingNames.contains(fullName));

                        existingNames.add(fullName);
                    }

                    currentPerson.setFirstName(firstName);
                    currentPerson.setLastName(lastName);

                    return currentPerson;
                })
                .forEach(personDAO::create);

        LOGGER.info(
                "{}Persons data loaded successfully.{}",
                AnsiCodes.GREEN, AnsiCodes.RESET_ALL
        );
    }


    public static void loadPersonsData(String jsonFilepath) {
        final PersonDAO personDAO = DAOFactory.createDAO(ClassConstants.PERSON_DAO);

        List<Person> persons = JacksonUtils.extractItems(
                jsonFilepath,
                ClassConstants.PERSON
        );
        persons.forEach(personDAO::create);
        LOGGER.info(
                "{}Persons data loaded successfully.{}",
                AnsiCodes.GREEN, AnsiCodes.RESET_ALL
        );

    }

    public static void loadEmployeesData(String jsonFilepath) {
        final EmployeeDAO employeeDAO = DAOFactory.createDAO(ClassConstants.EMPLOYEE_DAO);

        List<Employee> employees = JacksonUtils.extractItems(
                jsonFilepath,
                ClassConstants.EMPLOYEE
        );
        employees.forEach(employeeDAO::create);
        LOGGER.info(
                "{}Employees data loaded successfully.{}",
                AnsiCodes.GREEN, AnsiCodes.RESET_ALL
        );

    }

    public static void loadVehiclesData(String jsonFilepath) {
        final VehicleDAO vehicleDAO = DAOFactory.createDAO(ClassConstants.VEHICLE_DAO);

        List<Vehicle> vehicles = JacksonUtils.extractItems(
                jsonFilepath,
                ClassConstants.VEHICLE
        );
        vehicles.forEach(vehicleDAO::create);
        LOGGER.info(
                "{}Vehicles data loaded successfully.{}",
                AnsiCodes.GREEN, AnsiCodes.RESET_ALL
        );

    }

    public static void loadDriversData(String jsonFilepath) {
        final DriverDAO driverDAO = DAOFactory.createDAO(ClassConstants.DRIVER_DAO);

        List<Driver> drivers = JacksonUtils.extractItems(
                jsonFilepath,
                ClassConstants.DRIVER
        );
        drivers.forEach(driverDAO::create);
        LOGGER.info(
                "{}Drivers data loaded successfully.{}",
                AnsiCodes.GREEN, AnsiCodes.RESET_ALL
        );

    }

    public static List<Location> loadOrderRecipientsData(List<Location> availableLocations) {
        final OrderRecipientDAO orderRecipientDAO = DAOFactory.createDAO(ClassConstants.ORDER_RECIPIENT_DAO);
        final PersonDAO personDAO = DAOFactory.createDAO(ClassConstants.PERSON_DAO);

        Set<Integer> driverPersonIds = Set.of(1, 2); // Assuming person IDs of drivers are 1 and 2

        // this list will keep track of locations that are still able to be assigned to ensure orderRecipients have unique locationIds
        List<Location> assignableLocations = new ArrayList<>(availableLocations);
        Random random = new Random();

        // Assign locations to non-drivers
        IntStream.rangeClosed(3, OrderConstants.TOTAL_NUMBER_OF_PERSONS)
                .filter(personId -> !driverPersonIds.contains(personId))
                .mapToObj(personDAO::getById)
                .forEach(person -> {
                    if (!assignableLocations.isEmpty()) {
                        int randomIndex = random.nextInt(assignableLocations.size());
                        Location assignedLocation = assignableLocations.remove(randomIndex);

                        OrderRecipient orderRecipient = new OrderRecipient();
                        orderRecipient.setPersonId(person.getPersonId());
                        orderRecipient.setLocationId(assignedLocation.getLocationId());

                        orderRecipientDAO.create(orderRecipient);
                    } else {
                        final String NO_MORE_LOCATIONS_EXCEPTION_MSG =
                                "There are no more locations that can be assigned to an order recipient";
                        LOGGER.error(NO_MORE_LOCATIONS_EXCEPTION_MSG);
                        throw new NoMoreAssignableLocationsException(NO_MORE_LOCATIONS_EXCEPTION_MSG);
                    }
                });

        LOGGER.info("Order recipients data loaded successfully.");

        return assignableLocations;
    }

    public static void loadOrdersData() {
        final OrderDAO orderDAO = DAOFactory.createDAO(ClassConstants.ORDER_DAO);
        Random random = new Random();


        // used to store
        int currentStorageIndex = 0;
        for (int i = 0; i < OrderConstants.TOTAL_NUMBER_OF_ORDERS; i++) {
            // move to next storage ID after every 960 orders, and the TOTAL_NUMBER_OF_ORDERS_PER_WAREHOUSE
            // is perfectly divisible by 960
            if (
                    i % OrderConstants.TOTAL_NUMBER_OF_ORDERS_PER_WAREHOUSE == 0
                            && i > 0
            ) {
                currentStorageIndex++;
            }

            Order order = new Order();

            int currentOrderNumeral = i + 1;
            String orderNumber = "ORD" + currentOrderNumeral;
            order.setOrderNumber(orderNumber);
            order.setOrderStatus(OrderConstants.ORDER_STATUS_AWAITING_DELIVERY);

            // assign the storageId to whatever the current storage index is
            int storageId = OrderConstants.ALL_STORAGE_WAREHOUSE_IDS.get(currentStorageIndex);
            order.setStorageId(storageId);

            // assign a random orderRecipientId -- shift the '0' index up by 1
            int orderRecipientId = 1 + random.nextInt(OrderConstants.TOTAL_NUMBER_OF_ORDER_RECIPIENTS);


            order.setOrderRecipientId(orderRecipientId);

            // just set time to now by default
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            order.setOrderDate(currentTime);

            // delivery time not set yet
            order.setDeliveryDate(null);

            // set driverId to -1 as a signal to be null, and then it
            // will be set to null in the create() method
            order.setDriverId(-1);

            orderDAO.create(order);
        }

        LOGGER.info("Orders data loaded successfully.");
    }


    private LoadUtils() {
        ExceptionUtils.preventUtilityInstantiation();
    }
}
