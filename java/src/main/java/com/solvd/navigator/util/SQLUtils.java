package com.solvd.airport.util;

import com.solvd.airport.exception.InvalidDateFormatException;
import com.solvd.airport.exception.UnsuccessfulAutoGenerationOfIdException;
import com.solvd.airport.exception.UnsuccessfulStatementSetException;
import com.solvd.airport.persistence.AirportDAO;
import com.solvd.airport.persistence.CountryDAO;
import com.solvd.airport.persistence.FlightDAO;
import com.solvd.airport.persistence.GateDAO;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.Condition;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class SQLUtils {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.SQL_UTILS);

    public static void updateAndSetGeneratedId(
            PreparedStatement ps,
            IntConsumer setIdConsumer
    ) {
        try {
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                LOGGER.error("Creating object failed, no rows affected");
                return;
            }
            SQLUtils.setGeneratedKey(ps, setIdConsumer);
        } catch (SQLException e) {
            LOGGER.error("SQLException occurred: ", e);
        }
    }

    public static void setGeneratedKey(PreparedStatement preparedStatement, IntConsumer setIdConsumer) {
        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                setIdConsumer.accept(id);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to obtain ID.", e);
            throw new UnsuccessfulAutoGenerationOfIdException("Failed to obtain ID." + e);
        }
    }


    public static void setBooleanOrFalse(PreparedStatement ps, int parameterIndex, Boolean value) {
        try {
            if (value != null) {
                ps.setBoolean(parameterIndex, value);
            } else {
                ps.setBoolean(parameterIndex, false);
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException occurred while setting boolean value: ", e);
            throw new UnsuccessfulStatementSetException("SQLException occurred while setting boolean value: " + e);
        }
    }


    public static void setTimestampOrNull(PreparedStatement ps, int parameterIndex, Timestamp value) {
        try {


            if (value != null) {
                ps.setTimestamp(parameterIndex, value);
            } else {
                ps.setNull(parameterIndex, Types.TIMESTAMP);
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException occurred while setting Timestamp value: ", e);
            throw new UnsuccessfulStatementSetException("SQLException occurred while setting Timestamp value: " + e);
        }

    }

    public static void setStringOrNull(PreparedStatement ps, int parameterIndex, String value) {
        try {
            if (value != null && !value.isEmpty()) {
                ps.setString(parameterIndex, value);
            } else {
                ps.setNull(parameterIndex, Types.VARCHAR);
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException occurred while setting String value: ", e);
            throw new UnsuccessfulStatementSetException("SQLException occurred while setting String value: " + e);
        }
    }

    public static void setBigDecimalOrNull(PreparedStatement ps, int parameterIndex, BigDecimal value) {
        try {
            
            if (value != null) {
                ps.setBigDecimal(parameterIndex, value);
            } else {
                ps.setNull(parameterIndex, Types.DECIMAL);
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException occurred while setting BigDecimal value: ", e);
            throw new UnsuccessfulStatementSetException("SQLException occurred while setting BigDecimal value: " + e);
        }
    }

    public static void setIntOrNull(PreparedStatement ps, int parameterIndex, Integer value) {
        try {


            if (value != null && value >= 0) {
                ps.setInt(parameterIndex, value);
            } else {
                ps.setNull(parameterIndex, Types.INTEGER);
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException occurred while setting Integer value: ", e);
            throw new UnsuccessfulStatementSetException("SQLException occurred while setting Integer value: " + e);
        }
    }

    public static void displayBoardingPassInfo(String bookingNumber, boolean hasBaggage) {
        DBConnectionPool connectionPool = DBConnectionPool.getInstance();

        final String GET_BOARDING_PASS_INFO_SQL =
                "SELECT bp.boarding_group, bp.boarding_time, f.flight_code, g.gate_code" +
                        (hasBaggage ? ", ba.baggage_code " : " ") +
                        "FROM boarding_passes AS bp " +
                        "JOIN check_ins AS ci ON bp.check_in_id = ci.check_in_id " +
                        "JOIN bookings AS b ON ci.booking_id = b.booking_id " +
                        "JOIN flights AS f ON b.flight_code = f.flight_code " +
                        "JOIN gates AS g ON f.gate_id = g.gate_id" +
                        (hasBaggage ? " LEFT JOIN baggage AS ba ON ci.check_in_id = ba.check_in_id " : " ") +
                        "WHERE b.booking_number = ?";

        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_BOARDING_PASS_INFO_SQL)) {
            ps.setString(1, bookingNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String boardingGroup = rs.getString("boarding_group");
                    Timestamp boardingTime = rs.getTimestamp("boarding_time");
                    String flightCode = rs.getString("flight_code");
                    String gateCode = rs.getString("gate_code");
                    String baggageInfo = hasBaggage ? rs.getString("baggage_code") : "No Baggage";

                    LOGGER.info("\n\n=== Boarding Information ===\n" +
                                    "Boarding Group: {}\n" +
                                    "Boarding Time: {}\n" +
                                    "Flight Code: {}\n" +
                                    "Gate: {}\n" +
                                    (hasBaggage ? "Baggage Code: {}" : "{}"),
                            boardingGroup, boardingTime, flightCode, gateCode,
                            baggageInfo + "\n"
                    );
                } else {
                    LOGGER.info("No boarding information found for booking number: {}", bookingNumber);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching boarding pass information", e);
        }
    }


    public static String determineBoardingGroup(String seatClass) {
        seatClass = seatClass.toLowerCase();
        switch (seatClass) {
            case "first class":
                return "Group A";
            case "business":
                return "Group B";
            case "economy":
                return "Group C";
            default:
                return "Unknown Group";
        }
    }

    public static java.sql.Timestamp toTimestamp(String datetimeString) {
        java.sql.Timestamp newTimestamp;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(StringConstants.TIMESTAMP_PATTERN);
            java.util.Date parsedDate = formatter.parse(datetimeString);
            newTimestamp = new java.sql.Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            throw new InvalidDateFormatException("Invalid timestamp format: " + e);
        }
        return newTimestamp;
    }

    public static java.sql.Date toDate(String dateString) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(StringConstants.YEAR_FIRST_DATE_PATTERN);
            java.util.Date parsedDate = formatter.parse(dateString);
            return new java.sql.Date(parsedDate.getTime());
        } catch (ParseException e) {
            throw new InvalidDateFormatException("Invalid date format: " + e.getMessage());
        }
    }

    public static boolean doesCountryCodeExist(String countryCode) {
        final CountryDAO countryDAO = DataAccessProvider.getCountryDAO();

        boolean exists = countryDAO.doesCountryCodeExist(countryCode);

        if (!exists) {
            LOGGER.error("Country code doesn't exist in resource, please try another country.");
        }

        return exists;
    }

    public static boolean doesFlightExist(String flightCode) {
        final FlightDAO flightDAO = DataAccessProvider.getFlightDAO();

        boolean exists = flightDAO.doesFlightExist(flightCode);

        if (!exists) {
            LOGGER.error("Airport doesn't exist in resource, please try another.");
        }

        return exists;
    }

    public static boolean doesAirportExist(String airportCode) {
        final AirportDAO airportDAO = DataAccessProvider.getAirportDAO();

        boolean exists = airportDAO.doesAirportExist(airportCode);

        if (!exists) {
            LOGGER.error("Airport doesn't exist in resource, please try another.");
        }

        return exists;
    }

    public static boolean doesGateExist(String gateCode, String airportCode) {
        final GateDAO gateDAO = DataAccessProvider.getGateDAO();

        boolean exists = gateDAO.doesGateExist(gateCode, airportCode);

        if (!exists) {
            LOGGER.error("Gate doesn't exist in resource, please try another.");
        }

        return exists;
    }

    public static String qualifyColumnName(String tableName, String columnName) {
        return StringUtils.joinWith(StringConstants.FULL_STOP, tableName.strip(), columnName.strip());
    }

    public static String qualifyTableWithWildcard(String tableName) {
        return qualifyColumnName(tableName, SQLConstants.WILDCARD);
    }

    public static List<?> createPlaceholders(int numberOfPlaceholders) {
        return IntStream.range(0, numberOfPlaceholders)
                .mapToObj(SQLConstants.VALUE_PLACEHOLDERS)
                .collect(Collectors.toList());
    }

    public static Condition eqFields(String columnName1, String columnName2) {
        return DSL.field(columnName1).eq(DSL.field(columnName2));
    }

    public static Condition eqPlaceholder(String columnName) {
        return DSL.field(columnName).eq(SQLConstants.PLACEHOLDER);
    }


    private SQLUtils() {
        ExceptionUtils.preventUtilityInstantiation();
    }
}
