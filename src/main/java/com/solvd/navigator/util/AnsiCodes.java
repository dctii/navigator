package com.solvd.navigator.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.pattern.AnsiEscape;

public final class AnsiCodes {

    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.ANSI_CODES);
    public static final String RED = toAnsiString(AnsiEscape.RED);
    public static final String YELLOW = toAnsiString(AnsiEscape.YELLOW);
    public static final String GREEN = toAnsiString(AnsiEscape.GREEN);
    public static final String BLUE = toAnsiString(AnsiEscape.BLUE);
    public static final String BOLD = toAnsiString(AnsiEscape.BOLD);
    public static final String RESET_ALL = toAnsiString(AnsiEscape.NORMAL);

    private static String toAnsiString(AnsiEscape STYLE) {
        return wrapAnsiStyleCode(STYLE.getCode());
    }

    private static String wrapAnsiStyleCode(String STYLE_CODE) {
        return StringUtils.join(
                StringConstants.ESCAPE_SEQUENCE,
                StringConstants.OPENING_BRACKET,
                STYLE_CODE,
                StringConstants.LOWER_CASE_M_CHAR
        );
    }

    private AnsiCodes() {
        ExceptionUtils.preventUtilityInstantiation();
    }
}
