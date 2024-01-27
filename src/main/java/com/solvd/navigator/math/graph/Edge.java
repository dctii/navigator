package com.solvd.navigator.math.graph;

import com.solvd.navigator.exception.InvalidEdgeException;
import com.solvd.navigator.exception.InvalidEdgeIdNameException;
import com.solvd.navigator.util.BooleanUtils;
import com.solvd.navigator.util.ClassConstants;
import com.solvd.navigator.util.ExceptionUtils;
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
        ExceptionUtils.areValidVertices(vertex1, vertex2);

        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.edgeId =
                vertex1.getVertexId() + StringConstants.DASH_STRING + vertex2.getVertexId();
    }

    public Edge(Vertex vertex1, Vertex vertex2, double distance) {
        ExceptionUtils.areValidVertices(vertex1, vertex2);

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
        ExceptionUtils.areValidVertices(
                vertex1,
                vertex2,
                "Both vertices must be non-null and must have a vertexId to create an edgeId."
        );

        String vertex1Id = vertex1.getVertexId();
        String vertex2Id = vertex2.getVertexId();
        String correctEdgeId = vertex1Id + StringConstants.DASH_STRING + vertex2Id;

        if (BooleanUtils.areEqual(edgeId, correctEdgeId)) {
            this.edgeId = edgeId;
        } else {
            final String INVALID_EDGE_ID_NAME_EXCEPTION_MSG =
                    "The edge ID must match the vertices their relationship (e.g., V1 to V2 must have an edgeId of 'V1-V2'";
            LOGGER.warn(INVALID_EDGE_ID_NAME_EXCEPTION_MSG);
            throw new InvalidEdgeIdNameException(INVALID_EDGE_ID_NAME_EXCEPTION_MSG);
        }
    }

    public Vertex getVertex1() {
        return vertex1;
    }

    public void setVertex1(Vertex vertex1) {
        ExceptionUtils.isValidVertex(vertex1, "Vertex1 must be non-null.");
        this.vertex1 = vertex1;
    }

    public Vertex getVertex2() {
        return vertex2;
    }

    public void setVertex2(Vertex vertex2) {
        ExceptionUtils.isValidVertex(vertex2, "Vertex2 must be non-null.");
        this.vertex2 = vertex2;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        if (distance < 0) {
            final String INVALID_EDGE_EXCEPTION_MSG =
                    "Weight must be non-negative.";
            LOGGER.error(INVALID_EDGE_EXCEPTION_MSG);
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
                ExceptionUtils.isValidVertexOption();
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
                ExceptionUtils.isValidVertexOption();
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
