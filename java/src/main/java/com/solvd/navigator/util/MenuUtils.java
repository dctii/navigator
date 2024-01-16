package com.solvd.airport.util;

import com.solvd.airport.domain.Address;
import com.solvd.airport.domain.Airline;
import com.solvd.airport.domain.Airport;
import com.solvd.airport.domain.Booking;
import com.solvd.airport.domain.Country;
import com.solvd.airport.domain.EmailAddress;
import com.solvd.airport.domain.Gate;
import com.solvd.airport.domain.GateCollection;
import com.solvd.airport.domain.Passport;
import com.solvd.airport.domain.PersonInfo;
import com.solvd.airport.domain.PhoneNumber;
import com.solvd.airport.domain.Terminal;
import com.solvd.airport.domain.Timezone;
import com.solvd.airport.persistence.AddressDAO;
import com.solvd.airport.persistence.AirlineDAO;
import com.solvd.airport.persistence.AirportDAO;
import com.solvd.airport.persistence.BookingDAO;
import com.solvd.airport.persistence.CountryDAO;
import com.solvd.airport.persistence.GateDAO;
import com.solvd.airport.persistence.TerminalDAO;
import com.solvd.airport.persistence.TimezoneDAO;
import com.solvd.airport.service.BoardPassengerService;
import com.solvd.airport.service.CheckInService;
import com.solvd.airport.service.RegisterPassportHolderService;
import com.solvd.airport.service.UpdateFlightGateService;
import com.solvd.airport.service.impl.BoardPassengerServiceImpl;
import com.solvd.airport.service.impl.CheckInServiceImpl;
import com.solvd.airport.service.impl.RegisterPassportHolderServiceImpl;
import com.solvd.airport.service.impl.UpdateFlightGateServiceImpl;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.stream.StreamSupport;

public class MenuUtils {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.MENU_UTILS);

    private static final CheckInService checkInService = new CheckInServiceImpl();
    private static final BoardPassengerService boardPassengerService = new BoardPassengerServiceImpl();
    private static final RegisterPassportHolderService registerPassportHolderService = new RegisterPassportHolderServiceImpl();

    private static final UpdateFlightGateService updateFlightGateService = new UpdateFlightGateServiceImpl();


    public static void registerPassportHolder(Scanner scanner) {
        LOGGER.info("{}{}=== Register Passport Holder: ==={}",
                AnsiCodes.BOLD, AnsiCodes.YELLOW, AnsiCodes.RESET_ALL);

        PersonInfo personInfo = getPersonInfo(scanner);
        Passport passport = getPassport(scanner);
        Address address = getAddress(scanner);
        PhoneNumber phone = getPhoneNumber(scanner);
        EmailAddress email = getEmailAddress(scanner);

        // call RegisterPassportHolderService function
        LOGGER.info("Registering passport holder info");
        registerPassportHolderService.registerPassportHolder(
                personInfo,
                passport,
                address,
                phone,
                email
        );

        LOGGER.info("{}Registration completed successfully.{}\n", AnsiCodes.GREEN, AnsiCodes.RESET_ALL);
    }

    private static EmailAddress getEmailAddress(Scanner scanner) {
        String emailAddress = ScannerUtils.checkAndCleanInput(scanner, "Enter Email Address (e.g., 'example@domain.com'):",
                String::toLowerCase,
                BooleanUtils::isValidEmail
        );

        EmailAddress email = new EmailAddress(emailAddress);
        return email;
    }

    private static PhoneNumber getPhoneNumber(Scanner scanner) {
        String phoneNumber = ScannerUtils.getAndCheckInput(scanner, "Enter Phone Number (include country code, e.g., +15551234567):",
                input -> input.startsWith(StringConstants.PLUS_SIGN));

        PhoneNumber phone = new PhoneNumber(phoneNumber);
        return phone;
    }

    private static Address getAddress(Scanner scanner) {
        String street = ScannerUtils.getAndCleanInput(scanner, "Enter Street Address:", String::toUpperCase);
        String city = ScannerUtils.getAndCleanInput(scanner, "Enter City:", String::toUpperCase);
        String postalCode = ScannerUtils.getInput(scanner, "Enter Postal Code");
        String countryCode = ScannerUtils.checkAndCleanCountryCode(scanner, "Enter Country Code (e.g., US, JP):",
                String::toUpperCase,
                input -> {
                    // Sets input to uppercase and checks if it matches the list of ISO country codes
                    String upperCaseInput = input.toUpperCase();
                    return input.length() == 2
                            && Arrays.asList(Locale.getISOCountries()).contains(upperCaseInput);
                });

        Address address = new Address(street, city, postalCode, countryCode);
        return address;
    }

    private static Passport getPassport(Scanner scanner) {
        String passportNumber = ScannerUtils.getAndCleanPassportNum(scanner, "Enter Passport Number:", String::toUpperCase);
        Date issueDate = ScannerUtils.getDateInput(scanner, "Enter Passport Issue Date (YYYY-MM-DD):", StringConstants.YEAR_FIRST_DATE_PATTERN);
        Date expiryDate = ScannerUtils.getDateInput(scanner, "Enter Passport Expiry Date (YYYY-MM-DD):", StringConstants.YEAR_FIRST_DATE_PATTERN);

        Passport passport = new Passport(passportNumber, issueDate, expiryDate);
        return passport;
    }

    private static PersonInfo getPersonInfo(Scanner scanner) {
        String surname = ScannerUtils.getAndCleanInput(scanner, "Enter Surname:", String::toUpperCase);
        String givenName = ScannerUtils.getAndCleanInput(scanner, "Enter Given Name:", String::toUpperCase);
        String middleName = ScannerUtils.getAndCleanInput(scanner, "Enter Middle Name (or type 'none' if not applicable):", String::toUpperCase);
        if ("none".equalsIgnoreCase(middleName) || "".equals(middleName)) {
            middleName = null;
        }
        Date birthdate = ScannerUtils.getDateInput(scanner, "Enter Birthdate (YYYY-MM-DD):", StringConstants.YEAR_FIRST_DATE_PATTERN);
        String sex = ScannerUtils.checkAndCleanInput(scanner, "Enter Sex (M/F):",
                String::toUpperCase,
                input -> input.matches(RegExpConstants.UPPERCASE_M_OR_F));

        PersonInfo personInfo = new PersonInfo(surname, givenName, middleName, birthdate, sex);
        return personInfo;
    }

    public static void performCheckIn(Scanner scanner) {
        String staffEmail = ScannerUtils.checkAndCleanStaffEmail(scanner, "Enter Airline Staff Email (i.e. 'paul_blart@delta.com' or 'emi_sato@air-japan.co.jp':",
                String::toLowerCase,
                BooleanUtils::isValidEmail);

        String bookingNumber = ScannerUtils.getAndCleanBookingNum(scanner, "Enter Booking Number (i.e. EXPEDIA001, KAYAK001, KAYAK002):", String::toUpperCase);

        String hasBaggageString = ScannerUtils.checkAndCleanInput(scanner, "Does the passenger have baggage? Y/N",
                String::toLowerCase,
                input -> input.matches(RegExpConstants.LOWERCASE_Y_OR_N)
        );

        boolean hasBaggage = hasBaggageString.equalsIgnoreCase("y");
        double weight = 0.00;

        if (hasBaggage) {
            weight = Double.parseDouble(
                    ScannerUtils.getAndCheckInput(scanner,
                            "How much does the baggage weigh? Type in the weight (e.g., 23 or 23.00)",
                            input -> input.matches(RegExpConstants.DECIMAL_WITH_SCALE_OF_0_OR_2)
                    )
            );
        }
        checkInService.performCheckIn(staffEmail, bookingNumber, hasBaggage, weight);
        LOGGER.info("{}Check-In completed successfully.{}\n", AnsiCodes.GREEN, AnsiCodes.RESET_ALL);
    }

    public static void boardPassenger(Scanner scanner) {
        LOGGER.info("=== Board Passenger onto Plane ===");

        String bookingNumber = ScannerUtils.getAndCleanInput(scanner,
                "Enter Booking Number for Boarding (i.e. EXPEDIA001, KAYAK001, KAYAK002--must be checked-in first):",
                String::toUpperCase
        );

        boolean wasBoardingSuccessful = boardPassengerService.boardPassenger(bookingNumber);

        if (wasBoardingSuccessful) {
            LOGGER.info(
                    "{}Boarding completed successfully.{}\n",
                    AnsiCodes.GREEN,
                    AnsiCodes.RESET_ALL
            )
            ;
        } else {
            LOGGER.info(
                    "{}Boarding unsuccessful due to an invalid association with the inserted Booking Number. Try again.{}\n",
                    AnsiCodes.YELLOW, AnsiCodes.RESET_ALL
            );
        }
    }

    public static void updateFlightGate(Scanner scanner) {
        LOGGER.info("=== Update Departure Gate for Flight ===");

        String flightCode = ScannerUtils.checkAndCleanInput(scanner,
                "Enter Flight Code:",
                String::toUpperCase,
                SQLUtils::doesFlightExist
        );

        String airportCode = ScannerUtils.checkAndCleanInput(scanner,
                "Enter Airport Code (i.e. LAX):",
                String::toUpperCase,
                SQLUtils::doesAirportExist
        );

        String gateCode = ScannerUtils.checkAndCleanInput(scanner,
                "Enter Gate Code (i.e. 130-159, 201A, 201B, 202-204):",
                String::toUpperCase,
                input -> SQLUtils.doesGateExist(input, airportCode)
        );

        Gate newGate = new Gate(gateCode, airportCode);

        // uses JAXB
        updateFlightGateService.updateFlightGate(flightCode, newGate);


    }

    public static void loadCountryData() {
        final CountryDAO countryDAO = DataAccessProvider.getCountryDAO();

        LOGGER.info(
                "{}Loading country data...{}",
                AnsiCodes.YELLOW, AnsiCodes.RESET_ALL
        );
        Arrays.stream(Locale.getISOCountries()).forEach(code -> {
            String countryName = getCountryNameByIsoCode(code).trim();
            if (countryDAO.doesCountryCodeExist(code)) {

                Country existingCountry = countryDAO.getCountryByCode(code);
                if (!existingCountry.getCountryName().equals(countryName)) {
                    LOGGER.info(
                            "{}'{}' already exists, overwriting country name with '{}'.{}",
                            AnsiCodes.YELLOW, existingCountry.getCountryName(), countryName, AnsiCodes.RESET_ALL
                    );
                    existingCountry.setCountryName(countryName);
                    countryDAO.update(existingCountry);
                } else {
                    LOGGER.info(
                            "{}'{}' already exists, skipping.{}",
                            AnsiCodes.YELLOW, existingCountry.getCountryName(), AnsiCodes.RESET_ALL
                    );
                }
            } else {
                Country newCountry = new Country();
                newCountry.setCountryCode(code);
                newCountry.setCountryName(countryName);

                countryDAO.create(newCountry);
            }
        });
        LOGGER.info(
                "{}Country Data Loaded Successfully.{}",
                AnsiCodes.GREEN, AnsiCodes.RESET_ALL
        );
    }

    private static String getCountryNameByIsoCode(String code) {
        Locale locale = new Locale("", code);
        return locale.getDisplayCountry();
    }


    public static void loadTimeZones() {
        final TimezoneDAO timezoneDAO = DataAccessProvider.getTimezoneDAO();

        LOGGER.info(
                "{}Loading Time Zones...{}",
                AnsiCodes.YELLOW, AnsiCodes.RESET_ALL
        );

        Arrays.stream(TimeZone.getAvailableIDs()).forEach(tzString -> {
            if (!timezoneDAO.doesTimezoneExist(tzString)) {
                Timezone timezone = new Timezone();
                timezone.setTimezone(tzString);
                timezoneDAO.create(timezone);
            } else {
                LOGGER.info(
                        "{}'{}' already exists, skipping.{}",
                        AnsiCodes.YELLOW, tzString, AnsiCodes.RESET_ALL
                );
            }
        });

        LOGGER.info(
                "{}Time Zones Loaded Successfully.{}",
                AnsiCodes.GREEN, AnsiCodes.RESET_ALL
        );
    }

    public static void loadLargeAirports(String csvResourcePath) {
        CSVParser csvParser = null;
        final AirportDAO airportDAO = DataAccessProvider.getAirportDAO();

        LOGGER.info(
                "{}Loading 'large airports' IATA codes and Airport names into database...{}",
                AnsiCodes.YELLOW, AnsiCodes.RESET_ALL
        );

        try {
            InputStream inputStream = ClassConstants.MENU_UTILS.getClassLoader().getResourceAsStream(csvResourcePath);
            if (inputStream == null) {
                throw new FileNotFoundException("CSV File for airports not found in resources");
            }

            Reader reader = new InputStreamReader(
                    inputStream,
                    StandardCharsets.UTF_8
            );

            final String TYPE_HEADER_NAME = "type";
            final String LARGE_AIRPORT_TYPE = "large_airport";
            final String IATA_CODE_HEADER_NAME = "iata_code";
            final String AIRPORT_NAME_HEADER_NAME = "name";

            String[] csvHeaders = {
                    "id", "ident", TYPE_HEADER_NAME,
                    AIRPORT_NAME_HEADER_NAME, "latitude_deg",
                    "longitude_deg", "elevation_ft",
                    "continent", "iso_country",
                    "iso_region", "municipality",
                    "scheduled_service", "gps_code",
                    IATA_CODE_HEADER_NAME, "local_code",
                    "home_link", "wikipedia_link",
                    "keywords"
            };

            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader(csvHeaders)
                    .setSkipHeaderRecord(true)
                    .build();

            csvParser = new CSVParser(reader, csvFormat);

            StreamSupport.stream(csvParser.spliterator(), false)
                    .filter(csvRecord -> LARGE_AIRPORT_TYPE.equalsIgnoreCase(csvRecord.get(TYPE_HEADER_NAME)))
                    .forEach(csvRecord -> {
                        String airportIataCode = csvRecord.get(IATA_CODE_HEADER_NAME);
                        String airportName = csvRecord.get(AIRPORT_NAME_HEADER_NAME);

                        if (!airportDAO.doesAirportExist(airportIataCode)) {
                            Airport airport = new Airport();
                            airport.setAirportCode(airportIataCode);
                            airport.setAirportName(airportName);

                            airportDAO.create(airport);
                        } else {
                            LOGGER.info(
                                    "{}'{}' already exists, skipping.{}",
                                    AnsiCodes.YELLOW, airportName, AnsiCodes.RESET_ALL
                            );
                        }
                    });

        } catch (FileNotFoundException e) {
            LOGGER.error("CSV File not found: ", e);
        } catch (IOException e) {
            LOGGER.error("Error reading the CSV File: ", e);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid data format in CSV File: ", e);
        }

        LOGGER.info(
                "{}Loading airport data successful{}",
                AnsiCodes.GREEN, AnsiCodes.RESET_ALL
        );
    }

    public static void loadAirlines(String jsonFilePath) {
        AirlineDAO airlineDAO = DataAccessProvider.getAirlineDAO();
        AddressDAO addressDAO = DataAccessProvider.getAddressDAO();


        List<Airline> airlines = JacksonUtils.extractAirlines(jsonFilePath);
        airlines.forEach(airline -> {
            createOrGetAddressForAirline(airline, addressDAO);
            createAirlineIfNotExists(airline, airlineDAO);
        });
        LOGGER.info(
                "{}Airlines data loaded successfully.{}",
                AnsiCodes.GREEN, AnsiCodes.RESET_ALL
        );
    }

    private static void createAirlineIfNotExists(Airline airline, AirlineDAO airlineDAO) {
        if (!airlineDAO.doesAirlineExist(airline.getAirlineCode())) {
            airlineDAO.create(airline);
        } else {
            LOGGER.info(
                    "{}'{}' already exists, skipping.{}",
                    AnsiCodes.YELLOW, airline.getAirlineName(), AnsiCodes.RESET_ALL
            );
        }
    }

    private static void createOrGetAddressForAirline(Airline airline, AddressDAO addressDAO) {
        Address address = airline.getAddress();
        if (address != null) {
            Address existingAddress = addressDAO.getAddressByUniqueFields(
                    address.getStreet(), address.getCity(),
                    address.getPostalCode(), address.getCountryCode()
            );
            if (existingAddress == null) {
                int newAddressId = addressDAO.create(address);
                address.setAddressId(newAddressId);
                LOGGER.info(
                        "{}New address created with ID: {}{}",
                        AnsiCodes.GREEN, newAddressId, AnsiCodes.RESET_ALL
                );
            } else {
                airline.setAddress(existingAddress);
                LOGGER.info(
                        "{}Existing address used with ID: {}{}",
                        AnsiCodes.BLUE, existingAddress.getAddressId(), AnsiCodes.RESET_ALL
                );
            }
        }
    }

    public static void loadTerminals(String xmlFilePath) {
        TerminalDAO terminalDAO = DataAccessProvider.getTerminalDAO();
        List<Terminal> terminals = StaxUtils.extractTerminals(xmlFilePath);
        terminals.stream()
                .filter(terminal -> !terminalDAO.doesTerminalExist(
                        terminal.getAirportCode(),
                        terminal.getTerminalCode()
                ))
                .forEach(terminalDAO::create);

        terminals.stream()
                .filter(terminal -> terminalDAO.doesTerminalExist(
                        terminal.getAirportCode(),
                        terminal.getTerminalCode()
                ))
                .forEach(terminal -> {
                    LOGGER.info(
                            "{} Terminal '{}' at '{}' already exists, skipping.{}",
                            AnsiCodes.YELLOW,
                            terminal.getTerminalCode(), terminal.getAirportCode(),
                            AnsiCodes.RESET_ALL
                    );
                });
        LOGGER.info(
                "{}Terminals data loaded successfully.{}",
                AnsiCodes.GREEN, AnsiCodes.RESET_ALL
        );
    }

    public static void loadGates(String xmlFilePath) {
        GateDAO gateDAO = DataAccessProvider.getGateDAO();
        GateCollection gateCollection = JaxbUtils.extractGates(xmlFilePath);

        gateCollection.getGates().stream()
                .filter(gate -> !gateDAO.doesGateExist(
                        gate.getGateCode(), gate.getAirportCode()
                ))
                .forEach(gateDAO::create);

        gateCollection.getGates().stream()
                .filter(gate -> gateDAO.doesGateExist(
                        gate.getGateCode(), gate.getAirportCode()
                ))
                .forEach(gate -> {
                    LOGGER.info(
                            "{} Gate '{}' at '{}' in '{}' already exists, skipping.{}",
                            AnsiCodes.YELLOW,
                            gate.getGateCode(), gate.getAirportCode(), gate.getTerminalCode(),
                            AnsiCodes.RESET_ALL
                    );
                });

        LOGGER.info(
                "{}Gates data loaded successfully.{}",
                AnsiCodes.GREEN, AnsiCodes.RESET_ALL
        );
    }

    public static void loadBookings(String jsonFilePath) {
        BookingDAO bookingDAO = DataAccessProvider.getBookingDAO();

        List<Booking> bookings = JacksonUtils.extractBookings(jsonFilePath);
        bookings.forEach(booking -> {
            createBookingIfNotExists(booking, bookingDAO);
        });
        LOGGER.info(
                "{}Bookings data loaded successfully.{}",
                AnsiCodes.GREEN, AnsiCodes.RESET_ALL
        );
    }

    private static void createBookingIfNotExists(Booking booking, BookingDAO bookingDAO) {
        if (!bookingDAO.doesBookingExist(booking.getBookingNumber())) {
            bookingDAO.create(booking);
        } else {
            LOGGER.info(
                    "{}'{}' already exists, skipping.{}",
                    AnsiCodes.YELLOW, booking.getBookingNumber(), AnsiCodes.RESET_ALL
            );
        }
    }


    private MenuUtils() {
        ExceptionUtils.preventUtilityInstantiation();
    }

}
