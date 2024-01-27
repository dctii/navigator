package com.solvd.navigator.math.util;

import com.solvd.navigator.bin.Location;
import com.solvd.navigator.exception.InvalidVertexException;
import com.solvd.navigator.math.graph.Direction;
import com.solvd.navigator.math.graph.GraphConstants;
import com.solvd.navigator.math.graph.Point;
import com.solvd.navigator.math.graph.Vertex;
import com.solvd.navigator.math.graph.WeightedGraph;
import com.solvd.navigator.util.BooleanUtils;
import com.solvd.navigator.util.ClassConstants;
import com.solvd.navigator.util.ExceptionUtils;
import com.solvd.navigator.util.StringConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
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


    public static String findVertexIdByLocationId(Location location, WeightedGraph graph) {
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

    public static int getVertexIndex(String vertexId, String[] vertexIds) {
        return IntStream.range(0, vertexIds.length)
                .filter(i -> vertexId.equals(vertexIds[i]))
                .findFirst()
                .orElseGet(() -> {
                    LOGGER.error("Vertex ID {} not found in the list of vertex ids.", vertexId);
                    return -1; // Not found
                });
    }


    public static Integer parseIntFromVertexId(String vertexId) {
        if (
                vertexId != null
                        && vertexId.startsWith(String.valueOf(StringConstants.UPPER_CASE_V_CHAR)
                )
        ) {
            try {
                return Integer.parseInt(vertexId.substring(1));
            } catch (NumberFormatException e) {
                throw new InvalidVertexException("Invalid format for vertex ID: " + vertexId);
            }
        } else {
            throw new InvalidVertexException("Vertex ID does not start with 'V': " + vertexId);
        }
    }

    public static String createVertexIdFromLocationId(int locationId) {
        return StringUtils.join(
                String.valueOf(StringConstants.UPPER_CASE_V_CHAR),
                locationId
        );
    }

    private GraphUtils() {
        ExceptionUtils.preventUtilityInstantiation();
    }
}
