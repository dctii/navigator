package com.solvd.navigator.math;

import com.solvd.navigator.exception.InvalidCoordinatesException;
import com.solvd.navigator.exception.InvalidVertexException;
import com.solvd.navigator.exception.PointTypeIsNullException;
import com.solvd.navigator.util.BooleanUtils;
import com.solvd.navigator.util.StringFormatters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Vertex {
    private static final Logger LOGGER = LogManager.getLogger(Vertex.class);

    private String vertexId;
    private String vertexName;
    private Point point;

    public Vertex() {
    }

    public Vertex(String vertexId, Point point) {
        if (BooleanUtils.isBlankString(vertexId)) {
            throw new InvalidVertexException("Vertex identifier must not be null or empty.");
        }
        this.vertexId = vertexId;
        this.point = point;
    }

    public Vertex(String vertexId, double x, double y) {
        if (BooleanUtils.isBlankString(vertexId)) {
            throw new InvalidVertexException("Vertex identifier must not be null or empty.");
        }
        this.vertexId = vertexId;
        this.point = new Point(x, y);
    }

    public Vertex(String vertexId, String name, Point point) {
        if (BooleanUtils.isBlankString(vertexId)) {
            throw new InvalidVertexException("Vertex identifier must not be null or empty.");
        }
        this.vertexId = vertexId;
        this.vertexName = name;
        this.point = point;
    }

    public Vertex(String vertexId, String name, double x, double y) {
        if (BooleanUtils.isBlankString(vertexId)) {
            throw new InvalidVertexException("Vertex identifier must not be null or empty.");
        }
        this.vertexId = vertexId;
        this.vertexName = name;
        this.point = new Point(x, y);
    }

    public Vertex(String vertexId) {
        if (BooleanUtils.isBlankString(vertexId)) {
            throw new InvalidVertexException("Vertex identifier must not be null or empty.");
        }
        this.vertexId = vertexId;
    }

    public String getVertexId() {
        return vertexId;
    }

    public void setVertexId(String vertexId) {
        if (BooleanUtils.isBlankString(vertexId)) {
            throw new InvalidVertexException("Vertex identifier must not be null or empty.");
        }
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

    public String getCoordinatesString() {
        if (point == null) {
            throw new InvalidCoordinatesException("To get coordinates string, the 'Point' object in this Vertex cannot be null.");
        }

        if (BooleanUtils.areDoublesNan(point.getX(), point.getY())) {
            throw new InvalidCoordinatesException("To get coordinates string, the 'Point' object in this Vertex must have valid coordinates.");
        }

        return point.getCoordinatesString();

    }

    public void setPoint(Point point) {

        this.point = point;
    }

    public void setCoordinates(double x, double y) {
        if (point != null) {
            point.setCoordinates(x, y);
        } else {
            final String POINT_TYPE_IS_NULL_EXCEPTION_MSG =
                    "'Point point' field of this Vertex cannot set point 'x' and 'y' if null";
            LOGGER.error(POINT_TYPE_IS_NULL_EXCEPTION_MSG);
            throw new PointTypeIsNullException(POINT_TYPE_IS_NULL_EXCEPTION_MSG);
        }
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
        Class<?> currClass = Vertex.class;
        String[] fieldNames = {
                "vertexId",
                "vertexName",
                "point"
        };

        String fieldsString = StringFormatters.buildFieldsString(this, fieldNames);
        return StringFormatters.buildToString(currClass, fieldNames, fieldsString);
    }
}
