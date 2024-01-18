package com.solvd.navigator.math;

import com.solvd.navigator.exception.InvalidEdgeException;
import com.solvd.navigator.exception.InvalidVertexTargetException;
import com.solvd.navigator.util.StringFormatters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Edge {
    private static final Logger LOGGER = LogManager.getLogger(Edge.class);
    private Vertex vertex1;
    private Vertex vertex2;
    private int weight;

    public Edge() {
    }

    public Edge(Vertex vertex1, Vertex vertex2) {
        if (vertex1 == null || vertex2 == null) {
            throw new InvalidEdgeException("Both vertices must be non-null.");
        }
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
    }

    public Edge(Vertex vertex1, Vertex vertex2, int weight) {
        if (vertex1 == null || vertex2 == null) {
            throw new InvalidEdgeException("Both vertices must be non-null.");
        }
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.weight = weight;
    }

    public Vertex getVertex1() {
        return vertex1;
    }

    public void setVertex1(Vertex vertex1) {
        if (vertex1 == null) {
            throw new InvalidEdgeException("Vertex1 must be non-null.");
        }
        this.vertex1 = vertex1;
    }

    public Vertex getVertex2() {
        return vertex2;
    }

    public void setVertex2(Vertex vertex2) {
        if (vertex2 == null) {
            throw new InvalidEdgeException("Vertex2 must be non-null.");
        }
        this.vertex2 = vertex2;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        if (weight < 0) {
            throw new InvalidEdgeException("Weight must be non-negative.");
        }
        this.weight = weight;
    }

    public String getVertexId(int vertexNumber) {
        String vertexId = null;
        switch (vertexNumber) {
            case 1:
                vertexId = vertex1.getVertexId();
                break;
            case 2:
                vertexId = vertex2.getVertexId();
                break;
            default:
                LOGGER.error("Invalid vertex number. Only '1' and '2' available.");
                throw new InvalidVertexTargetException("Invalid vertex number. Only '1' and '2' available.");
        }

        return vertexId;
    }

    public Point getVertexCoordinates(int vertexNumber) {
        Point vertexCoordinates = null;
        switch (vertexNumber) {
            case 1:
                vertexCoordinates = vertex1.getCoordinates();
                break;
            case 2:
                vertexCoordinates = vertex2.getCoordinates();
                break;
            default:
                LOGGER.error("Invalid vertex number. Only '1' and '2' available.");
                throw new InvalidVertexTargetException("Invalid vertex number. Only '1' and '2' available.");
        }

        return vertexCoordinates;
    }


    @Override
    public String toString() {
        Class<?> currClass = Edge.class;
        String[] fieldNames = {
                "vertex1",
                "vertex2",
                "weight"
        };

        String fieldsString = StringFormatters.buildFieldsString(this, fieldNames);
        return StringFormatters.buildToString(currClass, fieldNames, fieldsString);
    }
}
