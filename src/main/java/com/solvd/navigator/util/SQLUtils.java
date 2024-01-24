package com.solvd.navigator.util;

import com.solvd.navigator.exception.InvalidDateFormatException;
import com.solvd.navigator.exception.UnsuccessfulAutoGenerationOfIdException;
import com.solvd.navigator.exception.UnsuccessfulStatementSetException;
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
    public static void setFloatOrNull(PreparedStatement ps, int parameterIndex, Float value) {
        try {


            if (value != null && value >= 0) {
                ps.setFloat(parameterIndex, value);
            } else {
                ps.setNull(parameterIndex, Types.FLOAT);
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException occurred while setting Float value: ", e);
            throw new UnsuccessfulStatementSetException("SQLException occurred while setting Float value: " + e);
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
