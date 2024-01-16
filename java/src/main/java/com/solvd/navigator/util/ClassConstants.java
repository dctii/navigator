package com.solvd.airport.util;


import com.solvd.airport.domain.Address;
import com.solvd.airport.domain.Airline;
import com.solvd.airport.domain.AirlineStaffMember;
import com.solvd.airport.domain.Airport;
import com.solvd.airport.domain.Baggage;
import com.solvd.airport.domain.BoardingPass;
import com.solvd.airport.domain.Booking;
import com.solvd.airport.domain.CheckIn;
import com.solvd.airport.domain.Country;
import com.solvd.airport.domain.EmailAddress;
import com.solvd.airport.domain.Flight;
import com.solvd.airport.domain.FlightStaffMember;
import com.solvd.airport.domain.Gate;
import com.solvd.airport.domain.GateCollection;
import com.solvd.airport.domain.Passport;
import com.solvd.airport.domain.PersonInfo;
import com.solvd.airport.domain.PhoneNumber;
import com.solvd.airport.domain.Terminal;
import com.solvd.airport.domain.Timezone;
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
import com.solvd.airport.persistence.FlightStaffMemberDAO;
import com.solvd.airport.persistence.GateDAO;
import com.solvd.airport.persistence.PassportDAO;
import com.solvd.airport.persistence.PersonInfoDAO;
import com.solvd.airport.persistence.PhoneNumberDAO;
import com.solvd.airport.persistence.TerminalDAO;
import com.solvd.airport.persistence.TimezoneDAO;
import com.solvd.airport.persistence.jdbc.AddressJDBCImpl;
import com.solvd.airport.persistence.jdbc.AirlineJDBCImpl;
import com.solvd.airport.persistence.jdbc.AirlineStaffMemberJDBCImpl;
import com.solvd.airport.persistence.jdbc.AirportJDBCImpl;
import com.solvd.airport.persistence.jdbc.BaggageJDBCImpl;
import com.solvd.airport.persistence.jdbc.BoardingPassJDBCImpl;
import com.solvd.airport.persistence.jdbc.BookingJDBCImpl;
import com.solvd.airport.persistence.jdbc.CheckInJDBCImpl;
import com.solvd.airport.persistence.jdbc.CountryJDBCImpl;
import com.solvd.airport.persistence.jdbc.EmailAddressJDBCImpl;
import com.solvd.airport.persistence.jdbc.FlightJDBCImpl;
import com.solvd.airport.persistence.jdbc.GateJDBCImpl;
import com.solvd.airport.persistence.jdbc.PassportJDBCImpl;
import com.solvd.airport.persistence.jdbc.PersonInfoJDBCImpl;
import com.solvd.airport.persistence.jdbc.PhoneNumberJDBCImpl;
import com.solvd.airport.persistence.jdbc.TerminalJDBCImpl;
import com.solvd.airport.persistence.jdbc.TimezoneJDBCImpl;
import com.solvd.airport.persistence.mybatis.AddressMyBatisImpl;
import com.solvd.airport.persistence.mybatis.AirlineMyBatisImpl;
import com.solvd.airport.persistence.mybatis.AirlineStaffMemberMyBatisImpl;
import com.solvd.airport.persistence.mybatis.AirportMyBatisImpl;
import com.solvd.airport.persistence.mybatis.BaggageMyBatisImpl;
import com.solvd.airport.persistence.mybatis.BoardingPassMyBatisImpl;
import com.solvd.airport.persistence.mybatis.BookingMyBatisImpl;
import com.solvd.airport.persistence.mybatis.CheckInMyBatisImpl;
import com.solvd.airport.persistence.mybatis.CountryMyBatisImpl;
import com.solvd.airport.persistence.mybatis.EmailAddressMyBatisImpl;
import com.solvd.airport.persistence.mybatis.FlightMyBatisImpl;
import com.solvd.airport.persistence.mybatis.GateMyBatisImpl;
import com.solvd.airport.persistence.mybatis.PassportMyBatisImpl;
import com.solvd.airport.persistence.mybatis.PersonInfoMyBatisImpl;
import com.solvd.airport.persistence.mybatis.PhoneNumberMyBatisImpl;
import com.solvd.airport.persistence.mybatis.TerminalMyBatisImpl;
import com.solvd.airport.persistence.mybatis.TimezoneMyBatisImpl;
import com.solvd.airport.service.BoardPassengerService;
import com.solvd.airport.service.CheckInService;
import com.solvd.airport.service.RegisterPassportHolderService;
import com.solvd.airport.service.UpdateFlightGateService;
import com.solvd.airport.service.impl.BoardPassengerServiceImpl;
import com.solvd.airport.service.impl.CheckInServiceImpl;
import com.solvd.airport.service.impl.RegisterPassportHolderServiceImpl;
import com.solvd.airport.service.impl.UpdateFlightGateServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public final class ClassConstants {

    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.class);
    // java.util
    public static final Class<List> JAVA_UTIL_LIST = List.class;

    // com.solvd.airport
    public static final Class<com.solvd.airport.BaseDataLoader> BASE_DATA_LOADER = com.solvd.airport.BaseDataLoader.class;
    public static final Class<com.solvd.airport.Main> MAIN = com.solvd.airport.Main.class;

    // com.solvd.airport.domain
    public static final Class<Address> ADDRESS = Address.class;
    public static final Class<Airline> AIRLINE = Airline.class;
    public static final Class<AirlineStaffMember> AIRLINE_STAFF_MEMBER = AirlineStaffMember.class;
    public static final Class<Airport> AIRPORT = Airport.class;
    public static final Class<Baggage> BAGGAGE = Baggage.class;
    public static final Class<BoardingPass> BOARDING_PASS = BoardingPass.class;
    public static final Class<Booking> BOOKING = Booking.class;
    public static final Class<CheckIn> CHECK_IN = CheckIn.class;
    public static final Class<Country> COUNTRY = Country.class;
    public static final Class<EmailAddress> EMAIL_ADDRESS = EmailAddress.class;
    public static final Class<Flight> FLIGHT = Flight.class;
    public static final Class<FlightStaffMember> FLIGHT_STAFF_MEMBER = FlightStaffMember.class;
    public static final Class<Gate> GATE = Gate.class;
    public static final Class<GateCollection> GATE_COLLECTION = GateCollection.class;
    public static final Class<Passport> PASSPORT = Passport.class;
    public static final Class<PersonInfo> PERSON_INFO = PersonInfo.class;
    public static final Class<PhoneNumber> PHONE_NUMBER = PhoneNumber.class;
    public static final Class<Terminal> TERMINAL = Terminal.class;
    public static final Class<Timezone> TIMEZONE = Timezone.class;

    // com.solvd.airport.persistence
    public static final Class<AddressDAO> ADDRESS_DAO = AddressDAO.class;
    public static final Class<AirlineDAO> AIRLINE_DAO = AirlineDAO.class;
    public static final Class<AirlineStaffMemberDAO> AIRLINE_STAFF_MEMBER_DAO = AirlineStaffMemberDAO.class;
    public static final Class<AirportDAO> AIRPORT_DAO = AirportDAO.class;
    public static final Class<BaggageDAO> BAGGAGE_DAO = BaggageDAO.class;
    public static final Class<BoardingPassDAO> BOARDING_PASS_DAO = BoardingPassDAO.class;
    public static final Class<BookingDAO> BOOKING_DAO = BookingDAO.class;
    public static final Class<CheckInDAO> CHECK_IN_DAO = CheckInDAO.class;
    public static final Class<CountryDAO> COUNTRY_DAO = CountryDAO.class;
    public static final Class<EmailAddressDAO> EMAIL_ADDRESS_DAO = EmailAddressDAO.class;
    public static final Class<FlightDAO> FLIGHT_DAO = FlightDAO.class;
    public static final Class<FlightStaffMemberDAO> FLIGHT_STAFF_MEMBER_DAO = FlightStaffMemberDAO.class;
    public static final Class<GateDAO> GATE_DAO = GateDAO.class;
    public static final Class<PassportDAO> PASSPORT_DAO = PassportDAO.class;
    public static final Class<PersonInfoDAO> PERSON_INFO_DAO = PersonInfoDAO.class;
    public static final Class<PhoneNumberDAO> PHONE_NUMBER_DAO = PhoneNumberDAO.class;
    public static final Class<TerminalDAO> TERMINAL_DAO = TerminalDAO.class;
    public static final Class<TimezoneDAO> TIMEZONE_DAO = TimezoneDAO.class;


    // com.solvd.airport.persistence.jdbc
    public static final Class<AddressJDBCImpl> ADDRESS_JDBC_IMPL = AddressJDBCImpl.class;
    public static final Class<AirlineJDBCImpl> AIRLINE_JDBC_IMPL = AirlineJDBCImpl.class;
    public static final Class<AirlineStaffMemberJDBCImpl> AIRLINE_STAFF_MEMBER_JDBC_IMPL = AirlineStaffMemberJDBCImpl.class;
    public static final Class<AirportJDBCImpl> AIRPORT_JDBC_IMPL = AirportJDBCImpl.class;
    public static final Class<BaggageJDBCImpl> BAGGAGE_JDBC_IMPL = BaggageJDBCImpl.class;
    public static final Class<BoardingPassJDBCImpl> BOARDING_PASS_JDBC_IMPL = BoardingPassJDBCImpl.class;
    public static final Class<BookingJDBCImpl> BOOKING_JDBC_IMPL = BookingJDBCImpl.class;
    public static final Class<CheckInJDBCImpl> CHECK_IN_JDBC_IMPL = CheckInJDBCImpl.class;
    public static final Class<CountryJDBCImpl> COUNTRY_JDBC_IMPL = CountryJDBCImpl.class;
    public static final Class<EmailAddressJDBCImpl> EMAIL_ADDRESS_JDBC_IMPL = EmailAddressJDBCImpl.class;
    public static final Class<FlightJDBCImpl> FLIGHT_JDBC_IMPL = FlightJDBCImpl.class;
    public static final Class<GateJDBCImpl> GATE_JDBC_IMPL = GateJDBCImpl.class;
    public static final Class<PassportJDBCImpl> PASSPORT_JDBC_IMPL = PassportJDBCImpl.class;
    public static final Class<PersonInfoJDBCImpl> PERSON_INFO_JDBC_IMPL = PersonInfoJDBCImpl.class;
    public static final Class<PhoneNumberJDBCImpl> PHONE_NUMBER_JDBC_IMPL = PhoneNumberJDBCImpl.class;
    public static final Class<TerminalJDBCImpl> TERMINAL_JDBC_IMPL = TerminalJDBCImpl.class;
    public static final Class<TimezoneJDBCImpl> TIMEZONE_JDBC_IMPL = TimezoneJDBCImpl.class;

    // com.solvd.airport.persistence.mybatis

    public static final Class<AddressMyBatisImpl> ADDRESS_MYBATIS_IMPL = AddressMyBatisImpl.class;
    public static final Class<AirlineMyBatisImpl> AIRLINE_MYBATIS_IMPL = AirlineMyBatisImpl.class;
    public static final Class<AirlineStaffMemberMyBatisImpl> AIRLINE_STAFF_MEMBER_MYBATIS_IMPL = AirlineStaffMemberMyBatisImpl.class;
    public static final Class<AirportMyBatisImpl> AIRPORT_MYBATIS_IMPL = AirportMyBatisImpl.class;
    public static final Class<BaggageMyBatisImpl> BAGGAGE_MYBATIS_IMPL = BaggageMyBatisImpl.class;
    public static final Class<BoardingPassMyBatisImpl> BOARDING_PASS_MYBATIS_IMPL = BoardingPassMyBatisImpl.class;
    public static final Class<BookingMyBatisImpl> BOOKING_MYBATIS_IMPL = BookingMyBatisImpl.class;
    public static final Class<CheckInMyBatisImpl> CHECK_IN_MYBATIS_IMPL = CheckInMyBatisImpl.class;
    public static final Class<CountryMyBatisImpl> COUNTRY_MYBATIS_IMPL = CountryMyBatisImpl.class;
    public static final Class<EmailAddressMyBatisImpl> EMAIL_ADDRESS_MYBATIS_IMPL = EmailAddressMyBatisImpl.class;
    public static final Class<FlightMyBatisImpl> FLIGHT_MYBATIS_IMPL = FlightMyBatisImpl.class;
    public static final Class<GateMyBatisImpl> GATE_MYBATIS_IMPL = GateMyBatisImpl.class;
    public static final Class<PassportMyBatisImpl> PASSPORT_MYBATIS_IMPL = PassportMyBatisImpl.class;
    public static final Class<PersonInfoMyBatisImpl> PERSON_INFO_MYBATIS_IMPL = PersonInfoMyBatisImpl.class;
    public static final Class<PhoneNumberMyBatisImpl> PHONE_NUMBER_MYBATIS_IMPL = PhoneNumberMyBatisImpl.class;
    public static final Class<TerminalMyBatisImpl> TERMINAL_MYBATIS_IMPL = TerminalMyBatisImpl.class;
    public static final Class<TimezoneMyBatisImpl> TIMEZONE_MYBATIS_IMPL = TimezoneMyBatisImpl.class;

    // com.solvd.airport.service
    public static final Class<BoardPassengerService> BOARD_PASSENGER_SERVICE = BoardPassengerService.class;
    public static final Class<CheckInService> CHECK_IN_SERVICE = CheckInService.class;
    public static final Class<RegisterPassportHolderService> REGISTER_PASSPORT_HOLDER_SERVICE = RegisterPassportHolderService.class;
    public static final Class<UpdateFlightGateService> UPDATE_FLIGHT_GATE_SERVICE = UpdateFlightGateService.class;

    // com.solvd.airport.service.impl
    public static final Class<BoardPassengerServiceImpl> BOARD_PASSENGER_SERVICE_IMPL = BoardPassengerServiceImpl.class;
    public static final Class<CheckInServiceImpl> CHECK_IN_SERVICE_IMPL = CheckInServiceImpl.class;
    public static final Class<RegisterPassportHolderServiceImpl> REGISTER_PASSPORT_HOLDER_SERVICE_IMPL = RegisterPassportHolderServiceImpl.class;
    public static final Class<UpdateFlightGateServiceImpl> UPDATE_FLIGHT_GATE_SERVICE_IMPL = UpdateFlightGateServiceImpl.class;


    // com.solvd.airport.util
    public static final Class<AnsiCodes> ANSI_CODES = AnsiCodes.class;
    public static final Class<ArrayUtils> ARRAY_UTILS = ArrayUtils.class;
    public static final Class<AuthConnection> AUTH_CONNECTION = AuthConnection.class;
    public static final Class<BigDecimalUtils> BIG_DECIMAL_UTILS = BigDecimalUtils.class;
    public static final Class<BooleanUtils> BOOLEAN_UTILS = BooleanUtils.class;
    public static final Class<CollectionUtils> COLLECTION_UTILS = CollectionUtils.class;
    public static final Class<ClassConstants> CLASS_CONSTANTS = ClassConstants.class;
    public static final Class<ConfigConstants> CONFIG_CONSTANTS = ConfigConstants.class;
    public static final Class<ConfigLoader> CONFIG_LOADER = ConfigLoader.class;
    public static final Class<ExceptionUtils> EXCEPTION_UTILS = ExceptionUtils.class;
    public static final Class<DataAccessProvider> DATA_ACCESS_PROVIDER = DataAccessProvider.class;
    public static final Class<DBConnectionPool> DB_CONNECTION_POOL = DBConnectionPool.class;
    public static final Class<FilepathConstants> FILEPATH_CONSTANTS = FilepathConstants.class;
    public static final Class<JacksonUtils> JACKSON_UTILS = JacksonUtils.class;
    public static final Class<JaxbUtils> JAXB_UTILS = JaxbUtils.class;
    public static final Class<MenuUtils> MENU_UTILS = MenuUtils.class;
    public static final Class<MyBatisUtils> MYBATIS_UTILS = MyBatisUtils.class;
    public static final Class<NumberUtils> NUMBER_UTILS = NumberUtils.class;
    public static final Class<ReflectionUtils> REFLECTION_UTILS = ReflectionUtils.class;
    public static final Class<RegExpConstants> REG_EXP_CONSTANTS = RegExpConstants.class;
    public static final Class<ScannerUtils> SCANNER_UTILS = ScannerUtils.class;
    public static final Class<SQLConstants> SQL_CONSTANTS = SQLConstants.class;
    public static final Class<SQLUtils> SQL_UTILS = SQLUtils.class;
    public static final Class<StaxUtils> STAX_UTILS = StaxUtils.class;
    public static final Class<StringConstants> STRING_CONSTANTS = StringConstants.class;
    public static final Class<StringFormatters> STRING_FORMATTERS = StringFormatters.class;


    private ClassConstants() {
        ExceptionUtils.preventConstantsInstantiation();
    }
}
