package com.solvd.navigator.math;

import com.solvd.navigator.exception.InvalidAbscissaException;
import com.solvd.navigator.exception.InvalidCoordinatesException;
import com.solvd.navigator.exception.InvalidOrdinateException;
import com.solvd.navigator.util.BooleanUtils;
import com.solvd.navigator.util.StringFormatters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Point extends Coordinate {
    private static final Logger LOGGER = LogManager.getLogger(Point.class);

    private String pointLabel;

    public Point() {
        super();
    }

    public Point(double x, double y) {
        super(x, y);
    }

    public Point(double x, double y, String pointLabel) {
        super(x, y);

        if (BooleanUtils.isBlankString(pointLabel)) {
            LOGGER.error("String for 'pointLabel' cannot be empty or null to set");
            throw new IllegalArgumentException("String for 'pointLabel' cannot be empty or null to set");
        }

        this.pointLabel = pointLabel;
    }

    public Point(int x, int y) {
        super(x, y);
    }

    public Point(int x, int y, String pointLabel) {
        super(x, y);

        if (BooleanUtils.isBlankString(pointLabel)) {
            LOGGER.error("String for 'pointLabel' cannot be empty or null to set");
            throw new IllegalArgumentException("String for 'pointLabel' cannot be empty or null to set");
        }

        this.pointLabel = pointLabel;
    }

    public Point(String xString, String yString) {
        super(xString, yString);
    }

    public Point(String xString, String yString, String pointLabel) {
        super(xString, yString);

        if (BooleanUtils.isBlankString(pointLabel)) {
            LOGGER.error("String for 'pointLabel' cannot be empty or null to set");
            throw new IllegalArgumentException("String for 'pointLabel' cannot be empty or null to set");
        }

        this.pointLabel = pointLabel;
    }

    @Override
    public void setX(double x) {
        if (x < 0) {
            throw new InvalidAbscissaException("Abscissa, 'x', out of valid range for Point.");
        }
        super.setX(x);
    }

    @Override
    public void setY(double y) {
        if (y < 0) {
            throw new InvalidOrdinateException("Ordinate, 'y', out of valid range for this Point.");
        }

        super.setY(y);
    }

    @Override
    public void setCoordinates(double x, double y) {
        // integers for 'x' and 'y' kept unsigned for the purposes of this project
        if (x < 0 || y < 0) {
            throw new InvalidCoordinatesException("Coordinates out of valid range.");
        }

        super.setCoordinates(x, y);
    }

    @Override
    public void setCoordinates(int x, int y) {
        if (x < 0 || y < 0) {
            throw new InvalidCoordinatesException("Coordinates out of valid range.");
        }

        super.setCoordinates(x, y);
    }

    @Override
    public void setCoordinates(String xString, String yString) {
        try {
            double x = Double.parseDouble(xString);
            double y = Double.parseDouble(yString);

            if (x < 0 || y < 0) {
                throw new InvalidCoordinatesException("Coordinates out of valid range.");
            }

            super.setCoordinates(x, y);

        } catch (NumberFormatException e) {
            throw new InvalidCoordinatesException("Invalid format for coordinates.");
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
        Class<?> currClass = Point.class;
        String[] fieldNames = {
                "pointLabel"
        };

        String fieldsString = StringFormatters.buildFieldsString(this, fieldNames);
        return StringFormatters.buildToString(currClass, fieldNames, fieldsString);
    }


}
