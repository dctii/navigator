package com.solvd.airport.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.solvd.airport.domain.Airline;
import com.solvd.airport.domain.Booking;
import com.solvd.airport.exception.ReadJsonFailureException;
import com.solvd.airport.exception.WriteToJsonFailureException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

public class JacksonUtils {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.JACKSON_UTILS);

    public static List<Booking> extractBookings(String resourcePath) {
        return extractItems(resourcePath, ClassConstants.BOOKING);
    }


    public static Booking getBookingByBookingNumber(String bookingNumber) {
        return getBookingByBookingNumber(FilepathConstants.BOOKINGS_JSON, bookingNumber);
    }

    public static Booking getBookingByBookingNumber(String filepath, String bookingNumber) {
        List<Booking> bookings = extractBookings(filepath);
        return bookings.stream()
                .filter(booking -> booking.getBookingNumber().equals(bookingNumber))
                .findFirst()
                .orElse(null);
    }

    public static void updateBookingByBookingNumber(Booking updatedBooking) {
        List<Booking> bookings = extractBookings(FilepathConstants.BOOKINGS_JSON);
        bookings.stream()
                .filter(booking ->
                        booking.getBookingNumber().equals(updatedBooking.getBookingNumber())
                )
                .findFirst()
                .ifPresent(booking -> updateBookingObj(updatedBooking, booking));

        writeToJSON(FilepathConstants.BOOKINGS_JSON, bookings);
    }

    private static void updateBookingObj(Booking updatedBooking, Booking booking) {
        booking.setBookingId(updatedBooking.getBookingId());
        booking.setBookingNumber(updatedBooking.getBookingNumber());
        booking.setPurchaseDatetime(updatedBooking.getPurchaseDatetime());
        booking.setSeatClass(updatedBooking.getSeatClass());
        booking.setSeatNumber(updatedBooking.getSeatNumber());
        booking.setStatus(updatedBooking.getStatus());
        booking.setPrice(updatedBooking.getPrice());
        booking.setAgency(updatedBooking.getAgency());
        booking.setPassportNumber(updatedBooking.getPassportNumber());
        booking.setFlightCode(updatedBooking.getFlightCode());
    }

    public static List<Airline> extractAirlines(String resourcePath) {
        return extractItems(resourcePath, ClassConstants.AIRLINE);
    }

    public static Airline getAirlineByCode(String airlineCode) {
        return getAirlineByCode(FilepathConstants.AIRLINES_JSON, airlineCode);
    }

    public static Airline getAirlineByCode(String filepath, String airlineCode) {
        List<Airline> airlines = extractAirlines(filepath);
        return airlines.stream()
                .filter(airline -> airlineCode.equals(airline.getAirlineCode()))
                .findFirst()
                .orElse(null);
    }

    public static Airline getAirlineByName(String airlineName) {
        return getAirlineByName(FilepathConstants.AIRLINES_JSON, airlineName);
    }

    public static Airline getAirlineByName(String filepath, String airlineName) {
        List<Airline> airlines = extractAirlines(filepath);
        return airlines.stream()
                .filter(airline -> airlineName.equals(airline.getAirlineName()))
                .findFirst()
                .orElse(null);
    }

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

    private JacksonUtils() {
        ExceptionUtils.preventUtilityInstantiation();
    }
}
