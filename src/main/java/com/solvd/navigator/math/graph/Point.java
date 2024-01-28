package com.solvd.navigator.math.graph;

import com.solvd.navigator.exception.InvalidCoordinatesException;
import com.solvd.navigator.util.ClassConstants;
import com.solvd.navigator.util.ExceptionUtils;
import com.solvd.navigator.util.StringFormatters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Point extends Coordinate {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.POINT);

    private String pointLabel;

    public Point() {
        super();
    }

    public Point(double x, double y) {
        super(x, y);
    }

    public Point(double x, double y, String pointLabel) {
        super(x, y);

        ExceptionUtils.isValidString(pointLabel, "String for 'pointLabel' cannot be empty or null to set");

        this.pointLabel = pointLabel;
    }

    public Point(int x, int y) {
        super(x, y);
    }

    public Point(int x, int y, String pointLabel) {
        super(x, y);

        ExceptionUtils.isValidString(pointLabel, "String for 'pointLabel' cannot be empty or null to set");

        this.pointLabel = pointLabel;
    }

    public Point(String xString, String yString) {
        super(xString, yString);
    }

    public Point(String xString, String yString, String pointLabel) {
        super(xString, yString);

        ExceptionUtils.isValidString(pointLabel, "String for 'pointLabel' cannot be empty or null to set");


        this.pointLabel = pointLabel;
    }

    @Override
    public void setX(double x) {
        ExceptionUtils.isValidAbscissa(x);
        super.setX(x);
    }


    @Override
    public void setY(double y) {
        ExceptionUtils.isValidOrdinate(y);

        super.setY(y);
    }

    @Override
    public void setCoordinates(double x, double y) {
        // integers for 'x' and 'y' kept unsigned for the purposes of this project
        ExceptionUtils.areValidCoordinates(x, y);

        super.setCoordinates(x, y);
    }

    @Override
    public void setCoordinates(int x, int y) {
        ExceptionUtils.areValidCoordinates(x, y);

        super.setCoordinates(x, y);
    }

    @Override
    public void setCoordinates(String xString, String yString) {
        try {
            double x = Double.parseDouble(xString);
            double y = Double.parseDouble(yString);

            ExceptionUtils.areValidCoordinates(x, y);

            super.setCoordinates(x, y);

        } catch (NumberFormatException e) {
            final String INVALID_NUMBER_FORMAT_FOR_COORDINATES_EXCEPTION_MSG =
                    "Invalid format for coordinates.";
            LOGGER.error(INVALID_NUMBER_FORMAT_FOR_COORDINATES_EXCEPTION_MSG);
            throw new InvalidCoordinatesException(INVALID_NUMBER_FORMAT_FOR_COORDINATES_EXCEPTION_MSG);
        }
    }

    public String getPointLabel() {
        return pointLabel;
    }

    public void setPointLabel(String pointLabel) {
        this.pointLabel = pointLabel;
    }

    @Override
    public String toString() {
        Class<?> currClass = ClassConstants.POINT;
        String[] fieldNames = {
                "pointLabel"
        };

        String fieldsString = StringFormatters.buildFieldsString(this, fieldNames);
        return StringFormatters.buildToString(currClass, fieldNames, fieldsString);
    }


}
