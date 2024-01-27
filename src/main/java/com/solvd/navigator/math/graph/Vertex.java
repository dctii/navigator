package com.solvd.navigator.math.graph;

import com.solvd.navigator.exception.InvalidCoordinatesException;
import com.solvd.navigator.util.BooleanUtils;
import com.solvd.navigator.util.ClassConstants;
import com.solvd.navigator.util.ExceptionUtils;
import com.solvd.navigator.util.StringFormatters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Vertex {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.VERTEX);

    private String vertexId;
    private String vertexName;
    private Point point;

    public Vertex() {
    }

    public Vertex(String vertexId, Point point) {
        ExceptionUtils.isValidVertexId(vertexId);
        this.vertexId = vertexId;
        this.point = point;
    }

    public Vertex(String vertexId, double x, double y) {
        ExceptionUtils.isValidVertexId(vertexId);
        this.vertexId = vertexId;
        this.point = new Point(x, y);
    }

    public Vertex(String vertexId, String name, Point point) {
        ExceptionUtils.isValidVertexId(vertexId);
        this.vertexId = vertexId;
        this.vertexName = name;
        this.point = point;
    }

    public Vertex(String vertexId, String name, double x, double y) {
        ExceptionUtils.isValidVertexId(vertexId);
        this.vertexId = vertexId;
        this.vertexName = name;
        this.point = new Point(x, y);
    }

    public Vertex(String vertexId) {
        ExceptionUtils.isValidVertexId(vertexId);
        this.vertexId = vertexId;
    }

    public String getVertexId() {
        return vertexId;
    }

    public void setVertexId(String vertexId) {
        ExceptionUtils.isValidVertexId(vertexId);
        this.vertexId = vertexId;
    }

    public String getVertexName() {
        return vertexName;
    }

    public void setVertexName(String vertexName) {
        this.vertexName = vertexName;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public double getX() {
        ExceptionUtils.isValidPoint(
                this.point,
                "To get 'X' coordinate, Point cannot be null"
        );
        return point.getX();
    }

    public double getY() {
        ExceptionUtils.isValidPoint(
                this.point,
                "To get 'Y' coordinate, Point cannot be null"
        );
        return point.getY();
    }

    public String getCoordinatesString() {
        if (point == null) {
            throw new InvalidCoordinatesException("To get coordinates string, the 'Point' object in this Vertex cannot be null.");
        }

        if (BooleanUtils.areDoublesNan(point.getX(), point.getY())) {
            throw new InvalidCoordinatesException("To get coordinates string, the 'Point' object in this Vertex must have valid coordinates.");
        }

        return point.getCoordinatesString();

    }

    public void setCoordinates(double x, double y) {
        ExceptionUtils.isValidPoint(
                this.point,
                "'Point point' field of this Vertex cannot set point 'x' and 'y' if null"
        );
        point.setCoordinates(x, y);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return vertexId.equals(vertex.vertexId);
    }

    @Override
    public int hashCode() {
        return vertexId.hashCode();
    }


    @Override
    public String toString() {
        Class<?> currClass = ClassConstants.VERTEX;
        String[] fieldNames = {
                "vertexId",
                "vertexName",
                "point"
        };

        String fieldsString = StringFormatters.buildFieldsString(this, fieldNames);
        return StringFormatters.buildToString(currClass, fieldNames, fieldsString);
    }
}
