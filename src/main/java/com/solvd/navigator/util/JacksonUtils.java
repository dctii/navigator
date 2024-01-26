package com.solvd.navigator.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.solvd.navigator.bin.Location;
import com.solvd.navigator.bin.Order;
import com.solvd.navigator.bin.OrderRecipient;
import com.solvd.navigator.bin.Storage;
import com.solvd.navigator.exception.ReadJsonFailureException;
import com.solvd.navigator.exception.WriteToJsonFailureException;
import com.solvd.navigator.math.util.OrderConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JacksonUtils {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.JACKSON_UTILS);

    /*
        Generic methods
    */
    public static <T> List<T> extractItems(String resourcePath, Class<T> clazz) {
        return parseJson(resourcePath, clazz);
    }


    public static <T> List<T> parseJson(String resourcePath, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        JavaType type = mapper
                .getTypeFactory()
                .constructCollectionType(ClassConstants.JAVA_UTIL_LIST, clazz);

        try (
                InputStream inputStream = ClassConstants.JACKSON_UTILS
                        .getClassLoader()
                        .getResourceAsStream(resourcePath)
        ) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            return mapper.readValue(inputStream, type);
        } catch (IOException e) {
            LOGGER.error("Error reading resource file: " + resourcePath, e);
            throw new ReadJsonFailureException("Error reading resource file: " + resourcePath + e);
        }
    }

    public static <T> void writeToJSON(String filepath, List<T> items) {
        ObjectMapper mapper = new ObjectMapper();

        SimpleDateFormat dateFormat = new SimpleDateFormat(StringConstants.TIMESTAMP_PATTERN);
        mapper.setDateFormat(dateFormat);

        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();

        try {

            String filepathFromResourcesDir =
                    FilepathConstants.RESOURCES_ABSOLUTE_PATH + filepath;

            File jsonToWriteTo = new File(filepathFromResourcesDir);


            writer.writeValue(jsonToWriteTo, items);
        } catch (IOException e) {
            LOGGER.error("Error writing to file: " + filepath, e);
            throw new WriteToJsonFailureException("Error writing to file: " + filepath + e);
        }
    }

    /*
        Spec methods for storages.json
    */

    public static Storage getStorageById(int storageId, List<Storage> storages) {
        return storages.stream()
                .filter(storage -> storage.getStorageId() == storageId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Target storage not found"));
    }

    public static Storage getRandomStorage(List<Storage> storages) {
        return CollectionUtils.getRandomItemFromList(storages);
    }


    public static Storage getStorageByLocationId(int locationId, List<Storage> storages) {
        return storages.stream()
                .filter(storage -> storage.getLocationId() == locationId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Target storage not found"));
    }

    public static void updateStoragesWithLocations(String filepath, List<Storage> storages) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat(StringConstants.TIMESTAMP_PATTERN));

        try {
            // read the existing storages from the JSON file
            File jsonFile = new File(FilepathConstants.RESOURCES_ABSOLUTE_PATH + filepath);
            List<Storage> currentStorages = mapper.readValue(jsonFile, new TypeReference<List<Storage>>() {
            });

            // create a map to make storages easier to look for
            Map<Integer, Storage> storageMap = currentStorages.stream()
                    .collect(Collectors.toMap(
                                    Storage::getStorageId,
                                    storage -> storage
                            )
                    );

            storages.stream()
                    .forEach(storage -> {
                        Storage currentStorage = storageMap.get(storage.getStorageId());
                        if (currentStorage != null) {
                            currentStorage.setLocationId(storage.getLocationId());
                        }
                    });


            // Write the updated list back to the JSON file
            mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, currentStorages);
        } catch (IOException e) {
            LOGGER.error("Error updating storages in file: " + filepath, e);
            throw new WriteToJsonFailureException("Error updating storages in file: " + filepath + e);
        }
    }

    /*
        Methods for locations.json
    */


    private static List<Location> getListOfOrderLocations(
            List<Order> orders,
            List<OrderRecipient> orderRecipients,
            List<Location> allLocations
    ) {
        return orders.stream()
                .map(order ->
                        getLocationByOrder(order, orderRecipients, allLocations)
                )
                .collect(Collectors.toList());
    }

    private static Location getLocationByOrder(Order order, List<OrderRecipient> orderRecipients, List<Location> allLocations) {
        OrderRecipient orderRecipient = getOrderRecipientById(order.getOrderRecipientId(), orderRecipients);
        return getLocationByOrderRecipient(orderRecipient, allLocations);
    }

    private static Location getLocationByOrderRecipient(OrderRecipient orderRecipient, List<Location> allLocations) {
        return getLocationById(orderRecipient.getLocationId(), allLocations);
    }


    public static Location getLocationById(int locationId, List<Location> locations) {
        return locations.stream()
                .filter(location -> location.getLocationId() == locationId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Location not found"));
    }

    public static Location getLocationByStorage(Storage storage, List<Location> locations) {
        return getLocationById(storage.getLocationId(), locations);
    }

    /*
        Methods for orderRecipients.json
    */

    private static OrderRecipient getOrderRecipientById(int orderRecipientId, List<OrderRecipient> orderRecipients) {
        return orderRecipients.stream()
                .filter(orderRecipient -> orderRecipient.getOrderRecipientId() == orderRecipientId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Order recipient not found"));
    }

    // uses orders to get locations from order recipients
    public static List<Location> extractAwaitingOrderLocationsForRoute(
            Storage targetStorage,
            List<Order> filteredOrders,
            List<OrderRecipient> orderRecipients,
            List<Location> allLocations
    ) {
        int targetStorageId = targetStorage.getStorageId();

        // Use getListOfOrderLocations to get the list of locations
        return getListOfOrderLocations(filteredOrders, orderRecipients, allLocations);
    }

    /*
        Methods for orders.json
    */

    public static List<Order> getAwaitingOrdersFromStorage(List<Order> allOrders, Storage targetStorage, int orderLimit) {
        int targetStorageId = targetStorage.getStorageId();

        return allOrders.stream()
                .filter(order ->
                        order.getStorageId() == targetStorageId
                                && order.getOrderStatus()
                                .equalsIgnoreCase(OrderConstants.ORDER_STATUS_AWAITING_DELIVERY)
                )
                .limit(orderLimit)
                .collect(Collectors.toList());
    }

    public static void updateOrderInJsonById(String filepath, Order updatedOrder) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat(StringConstants.TIMESTAMP_PATTERN));

        try {
            // read existing orders
            File jsonFile = new File(FilepathConstants.RESOURCES_ABSOLUTE_PATH + filepath);
            List<Order> orders = mapper.readValue(jsonFile, new TypeReference<List<Order>>() {
            });

            // find and update the specific order
            IntStream.range(0, orders.size())
                    .filter(i ->
                            orders.get(i).getOrderId() == updatedOrder.getOrderId()
                    )
                    .findFirst()
                    .ifPresent(i -> orders.set(i, updatedOrder));

            // Write the updated list back to the file
            mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, orders);
        } catch (IOException e) {
            LOGGER.error("Error updating order in file: " + filepath, e);
            throw new WriteToJsonFailureException("Error updating order in file: " + filepath + e);
        }
    }


    private JacksonUtils() {
        ExceptionUtils.preventUtilityInstantiation();
    }
}
