package com.solvd.navigator.math.util;

import com.solvd.navigator.bin.Location;
import com.solvd.navigator.math.graph.Direction;
import com.solvd.navigator.math.graph.GraphConstants;
import com.solvd.navigator.math.graph.Point;
import com.solvd.navigator.math.graph.Vertex;
import com.solvd.navigator.math.graph.WeightedGraph;
import com.solvd.navigator.util.BooleanUtils;
import com.solvd.navigator.util.ClassConstants;
import com.solvd.navigator.util.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GraphUtils {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.GRAPH_UTILS);

    public static WeightedGraph generateFixedPositionGraph(int coordinateMax) {
        WeightedGraph graph = new WeightedGraph();

        // generate vertices
        IntStream.rangeClosed(0, coordinateMax).boxed()
                .flatMap(x ->
                        IntStream.rangeClosed(0, coordinateMax)
                                .mapToObj(y -> {
                                    Point coordinates = new Point(x, y);
                                    Vertex newVertex = null;
                                    if (!GraphConstants.EXCLUDED_COORDINATES.contains(coordinates)) {
                                        String vertexId =
                                                calculateVertexId(x, y, coordinateMax);
                                        newVertex = new Vertex(vertexId, coordinates);
                                    }

                                    return newVertex;
                                })
                )
                .filter(vertex -> vertex != null)
                .forEach(graph::addVertex);

        // generate edges
        IntStream.rangeClosed(0, coordinateMax).forEach(x ->
                IntStream.rangeClosed(0, coordinateMax)
                        .forEach(y -> {
                            String vertexId =
                                    calculateVertexId(x, y, coordinateMax);

                            // only execute connectToNeighbors if vertices exist in the graph
                            if (graph.getVertexIdsForGraph().contains(vertexId)) {
                                connectToNeighbors(
                                        graph,
                                        x,
                                        y,
                                        coordinateMax,
                                        vertexId
                                );
                            }
                        })
        );

        return graph;
    }

    /* Connect vertices to neighbors

        Edge is the line. For example, V59 has an outgoing direction to V60
        and V60 has an outgoing direction to V59. This means that both are also
        incoming to each other, making the graph "undirected". Other connections
        can be reviewed.


        Vertex: V59, Coordinates: {8.0, 5.0}
            ...
         -> Edge V59-V60 to: V60 {9.0, 5.0}
        Vertex: V60, Coordinates: {9.0, 5.0}
         -> Edge V60-V59 to: V59 {8.0, 5.0}
            ...

        Vertex: V1, Coordinates: {0.0, 0.0}
         -> Edge V1-V11 to: V11 {0.0, 1.0} (Distance: 0.15)
         -> Edge V1-V2 to: V2 {1.0, 0.0} (Distance: 0.15)
        */
    public static void connectToNeighbors(
            WeightedGraph graph,
            double x,
            double y,
            double coordinateMax,
            String vertexId
    ) {

        // possible relative directions for each vertex, they do not cross diagonally
        Direction.Builder directionBuilder = new Direction.Builder();
        Direction[] directions = {
                directionBuilder.dx(-1.00).dy(0.00).build(),  // horizontal left
                directionBuilder.dx(0.00).dy(-1.00).build(),  // vertical down
                directionBuilder.dx(0.00).dy(1.00).build(),   // vertical up
                directionBuilder.dx(1.00).dy(0.00).build()   // horizontal right
        };

        /*
            Determines the range of coordinates:
                - constrained as a perfect square between (0, 0) and (maxX, maxY)
                    - e.g., if `maxX = maxY = 9`, then between (0, 0) and (9, 9), making for 100 points

        TODO: move this to `generateFixedPositionGraph()`, have this take in a `Double[] coordinatesRange` as a param
        */
        double minX, minY, maxX, maxY;
        minX = minY = 0.0;
        maxX = maxY = coordinateMax;
        Double[] coordinatesRange = {minX, maxX, minY, maxY};

        Arrays.stream(directions)
                .map(direction -> {
                    // calculate new relative coordinates
                    double newX = x + direction.getX();
                    double newY = y + direction.getY();
                    return new Double[]{newX, newY};
                })
                .filter(newCoordinates ->
                        // check if the new coordinates are within the graph's bounds
                        BooleanUtils.areCoordinatesWithinRange(newCoordinates, coordinatesRange)
                )
                .map(newCoordinates -> {
                    // generate a vertexId for the coordinates
                    double newX = newCoordinates[0];
                    double newY = newCoordinates[1];
                    return calculateVertexId(
                            (int) newX,
                            (int) newY,
                            (int) coordinateMax
                    );
                })
                // filter out any possible vertexIds that don't exist
                .filter(neighborId -> graph.getVertexIdsForGraph().contains(neighborId))
                .forEach(neighborId ->
                        graph.addEdge(
                                vertexId,
                                neighborId,
                                0.15 // set distance to 0.15km for all
                        )
                );
    }

    public static String calculateVertexId(int x, int y, int coordinateMax) {
        int yShift = y * (coordinateMax + 1);
        int xOffset = x + 1;
        int vertexIndex = yShift + xOffset;

        return String.format("V%d", vertexIndex);
    }

    public static Map<Integer, String> mapLocationIdsToVertexIds(List<Location> locations, WeightedGraph graph) {
        return locations.stream()
                .filter(location -> findMatchingVertexId(location, graph) != null) // filter out locations without matching vertex IDs
                .collect(Collectors.toMap(
                        Location::getLocationId, // key mapper
                        location -> findMatchingVertexId(location, graph) // value mapper
                ));
    }


    public static String findMatchingVertexId(Location location, WeightedGraph graph) {
        return graph.getVertexIdsForGraph().stream()
                .map(graph::getVertex) // get vertex objects by vertexId
                .filter(vertex ->
                        vertex.getX() == location.getCoordinateX()
                                && vertex.getY() == location.getCoordinateY()
                ) // filter by matching coordinates
                .findAny()
                .map(Vertex::getVertexId)
                .orElse(null); // return null if no match is found
    }

    public static Map<Integer, Integer> createLocationToVertexIndexMap(
            Map<Integer, String> locationIdToVertexIdMap,
            WeightedGraph graph
    ) {
        return locationIdToVertexIdMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> getVertexIndex(
                                entry.getValue(),
                                graph.getVertexIdsForGraph().toArray(new String[0])
                        )
                ));
    }

    public static Map<String, Integer> createVertexIdToIndexMap(WeightedGraph graph) {
        return graph.getVertexIdsForGraph().stream()
                .collect(Collectors.toMap(
                        vertexId -> vertexId,
                        vertexId -> getVertexIndex(
                                vertexId,
                                graph.getVertexIdsForGraph().toArray(new String[0])
                        )
                ));
    }


    /*
        Floyd-Warshall Algorithm
        https://www.geeksforgeeks.org/floyd-warshall-algorithm-dp-16/
    */
    public static double[][] executeFloydWarshall(WeightedGraph graph) {
        int totalVertices = graph.getVertexIdsForGraph().size();
        double[][] shortestDistances = new double[totalVertices][totalVertices];
        int[][] nextVertex = new int[totalVertices][totalVertices];
        String[] vertexIds = graph.getVertexIdsForGraph().toArray(new String[0]);

        initializeDistancesAndPaths(shortestDistances, nextVertex, totalVertices);
        fillDistancesFromEdges(graph, shortestDistances, nextVertex, vertexIds);
        applyFloydWarshall(shortestDistances, nextVertex, totalVertices);

        return shortestDistances;
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
                int initialVertexIndex = getVertexIndex(vertexId, vertexIds);
                int terminalVertexIndex = getVertexIndex(edge.getVertex2().getVertexId(), vertexIds);
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

    public static int getVertexIndex(String vertexId, String[] vertexIds) {
        return IntStream.range(0, vertexIds.length)
                .filter(i -> vertexId.equals(vertexIds[i]))
                .findFirst()
                .orElseGet(() -> {
                    LOGGER.error("Vertex ID {} not found in the list of vertex ids.", vertexId);
                    return -1; // Not found
                });
    }

    public static void printShortestDistances(
            double[][] shortestDistances,
            String[] vertexIds
    ) {
        LOGGER.info("Shortest Distances Matrix:");
        IntStream.range(0, vertexIds.length).forEach(fromIndex -> {
            IntStream.range(0, vertexIds.length).forEach(toIndex -> {
                String distance = shortestDistances[fromIndex][toIndex] == Double.POSITIVE_INFINITY
                        ? "INF"
                        : String.format("%.2f", shortestDistances[fromIndex][toIndex]);
                LOGGER.info(vertexIds[fromIndex] + " to " + vertexIds[toIndex] + ": " + distance);
            });
        });
    }


    private GraphUtils() {
        ExceptionUtils.preventUtilityInstantiation();
    }
}
