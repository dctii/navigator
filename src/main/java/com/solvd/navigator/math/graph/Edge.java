package com.solvd.navigator.math.graph;

import com.solvd.navigator.exception.InvalidEdgeException;
import com.solvd.navigator.exception.InvalidEdgeIdNameException;
import com.solvd.navigator.exception.InvalidVertexException;
import com.solvd.navigator.exception.InvalidVertexTargetException;
import com.solvd.navigator.util.BooleanUtils;
import com.solvd.navigator.util.ClassConstants;
import com.solvd.navigator.util.StringConstants;
import com.solvd.navigator.util.StringFormatters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Edge {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.EDGE);

    String edgeId;
    private Vertex vertex1;
    private Vertex vertex2;
    private double distance;

    public Edge() {
    }

    public Edge(Vertex vertex1, Vertex vertex2) {
        if (BooleanUtils.areInvalidVertices(vertex1, vertex2)) {
            throw new InvalidEdgeException("Both vertices must be non-null and have vertexIds to create an edge.");
        }

        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.edgeId =
                vertex1.getVertexId() + StringConstants.DASH_STRING + vertex2.getVertexId();
    }

    public Edge(Vertex vertex1, Vertex vertex2, double distance) {
        if (BooleanUtils.areInvalidVertices(vertex1, vertex2)) {
            throw new InvalidEdgeException("Both vertices must be non-null and have vertexIds to create an edge.");
        }

        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.distance = distance;
        this.edgeId =
                vertex1.getVertexId() + StringConstants.DASH_STRING + vertex2.getVertexId();
    }

    public String getEdgeId() {
        return edgeId;
    }

    public void setEdgeId(String edgeId) {
        if (BooleanUtils.areInvalidVertices(vertex1, vertex2)) {
            LOGGER.warn("Both vertices must be non-null and must have a vertexId to create an edgeId.");
            throw new InvalidVertexException("Both vertices must be non-null and must have a vertexId to create an edgeId.");
        }
        String vertex1Id = vertex1.getVertexId();
        String vertex2Id = vertex2.getVertexId();
        String correctEdgeId = vertex1Id + StringConstants.DASH_STRING + vertex2Id;

        if (BooleanUtils.areEqual(edgeId, correctEdgeId)) {
            this.edgeId = edgeId;
        } else {
            LOGGER.warn("The edge ID must match the vertices their relationship (e.g., V1 to V2 must have an edgeId of 'V1-V2'");
            throw new InvalidEdgeIdNameException("The edge ID must match the vertices their relationship (e.g., V1 to V2 must have an edgeId of 'V1-V2'");
        }
    }

    public Vertex getVertex1() {
        return vertex1;
    }

    public void setVertex1(Vertex vertex1) {
        if (BooleanUtils.isInvalidVertex(vertex1)) {
            throw new InvalidEdgeException("Vertex1 must be non-null.");
        }
        this.vertex1 = vertex1;
    }

    public Vertex getVertex2() {
        return vertex2;
    }

    public void setVertex2(Vertex vertex2) {
        if (BooleanUtils.isInvalidVertex(vertex2)) {
            throw new InvalidEdgeException("Vertex2 must be non-null.");
        }
        this.vertex2 = vertex2;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        if (distance < 0) {
            throw new InvalidEdgeException("Weight must be non-negative.");
        }
        this.distance = distance;
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
                vertexCoordinates = vertex1.getPoint();
                break;
            case 2:
                vertexCoordinates = vertex2.getPoint();
                break;
            default:
                LOGGER.error("Invalid vertex number. Only '1' and '2' available.");
                throw new InvalidVertexTargetException("Invalid vertex number. Only '1' and '2' available.");
        }

        return vertexCoordinates;
    }


    @Override
    public String toString() {
        Class<?> currClass = ClassConstants.EDGE;
        String[] fieldNames = {
                "edgeId",
                "vertex1",
                "vertex2",
                "distance"
        };

        String fieldsString = StringFormatters.buildFieldsString(this, fieldNames);
        return StringFormatters.buildToString(currClass, fieldNames, fieldsString);
    }
}
