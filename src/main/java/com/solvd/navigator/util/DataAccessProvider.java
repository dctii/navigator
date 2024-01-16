package com.solvd.airport.util;

import com.solvd.airport.exception.GetDaoFailureException;
import com.solvd.airport.persistence.AddressDAO;
import com.solvd.airport.persistence.AirlineDAO;
import com.solvd.airport.persistence.AirlineStaffMemberDAO;
import com.solvd.airport.persistence.AirportDAO;
import com.solvd.airport.persistence.BaggageDAO;
import com.solvd.airport.persistence.BoardingPassDAO;
import com.solvd.airport.persistence.BookingDAO;
import com.solvd.airport.persistence.CheckInDAO;
import com.solvd.airport.persistence.CountryDAO;
import com.solvd.airport.persistence.EmailAddressDAO;
import com.solvd.airport.persistence.FlightDAO;
import com.solvd.airport.persistence.GateDAO;
import com.solvd.airport.persistence.PassportDAO;
import com.solvd.airport.persistence.PersonInfoDAO;
import com.solvd.airport.persistence.PhoneNumberDAO;
import com.solvd.airport.persistence.TerminalDAO;
import com.solvd.airport.persistence.TimezoneDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public final class DataAccessProvider {

    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.DATA_ACCESS_PROVIDER);
    private static final String databaseImpl;


    static {
        Properties config = ConfigLoader.loadProperties(
                ClassConstants.DATA_ACCESS_PROVIDER,
                ConfigConstants.CONFIG_PROPS_FILE_NAME
        );
        databaseImpl = config.getProperty(ConfigConstants.DATABASE_IMPLEMENTATION);
    }

    public static <T> T getDAO(Class<T> daoInterface) {
        String implClassName = getImplClassName(daoInterface);

        try {
            Class<?> implClass = Class.forName(implClassName);
            return daoInterface.cast(implClass.getDeclaredConstructor().newInstance());
        } catch (
                ClassNotFoundException
                | InstantiationException
                | IllegalAccessException
                | NoSuchMethodException
                | InvocationTargetException e) {
            throw new GetDaoFailureException("Error creating DAO implementation for " + daoInterface.getName() + e);
        }
    }

    public static AddressDAO getAddressDAO() {
        return getDAO(ClassConstants.ADDRESS_DAO);
    }

    public static AirlineDAO getAirlineDAO() {
        return getDAO(ClassConstants.AIRLINE_DAO);
    }

    public static AirportDAO getAirportDAO() {
        return getDAO(ClassConstants.AIRPORT_DAO);
    }

    public static AirlineStaffMemberDAO getAirlineStaffMemberDAO() {
        return getDAO(ClassConstants.AIRLINE_STAFF_MEMBER_DAO);
    }

    public static BaggageDAO getBaggageDAO() {
        return getDAO(ClassConstants.BAGGAGE_DAO);
    }

    public static BoardingPassDAO getBoardingPassDAO() {
        return getDAO(ClassConstants.BOARDING_PASS_DAO);
    }

    public static BookingDAO getBookingDAO() {
        return getDAO(ClassConstants.BOOKING_DAO);
    }

    public static CheckInDAO getCheckInDAO() {
        return getDAO(ClassConstants.CHECK_IN_DAO);
    }

    public static EmailAddressDAO getEmailAddressDAO() {
        return getDAO(ClassConstants.EMAIL_ADDRESS_DAO);
    }

    public static FlightDAO getFlightDAO() {
        return getDAO(ClassConstants.FLIGHT_DAO);
    }

    public static GateDAO getGateDAO() {
        return getDAO(ClassConstants.GATE_DAO);
    }

    public static PassportDAO getPassportDAO() {
        return getDAO(ClassConstants.PASSPORT_DAO);
    }

    public static CountryDAO getCountryDAO() {
        return getDAO(ClassConstants.COUNTRY_DAO);
    }

    public static TerminalDAO getTerminalDAO() {
        return getDAO(ClassConstants.TERMINAL_DAO);
    }

    public static TimezoneDAO getTimezoneDAO() {
        return getDAO(ClassConstants.TIMEZONE_DAO);
    }


    public static PersonInfoDAO getPersonInfoDAO() {
        return getDAO(ClassConstants.PERSON_INFO_DAO);
    }

    public static PhoneNumberDAO getPhoneNumberDAO() {
        return getDAO(ClassConstants.PHONE_NUMBER_DAO);
    }

    private static <T> String getImplClassName(Class<T> daoInterface) {
        String daoInterfaceSimpleName = daoInterface.getSimpleName();
        if (daoInterfaceSimpleName.contains(ConfigConstants.DAO_CLASS_SUBSTRING)) {
            daoInterfaceSimpleName = daoInterfaceSimpleName
                    .replace(
                            ConfigConstants.DAO_CLASS_SUBSTRING,
                            StringConstants.EMPTY_STRING
                    );
        }

        return ConfigConstants.DATABASE_IMPLEMENTATION_VAL_MYBATIS.equals(databaseImpl)
                ? ConfigConstants.DAO_MYBATIS_IMPL_PACKAGE + daoInterfaceSimpleName + ConfigConstants.MYBATIS_IMPL_SUFFIX
                : ConfigConstants.DAO_JDBC_IMPL_PACKAGE + daoInterfaceSimpleName + ConfigConstants.JDBC_IMPL_SUFFIX;
    }

    private DataAccessProvider() {
        ExceptionUtils.preventConstantsInstantiation();
    }
}
