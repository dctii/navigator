package com.solvd.airport.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.util.function.IntFunction;

public class SQLConstants {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.SQL_CONSTANTS);

    public static final String PLACEHOLDER = StringConstants.QUESTION_MARK;
    public static final String WILDCARD = StringConstants.ASTERISK_SIGN;

    public static final IntFunction<?> VALUE_PLACEHOLDERS = i -> DSL.val(PLACEHOLDER);
    public static final SQLDialect MYSQL_DIALECT = SQLDialect.MYSQL;
    
    public static final SQLDialect SQL_DIALECT = MYSQL_DIALECT;
}
