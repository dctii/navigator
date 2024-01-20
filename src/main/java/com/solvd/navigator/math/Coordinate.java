package com.solvd.navigator.math;

import com.solvd.navigator.exception.InvalidCoordinatesException;
import com.solvd.navigator.util.BooleanUtils;
import com.solvd.navigator.util.NumberUtils;
import com.solvd.navigator.util.StringConstants;
import com.solvd.navigator.util.StringFormatters;
import org.apache.commons.lang3.StringUtils;

public abstract class Coordinate {
    private double x;
    private double y;

    public Coordinate() {
        this.x = Double.NaN;
        this.y = Double.NaN;
    }

    public Coordinate(double x, double y) {
        setCoordinates(x, y);
    }

    public Coordinate(int x, int y) {
        setCoordinates(x, y);
    }

    public Coordinate(String xString, String yString) {
        setCoordinates(xString, yString);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = NumberUtils.roundToScale(x, 0);
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = NumberUtils.roundToScale(y, 0);
    }

    public void setCoordinates(double x, double y) {
        this.x = NumberUtils.roundToScale(x, 0);
        this.y = NumberUtils.roundToScale(y, 0);
    }

    public void setCoordinates(int x, int y) {
        this.x = NumberUtils.roundToScale(x, 0);
        this.y = NumberUtils.roundToScale(y, 0);
    }

    public void setCoordinates(String xString, String yString) {
        if (BooleanUtils.areAnyStringsBlank(xString, yString)) {
            throw new InvalidCoordinatesException("Coordinate strings must not be blank.");
        }

        try {
            double x = Double.parseDouble(xString);
            double y = Double.parseDouble(yString);

            setCoordinates(x, y);
        } catch (NumberFormatException e) {
            throw new InvalidCoordinatesException("Invalid format for coordinates.");
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return Double.compare(that.x, x) == 0 &&
                Double.compare(that.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(x, y);
    }

    @Override
    public String toString() {
        Class<?> currClass = Coordinate.class;
        String[] fieldNames = {
                "x",
                "y"
        };

        String fieldsString = StringFormatters.buildFieldsString(this, fieldNames);
        return StringFormatters.buildToString(currClass, fieldNames, fieldsString);
    }
}
