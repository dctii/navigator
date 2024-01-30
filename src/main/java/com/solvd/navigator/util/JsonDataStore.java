package com.solvd.navigator.util;

import com.solvd.navigator.bin.Driver;
import com.solvd.navigator.bin.Employee;
import com.solvd.navigator.bin.Item;
import com.solvd.navigator.bin.Location;
import com.solvd.navigator.bin.OrderRecipient;
import com.solvd.navigator.bin.Person;
import com.solvd.navigator.bin.Storage;
import com.solvd.navigator.bin.Vehicle;
import com.solvd.navigator.math.graph.GraphConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class JsonDataStore {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.JSON_DATA_STORE);
    public static List<Driver> allDrivers =
            JacksonUtils.extractItems(FilepathConstants.DRIVERS_JSON, Driver.class);

    public static List<Employee> allEmployees =
            JacksonUtils.extractItems(FilepathConstants.EMPLOYEES_JSON, Employee.class);
    public static List<Item> allItems =
            JacksonUtils.extractItems(FilepathConstants.ITEMS_JSON, Item.class);
    public static List<Location> allLocations =
            JacksonUtils.extractItems(FilepathConstants.LOCATIONS_JSON, Location.class);

    public static List<Location> allAvailableLocations = allLocations.stream()
            .filter(location -> !GraphConstants.EXCLUDED_LOCATION_IDS.contains(location.getLocationId()))
            .collect(Collectors.toList());
    public static List<OrderRecipient> allOrderRecipients =
            JacksonUtils.extractItems(FilepathConstants.ORDER_RECIPIENTS_JSON, OrderRecipient.class);
    public static List<Person> allPersons =
            JacksonUtils.extractItems(FilepathConstants.PERSONS_JSON, Person.class);
    public static List<Storage> allStorages =
            JacksonUtils.extractItems(FilepathConstants.STORAGES_JSON, Storage.class);

    public static List<Vehicle> allVehicles =
            JacksonUtils.extractItems(FilepathConstants.VEHICLES_JSON, Vehicle.class);


    /*
        public static List<Order> allOrders =
            JacksonUtils.extractItems(FilepathConstants.ORDERS_JSON, Order.class);

        public static List<OrderItem> allOrderItems =
            JacksonUtils.extractItems(FilepathConstants.ORDER_ITEMS_JSON, OrderItem.class);

     */

    private JsonDataStore() {
        ExceptionUtils.preventConstantsInstantiation();
    }
}
