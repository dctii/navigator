package com.solvd.navigator.math.graph;

import com.solvd.navigator.util.ClassConstants;
import com.solvd.navigator.util.NumberUtils;
import com.solvd.navigator.util.StringFormatters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Direction extends Coordinate {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.DIRECTION);

    private Direction(Builder builder) {
        super(builder.dx, builder.dy);
    }

    public static class Builder {
        private double dx = Double.NaN;
        private double dy = Double.NaN;

        public Builder() {
        }

        public Builder dx(double dx) {
            this.dx = dx;
            return this;
        }

        public Builder dy(double dy) {
            this.dy = dy;
            return this;
        }

        public Builder dx(int dx) {
            this.dx = dx;
            return this;
        }

        public Builder dy(int dy) {
            this.dy = dy;
            return this;
        }

        public Builder dx(String dxString) {
            this.dx = NumberUtils.parseDoubleOrNaN(dxString);
            return this;
        }

        public Builder dy(String dyString) {
            this.dy = NumberUtils.parseDoubleOrNaN(dyString);
            return this;
        }


        public Direction build() {
            return new Direction(this);
        }
    }

    @Override
    public String toString() {
        Class<?> currClass = ClassConstants.DIRECTION;
        String[] fieldNames = {
                "x",
                "y"
        };
        String fieldsString = StringFormatters.buildFieldsString(this, fieldNames);
        return StringFormatters.buildToString(currClass, fieldNames, fieldsString);
    }
}
