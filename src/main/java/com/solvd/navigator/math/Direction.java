package com.solvd.navigator.math;

import com.solvd.navigator.util.NumberUtils;
import com.solvd.navigator.util.StringFormatters;

public class Direction extends Coordinate {

    private Direction(Builder builder) {
        super(builder.x, builder.y);
    }

    public static class Builder {
        private double x = Double.NaN;
        private double y = Double.NaN;

        public Builder() {
        }

        public Builder x(double x) {
            this.x = x;
            return this;
        }

        public Builder y(double y) {
            this.y = y;
            return this;
        }

        public Builder x(int x) {
            this.x = x;
            return this;
        }

        public Builder y(int y) {
            this.y = y;
            return this;
        }

        public Builder x(String xString) {
            this.x = NumberUtils.parseDoubleOrNaN(xString);
            return this;
        }

        public Builder y(String yString) {
            this.y = NumberUtils.parseDoubleOrNaN(yString);
            return this;
        }


        public Direction build() {
            return new Direction(this);
        }
    }

    // Override toString
    @Override
    public String toString() {
        Class<?> currClass = Direction.class;
        String[] fieldNames = {
                "x",
                "y"
        };
        String fieldsString = StringFormatters.buildFieldsString(this, fieldNames);
        return StringFormatters.buildToString(currClass, fieldNames, fieldsString);
    }
}
