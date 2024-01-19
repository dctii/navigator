package com.solvd.navigator.math;

import com.solvd.navigator.exception.InvalidPointException;
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
    private Point coordinates;

    public Vertex() {
    }

    public Vertex(String vertexId, Point coordinates) {
        if (BooleanUtils.isBlankString(vertexId)) {
            throw new InvalidVertexException("Vertex identifier must not be null or empty.");
        }
        this.vertexId = vertexId;
        this.coordinates = coordinates;
    }

    public Vertex(String vertexId, double x, double y) {
        if (BooleanUtils.isBlankString(vertexId)) {
            throw new InvalidVertexException("Vertex identifier must not be null or empty.");
        }
        this.vertexId = vertexId;
        this.coordinates = new Point(x, y);
    }

    public Vertex(String vertexId, String name, Point coordinates) {
        if (BooleanUtils.isBlankString(vertexId)) {
            throw new InvalidVertexException("Vertex identifier must not be null or empty.");
        }
        this.vertexId = vertexId;
        this.vertexName = name;
        this.coordinates = coordinates;
    }

    public Vertex(String vertexId, String name, double x, double y) {
        if (BooleanUtils.isBlankString(vertexId)) {
            throw new InvalidVertexException("Vertex identifier must not be null or empty.");
        }
        this.vertexId = vertexId;
        this.vertexName = name;
        this.coordinates = new Point(x, y);
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

    public Point getCoordinates() {
        return coordinates;
    }

    public String getCoordinatesString() {
        if (coordinates != null) {
            return coordinates.getCoordinatesString();
        } else {
            throw new InvalidPointException("To get coordinates string, this Vertex's 'Point coordinates' cannot be null");
        }

    }

    public void setCoordinates(Point coordinates) {

        this.coordinates = coordinates;
    }

    public void setCoordinates(double x, double y) {
        if (coordinates != null) {
            coordinates.setCoordinates(x, y);
        } else {
            final String POINT_TYPE_IS_NULL_EXCEPTION_MSG =
                    "'Point coordinates' field of this Vertex cannot set coordinates 'x' and 'y' if null";
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
                "coordinates"
        };

        String fieldsString = StringFormatters.buildFieldsString(this, fieldNames);
        return StringFormatters.buildToString(currClass, fieldNames, fieldsString);
    }
}
