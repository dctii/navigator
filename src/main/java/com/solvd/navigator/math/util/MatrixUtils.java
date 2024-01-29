package com.solvd.navigator.math.util;

import com.solvd.navigator.bin.Location;
import com.solvd.navigator.exception.InvalidMatrixIndexException;
import com.solvd.navigator.exception.InvalidVertexException;
import com.solvd.navigator.math.graph.ShortestPathsMatrix;
import com.solvd.navigator.math.graph.WeightedGraph;
import com.solvd.navigator.util.ClassConstants;
import com.solvd.navigator.util.ExceptionUtils;
import com.solvd.navigator.util.StringConstants;
import com.solvd.navigator.util.StringFormatters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MatrixUtils {

    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.MATRIX_UTILS);

    public static Map<Integer, Integer> mapLocationIdsToMatrixIndexes(List<Location> locations, WeightedGraph graph) {
        return locations.stream()
                .map(location -> GraphUtils.findVertexIdByLocationId(location, graph))
                .filter(Objects::nonNull) // filter out null vertices (excluded locations)
                .collect(Collectors.toMap(
                        GraphUtils::parseIntFromVertexId, // key: locationId
                        vertexId -> GraphUtils.getVertexIndex(
                                vertexId,
                                graph.getVertexIdsForGraph().toArray(new String[0])
                        ) // val: matrix index
                ));
    }

    public static int getLocationIdByMatrixIndex(int indexNumber, Map<Integer, Integer> idToIndexMap) {
        for (Map.Entry<Integer, Integer> entry : idToIndexMap.entrySet()) {
            if (Objects.equals(entry.getValue(), indexNumber)) {
                return entry.getKey();
            }
        }
        final String INVALID_MATRIX_EXCEPTION_MSG = "Matrix index " + indexNumber + " not found in the idToIndexMap.";
        LOGGER.error(INVALID_MATRIX_EXCEPTION_MSG);
        throw new InvalidMatrixIndexException(INVALID_MATRIX_EXCEPTION_MSG);
    }


    public static int getMatrixIndexByLocationId(int locationId, Map<Integer, Integer> idToIndexMap) {
        Integer index = idToIndexMap.get(locationId);
        if (index != null) {
            return index;
        } else {
            final String INVALID_MATRIX_EXCEPTION_MSG =
                    "Location ID " + locationId + " not found in the matrix.";
            LOGGER.error(INVALID_MATRIX_EXCEPTION_MSG);
            throw new InvalidMatrixIndexException(INVALID_MATRIX_EXCEPTION_MSG);
        }
    }

    public static Integer getMatrixIndexByVertexId(String vertexId,
                                                   Map<Integer, Integer> idToIndexMap) {
        if (
                vertexId != null
                        && vertexId.startsWith(String.valueOf(StringConstants.UPPER_CASE_V_CHAR))
        ) {
            try {
                int locationId = GraphUtils.parseIntFromVertexId(vertexId);

                return getMatrixIndexByLocationId(locationId, idToIndexMap);
            } catch (NumberFormatException e) {
                final String NUMBER_FORMAT_EXCEPTION_MSG =
                        "NumberFormatException thrown: invalid number format for vertex ID (e.g., 'V25', 'V100', etc.): " + vertexId;
                LOGGER.error(NUMBER_FORMAT_EXCEPTION_MSG);
                throw new InvalidVertexException(NUMBER_FORMAT_EXCEPTION_MSG);
            }
        } else {
            final String INVALID_VERTEX_EXCEPTION_MSG =
                    "Vertex ID does not start with 'V': " + vertexId;
            LOGGER.error(INVALID_VERTEX_EXCEPTION_MSG);
            throw new InvalidVertexException(INVALID_VERTEX_EXCEPTION_MSG);
        }
    }

    public static String getVertexIdByLocationId(int locationId, Map<Integer, Integer> idToIndexMap) {
        if (!idToIndexMap.containsKey(locationId)) {
            final String INVALID_MATRIX_INDEX_EXCEPTION_MSG =
                    "No vertex index found for location ID: " + locationId + " in the matrix.";
            LOGGER.error(INVALID_MATRIX_INDEX_EXCEPTION_MSG);
            throw new InvalidMatrixIndexException(INVALID_MATRIX_INDEX_EXCEPTION_MSG);
        }

        return GraphUtils.createVertexIdFromLocationId(locationId);
    }


    public static void printShortestDistances(
            double[][] shortestDistances,
            String[] vertexIds
    ) {
        LOGGER.info("Shortest Distances Matrix:");
        IntStream.range(0, vertexIds.length).forEach(fromIndex -> {
            IntStream.range(0, vertexIds.length).forEach(toIndex -> {
                String distance = shortestDistances[fromIndex][toIndex] == Double.POSITIVE_INFINITY
                        ? StringConstants.INF
                        : String.format(
                        StringConstants.DECIMAL_FORMAT_OF_SCALE_2,
                        shortestDistances[fromIndex][toIndex]
                );
                LOGGER.info("{} to {}: {}",
                        vertexIds[fromIndex],
                        vertexIds[toIndex],
                        distance
                );
            });
        });
    }

    public static void printRawShortestDistances(double[][] shortestDistances) {
        IntStream.range(0, shortestDistances.length).forEach(i -> {
            IntStream.range(0, shortestDistances[i].length).forEach(j -> {
                String distance = (shortestDistances[i][j] == Double.POSITIVE_INFINITY)
                        ? StringConstants.INF
                        : String.format(
                        StringConstants.DECIMAL_FORMAT_OF_SCALE_2,
                        shortestDistances[i][j]
                );
                LOGGER.info("{}{}: {}",
                        StringFormatters.nestInBrackets(String.valueOf(i)),
                        StringFormatters.nestInBrackets(String.valueOf(j)),
                        distance
                );
            });
        });
    }

    /*
            Floyd-Warshall Algorithm
            https://www.geeksforgeeks.org/floyd-warshall-algorithm-dp-16/
        */

    public static class FloydWarshallResult {
        private final double[][] shortestDistances;
        private final int[][] nextLocations;

        public FloydWarshallResult(double[][] shortestDistances, int[][] nextLocations) {
            this.shortestDistances = shortestDistances;
            this.nextLocations = nextLocations;
        }

        double[][] getShortestDistances() {
            return shortestDistances;
        }

        int[][] getNextLocations() {
            return nextLocations;
        }
    }

    public static FloydWarshallResult runFloydWarshall(WeightedGraph graph) {
        int totalVertices = graph.getVertexIdsForGraph().size();
        double[][] shortestDistances = new double[totalVertices][totalVertices];
        int[][] nextLocations = new int[totalVertices][totalVertices];
        String[] vertexIds = graph.getVertexIdsForGraph().toArray(new String[0]);

        initializeDistancesAndPaths(shortestDistances, nextLocations, totalVertices);
        fillDistancesFromEdges(graph, shortestDistances, nextLocations, vertexIds);
        applyFloydWarshall(shortestDistances, nextLocations, totalVertices);

        return new FloydWarshallResult(shortestDistances, nextLocations);
    }

    private static void initializeDistancesAndPaths(double[][] distances, int[][] next, int totalVertices) {
        IntStream.range(0, totalVertices).forEach(initialIndex -> {
            IntStream.range(0, totalVertices).forEach(terminalIndex -> {
                if (initialIndex == terminalIndex) {
                    distances[initialIndex][terminalIndex] = 0;
                } else {
                    distances[initialIndex][terminalIndex] = Double.POSITIVE_INFINITY;
                }
                next[initialIndex][terminalIndex] = -1; // no next vertex if same
            });
        });


//        for (int initialIndex = 0; initialIndex < totalVertices; initialIndex++) {
//            for (int terminalIndex = 0; terminalIndex < totalVertices; terminalIndex++) {
//                // no path at the start, do this
//                if (initialIndex == terminalIndex) {
//                    distances[initialIndex][terminalIndex] = 0;
//                } else {
//                    distances[initialIndex][terminalIndex] = Double.POSITIVE_INFINITY;
//                }
//                next[initialIndex][terminalIndex] = -1; // no next vertex if same
//            }
//        }
    }

    private static void fillDistancesFromEdges(
            WeightedGraph graph,
            double[][] distances,
            int[][] next,
            String[] vertexIds
    ) {

        Arrays.stream(vertexIds).forEach(vertexId -> {
            graph.getEdges(vertexId).forEach(edge -> {
                int initialVertexIndex = GraphUtils.getVertexIndex(vertexId, vertexIds);
                int terminalVertexIndex = GraphUtils.getVertexIndex(edge.getVertex2().getVertexId(), vertexIds);
                distances[initialVertexIndex][terminalVertexIndex] = edge.getDistance();
                next[initialVertexIndex][terminalVertexIndex] = terminalVertexIndex;
            });
        });

//        for (String vertexId : vertexIds) {
//            for (Edge edge : graph.getEdges(vertexId)) {
//                int initialVertexIndex = getIndex(vertexId, vertexIds);
//                int terminalVertexIndex = getIndex(edge.getVertex2().getVertexId(), vertexIds);
//                distances[initialVertexIndex][terminalVertexIndex] = edge.getDistance();
//                next[initialVertexIndex][terminalVertexIndex] = terminalVertexIndex;
//            }
//        }
    }

    private static void applyFloydWarshall(
            double[][] distances,
            int[][] next,
            int totalVertices
    ) {
        for (int intermediate = 0; intermediate < totalVertices; intermediate++) {
            for (int initialVertex = 0; initialVertex < totalVertices; initialVertex++) {
                for (int terminalVertex = 0; terminalVertex < totalVertices; terminalVertex++) {
                    // distance from the 'initial' vertex to the current 'intermediate' vertex
                    double distFromInitialToInter = distances[initialVertex][intermediate];

                    // distance from the 'intermediate' vertex to the 'terminal' vertex
                    double distFromInterToTerminal = distances[intermediate][terminalVertex];

                    // current shortest known distance from the 'initial' vertex to the 'terminal' vertex
                    double currentShortestDist = distances[initialVertex][terminalVertex];

                    if (
                            distFromInitialToInter + distFromInterToTerminal < currentShortestDist
                    ) {
                        distances[initialVertex][terminalVertex] = distances[initialVertex][intermediate] + distances[intermediate][terminalVertex];
                        next[initialVertex][terminalVertex] = next[initialVertex][intermediate];
                    }
                }
            }
        }
    }
    public static List<Integer> getPath(ShortestPathsMatrix matrix, int fromLocationId, int toLocationId) {

        // get corresponding matrix index for locationId
        int fromIndex = matrix.getMatrixIndexByLocationId(fromLocationId);
        int toIndex = matrix.getMatrixIndexByLocationId(toLocationId);
        Map<Integer, Integer> idToIndexMap = matrix.getLocationIdToIndexMap();

        int[][] nextLocations = matrix.getNextLocations();

        if (nextLocations[fromIndex][toIndex] == -1) {
            LOGGER.warn("No path exists from location ID {} to location ID {}", fromLocationId, toLocationId);
            return new ArrayList<>(); // no path exists
        }

        List<Integer> path = new ArrayList<>();

        while (fromIndex != toIndex) {
            int locationId = MatrixUtils.getLocationIdByMatrixIndex(fromIndex, idToIndexMap);
            path.add(locationId);
            fromIndex = nextLocations[fromIndex][toIndex];
        }
        int destinationLocationId = MatrixUtils.getLocationIdByMatrixIndex(toIndex, idToIndexMap);
        path.add(destinationLocationId); // add the destination to the path

        return path;
    }


    private MatrixUtils() {
        ExceptionUtils.preventUtilityInstantiation();
    }
}
