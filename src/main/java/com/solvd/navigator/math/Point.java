package com.solvd.navigator.math;

import com.solvd.navigator.exception.InvalidAbscissaException;
import com.solvd.navigator.exception.InvalidOrdinateException;
import com.solvd.navigator.exception.InvalidPointException;
import com.solvd.navigator.util.NumberUtils;
import com.solvd.navigator.util.StringConstants;
import com.solvd.navigator.util.StringFormatters;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Point {
    private static final Logger LOGGER = LogManager.getLogger(Point.class);
    private double x;
    private double y;

    public Point() {
    }

    public Point(double x, double y) {
        setCoordinates(x, y);
    }

    // Getters and Setters
    public double getX() {
        return x;
    }

    public void setX(double x) {
        if (x < 0) {
            throw new InvalidAbscissaException("Abscissa, 'x', out of valid range.");
        }

        this.x = NumberUtils.roundToScale(x, 2);
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        if (y < 0) {
            throw new InvalidOrdinateException("Ordinate, 'y', out of valid range.");
        }

        this.y = NumberUtils.roundToScale(y, 2);
    }

    public String getCoordinatesString() {
        return StringFormatters.nestInCurlyBraces(
                StringUtils.joinWith(
                        StringConstants.COMMA_DELIMITER,
                        this.x,
                        this.y
                )
        );
    }

    public void setCoordinates(double x, double y) {
        // integers for 'x' and 'y' kept unsigned for the purposes of this project
        if (x < 0 || y < 0) {
            throw new InvalidPointException("Coordinates out of valid range.");
        }

        this.x = NumberUtils.roundToScale(x, 0);
        this.y = NumberUtils.roundToScale(y, 0);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Double.compare(point.x, x) == 0 &&
                Double.compare(point.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(x, y);
    }

    @Override
    public String toString() {
        Class<?> currClass = Vertex.class;
        String[] fieldNames = {
                "x",
                "y"
        };

        String fieldsString = StringFormatters.buildFieldsString(this, fieldNames);
        return StringFormatters.buildToString(currClass, fieldNames, fieldsString);
    }
}
