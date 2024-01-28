package com.solvd.navigator.math.graph;

import com.solvd.navigator.math.util.GraphUtils;
import com.solvd.navigator.math.util.MatrixUtils;
import com.solvd.navigator.util.ClassConstants;
import com.solvd.navigator.util.StringFormatters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ShortestPathsMatrix {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.SHORTEST_PATHS_MATRIX);
    private double[][] shortestDistances;
    private Map<Integer, Integer> locationIdToIndexMap; // mapping from vertex ID to index in the matrix

    private ShortestPathsMatrix(Builder builder) {
        this.shortestDistances = builder.shortestDistances;
        this.locationIdToIndexMap = builder.locationIdToIndexMap;
    }

    public static class Builder {
        private double[][] shortestDistances;
        private Map<Integer, Integer> locationIdToIndexMap;

        public Builder() {
            this.locationIdToIndexMap = new HashMap<>();
        }

        public Builder setShortestDistances(double[][] shortestDistances) {
            this.shortestDistances = shortestDistances;
            return this;
        }

        public Builder setLocationIdToIndexMap(Map<Integer, Integer> locationIdToIndexMap) {
            this.locationIdToIndexMap = locationIdToIndexMap;
            return this;
        }

        public ShortestPathsMatrix build() {
            return new ShortestPathsMatrix(this);
        }
    }


    public double[][] getShortestDistances() {
        return shortestDistances;
    }

    public void setShortestDistances(double[][] shortestDistances) {
        this.shortestDistances = shortestDistances;
    }

    public Map<Integer, Integer> getLocationIdToIndexMap() {
        return locationIdToIndexMap;
    }

    public void setLocationIdToIndexMap(Map<Integer, Integer> locationIdToIndexMap) {
        this.locationIdToIndexMap = locationIdToIndexMap;
    }


    public int getMatrixIndexByLocationId(int locationId) {
        return MatrixUtils.getMatrixIndexByLocationId(
                locationId,
                this.locationIdToIndexMap
        );
    }

    public Integer getMatrixIndexByVertexId(String vertexId) {
        return MatrixUtils.getMatrixIndexByVertexId(
                vertexId,
                this.locationIdToIndexMap
        );
    }

    /*
        Printers
    */

    public void printRawShortestDistances() {
        LOGGER.info("Printing Raw Shortest Distances Matrix:");
        MatrixUtils.printRawShortestDistances(this.shortestDistances);
    }


    public void printShortestDistances(WeightedGraph graph) {
        printShortestDistances(
                graph.getVertexIdsForGraph()
                        .toArray(new String[0])
        );
    }

    public void printShortestDistances(String[] vertexIds) {
        MatrixUtils.printShortestDistances(
                this.shortestDistances,
                vertexIds
        );
    }

    public void printMatrixIndexByLocationId(int locationId) {
        LOGGER.info(
                "Matrix Index Number from Location ID #{}: {}",
                locationId,
                getMatrixIndexByLocationId(locationId)
        );
    }

    public void printMatrixIndexByVertexId(String vertexId) {
        LOGGER.info(
                "Matrix Index Number from Vertex ID #{}: {}",
                vertexId,
                getMatrixIndexByVertexId(vertexId)
        );
    }

    public void printVertexIdByLocationId(int locationId) {
        LOGGER.info(
                "Vertex ID from locationId #{}: {}",
                locationId,
                MatrixUtils.getVertexIdByLocationId(locationId, this.locationIdToIndexMap)
        );
    }


    public void printVertexIdToIndexMap() {
        LOGGER.info("Vertex ID to Shortest Paths Matrix Index Map:");
        locationIdToIndexMap.forEach((locationId, matrixIndex) ->
                LOGGER.info(

                        "Vertex ID: {}, Matrix Index: {}",
                        GraphUtils.createVertexIdFromLocationId(locationId),
                        matrixIndex
                )
        );
    }

    public void printLocationToMatrixIndexMap() {
        LOGGER.info("LocationId to Shortest Paths Matrix Index Map:");
        locationIdToIndexMap.forEach((locationId, matrixIndex) ->
                LOGGER.info(
                        "Location ID: {}, Matrix Index: {}",
                        locationId,
                        matrixIndex
                )
        );
    }

    @Override
    public String toString() {
        Class<?> currClass = ClassConstants.SHORTEST_PATHS_MATRIX;
        String[] fieldNames = {
                "shortestDistances",
                "locationIdToIndexMap"
        };

        String fieldsString = StringFormatters.buildFieldsString(this, fieldNames);
        return StringFormatters.buildToString(currClass, fieldNames, fieldsString);
    }
}
