package com.solvd.navigator.math;

import com.solvd.navigator.exception.InvalidVertexException;
import com.solvd.navigator.util.StringFormatters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Vertex {
    private static final Logger LOGGER = LogManager.getLogger(Vertex.class);

    private String vertexId;
    private String name;
    private Point coordinates;

    public Vertex() {
    }

    public Vertex(String id, Point coordinates) {
        if (id == null || id.isEmpty()) {
            throw new InvalidVertexException("Vertex identifier must not be null or empty.");
        }
        this.vertexId = id;
        this.coordinates = coordinates;
    }

    public Vertex(String id, String name, Point coordinates) {
        if (id == null || id.isEmpty()) {
            throw new InvalidVertexException("Vertex identifier must not be null or empty.");
        }
        this.vertexId = id;
        this.name = name;
        this.coordinates = coordinates;
    }

    public Vertex(String id) {
        if (id == null || id.isEmpty()) {
            throw new InvalidVertexException("Vertex identifier must not be null or empty.");
        }
        this.vertexId = id;
    }

    public String getVertexId() {
        return vertexId;
    }

    public void setVertexId(String vertexId) {
        this.vertexId = vertexId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
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
                "name",
                "coordinates"
        };

        String fieldsString = StringFormatters.buildFieldsString(this, fieldNames);
        return StringFormatters.buildToString(currClass, fieldNames, fieldsString);
    }
}
