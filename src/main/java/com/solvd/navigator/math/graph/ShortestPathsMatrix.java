package com.solvd.navigator.math.graph;

import com.solvd.navigator.math.util.GraphUtils;
import com.solvd.navigator.util.ClassConstants;
import com.solvd.navigator.util.StringFormatters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ShortestPathsMatrix {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.SHORTEST_PATHS_MATRIX);
    private double[][] shortestDistances;
    private Map<String, Integer> vertexIdToIndexMap; // mapping from vertex ID to index in the matrix
    private Map<Integer, Integer> locationToVertexIndexMap;

    private ShortestPathsMatrix(Builder builder) {
        this.shortestDistances = builder.shortestDistances;
        this.vertexIdToIndexMap = builder.vertexIdToIndexMap;
        this.locationToVertexIndexMap = builder.locationToVertexIndexMap;
    }

    public static class Builder {
        private double[][] shortestDistances;
        private Map<String, Integer> vertexIdToIndexMap;
        private Map<Integer, Integer> locationToVertexIndexMap;

        public Builder() {
            this.vertexIdToIndexMap = new HashMap<>();
            this.locationToVertexIndexMap = new HashMap<>();
        }

        public Builder setShortestDistances(double[][] shortestDistances) {
            this.shortestDistances = shortestDistances;
            return this;
        }

        public Builder setVertexIdToIndexMap(Map<String, Integer> vertexIdToIndexMap) {
            this.vertexIdToIndexMap = vertexIdToIndexMap;
            return this;
        }

        public Builder setLocationToVertexIndexMap(Map<Integer, Integer> locationToVertexIndexMap) {
            this.locationToVertexIndexMap = locationToVertexIndexMap;
            return this;
        }

        public ShortestPathsMatrix build() {
            return new ShortestPathsMatrix(this);
        }
    }

    public double getDistanceBetween(String vertexFromId, String vertexToId) {
        Integer fromIndex = vertexIdToIndexMap.get(vertexFromId);
        Integer toIndex = vertexIdToIndexMap.get(vertexToId);

        if (fromIndex == null || toIndex == null) {
            throw new IllegalArgumentException("One of the vertices does not exist in the matrix.");
        }

        return shortestDistances[fromIndex][toIndex];
    }

    public double[][] getShortestDistances() {
        return shortestDistances;
    }

    public void setShortestDistances(double[][] shortestDistances) {
        this.shortestDistances = shortestDistances;
    }

    public Map<String, Integer> getVertexIdToIndexMap() {
        return vertexIdToIndexMap;
    }

    public void setVertexIdToIndexMap(Map<String, Integer> vertexIdToIndexMap) {
        this.vertexIdToIndexMap = vertexIdToIndexMap;
    }

    public Map<Integer, Integer> getLocationToVertexIndexMap() {
        return locationToVertexIndexMap;
    }

    public void setLocationToVertexIndexMap(Map<Integer, Integer> locationToVertexIndexMap) {
        this.locationToVertexIndexMap = locationToVertexIndexMap;
    }

    public void printShortestDistances(String[] vertexIds) {
        GraphUtils.printShortestDistances(
                this.shortestDistances,
                vertexIds // get vertexIds from graph.getVertexIdsForGraph().toArray(new String[0])
        );
    }

    public void printShortestDistances(WeightedGraph graph) {
        printShortestDistances(
                graph.getVertexIdsForGraph()
                        .toArray(new String[0])
        );
    }

    @Override
    public String toString() {
        Class<?> currClass = ClassConstants.SHORTEST_PATHS_MATRIX;
        String[] fieldNames = {
                "shortestDistances",
                "vertexIdToIndexMap",
                "locationToVertexIndexMap"
        };

        String fieldsString = StringFormatters.buildFieldsString(this, fieldNames);
        return StringFormatters.buildToString(currClass, fieldNames, fieldsString);
    }
}
