package com.solvd.navigator.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.function.BinaryOperator;

public class BigDecimalUtils {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.BIG_DECIMAL_UTILS);
    private static final MathContext MATH_CONTEXT = new MathContext(10);

    public static final BinaryOperator<BigDecimal> ADD_OPERATION = BigDecimalUtils::add;
    public static final BinaryOperator<BigDecimal> SUBTRACT_OPERATION = BigDecimalUtils::subtract;
    public static final BinaryOperator<BigDecimal> MULTIPLY_OPERATION = BigDecimalUtils::multiply;
    public static final BinaryOperator<BigDecimal> DIVIDE_OPERATION = BigDecimalUtils::divide;


    public static Number sqrt(Number value) {
    /*
        "BigDecimal sqrt() Method in Java with Examples"
        https://www.geeksforgeeks.org/bigdecimal-sqrt-method-in-java-with-examples/
    */
        BigDecimal sqrtValue =
                new BigDecimal(value.toString())
                        .sqrt(MATH_CONTEXT);

        if (value instanceof BigDecimal) {
            return sqrtValue;
        } else if (value instanceof Double) {
            return sqrtValue.floatValue();
        } else if (value instanceof Integer) {
            return sqrtValue.intValue();
        } else if (value instanceof Long) {
            return sqrtValue.longValue();
        } else {
            final String ILLEGAL_ARGUMENT_MESSAGE = "Number type not supported";
            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE);
        }
    }

    public static int roundToInt(Number number) {
        return NumberUtils.roundToInt(number);
    }

    public static BigDecimal round(int scale, Double value) {
        return BigDecimal.valueOf(value).setScale(scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal round(int scale, Float value) {
        return BigDecimal.valueOf(value).setScale(scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal round(int scale, Integer value) {
        return BigDecimal.valueOf(value).setScale(scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal round(int scale, Long value) {
        return BigDecimal.valueOf(value).setScale(scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal divide(Number... values) {
        return arithmetize(values, StringConstants.DIVIDE_STRING);
    }

    public static BigDecimal divideAll(Number... values) {
        return arithmetizeAll(values, StringConstants.DIVIDE_STRING);
    }


    public static BigDecimal multiply(Number... values) {
        return arithmetize(values, StringConstants.MULTIPLY_STRING);
    }

    public static BigDecimal multiplyAll(Number... values) {
        return arithmetizeAll(values, StringConstants.MULTIPLY_STRING);
    }


    public static BigDecimal add(Number... values) {
        return arithmetize(values, StringConstants.ADD_STRING);
    }

    public static BigDecimal addAll(Number... values) {
        return arithmetizeAll(values, StringConstants.ADD_STRING);
    }


    public static BigDecimal subtract(Number... values) {
        return arithmetize(values, StringConstants.SUBTRACT_STRING);
    }

    public static BigDecimal subtractAll(Number... values) {
        return arithmetizeAll(values, StringConstants.SUBTRACT_STRING);
    }

    public static BigDecimal arithmetize(Number[] values, String operationType) {
        if (values.length != 2) {
            final String ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE =
                    "Exactly two values required.";
            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE);
        }

        BigDecimal leftOperand = ensureBigDecimal(values[0]);
        BigDecimal rightOperand = ensureBigDecimal(values[1]);

        switch (operationType.toLowerCase()) {
            case StringConstants.ADD_STRING:
                return leftOperand.add(rightOperand);
            case StringConstants.SUBTRACT_STRING:
                return leftOperand.subtract(rightOperand);
            case StringConstants.MULTIPLY_STRING:
                return leftOperand.multiply(rightOperand);
            case StringConstants.DIVIDE_STRING:
                return leftOperand.divide(rightOperand, MATH_CONTEXT);
            default:
                final String ILLEGAL_ARGUMENT_MESSAGE = "Invalid operation type.";
                throw new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE);
        }
    }

    public static BigDecimal arithmetizeAll(Number[] values, String operationType) {
        if (values == null || values.length < 2) {
            final String ILLEGAL_ARGUMENT_MESSAGE = "Invalid operation type.";
            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE);
        } else if (values.length == 2) {
            return arithmetize(values, operationType);
        }

        BinaryOperator<BigDecimal> arithmeticOperation;
        BigDecimal initialValue = BigDecimal.valueOf(values[0].doubleValue());

        switch (operationType) {
            case StringConstants.ADD_STRING:
                arithmeticOperation = ADD_OPERATION;
                break;
            case StringConstants.SUBTRACT_STRING:
                arithmeticOperation = SUBTRACT_OPERATION;
                break;
            case StringConstants.MULTIPLY_STRING:
                arithmeticOperation = MULTIPLY_OPERATION;
                break;
            case StringConstants.DIVIDE_STRING:
                arithmeticOperation = DIVIDE_OPERATION;
                break;
            default:
                final String ILLEGAL_ARGUMENT_MESSAGE = "Invalid operation type.";
                throw new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE);
        }

        return Arrays.stream(values)
                .skip(1)
                .map(value -> BigDecimal.valueOf(value.doubleValue()))
                .reduce(initialValue, arithmeticOperation);
    }

    public static BigDecimal ensureBigDecimal(Number value) {
        BigDecimal bigDecimalValue;
        if (value instanceof BigDecimal) {
            bigDecimalValue = (BigDecimal) value;
        } else {
            bigDecimalValue = BigDecimal.valueOf(value.doubleValue());
        }
        return bigDecimalValue;
    }

    private BigDecimalUtils() {
        ExceptionUtils.preventUtilityInstantiation();
    }
}
