package com.solvd.navigator.math;

import com.solvd.navigator.util.BooleanUtils;
import com.solvd.navigator.util.ExceptionUtils;

import java.util.Arrays;
import java.util.stream.IntStream;

public class GraphUtils {

    public static WeightedGraph generateFixedPositionGraph(int coordinateMax) {
        WeightedGraph graph = new WeightedGraph();

        // generate vertices
        IntStream.rangeClosed(0, coordinateMax).boxed()
                .flatMap(x ->
                        IntStream.rangeClosed(0, coordinateMax)
                                .mapToObj(y -> {
                                    Point coordinates = new Point(x, y);
                                    String vertexId =
                                            calculateVertexId(x, y, coordinateMax);

                                    return new Vertex(vertexId, coordinates);
                                })
                )
                .forEach(graph::addVertex);


        /* Connect vertices to neighbors

        Edge is the line. For example, V59 has an outgoing direction to V48
        and V48 has an outgoing direction to V59. This means that both are also
        incoming to each other, making the graph "undirected". Other connections
        can be reviewed.


        Vertex: V59, Coordinates: {8.0, 5.0}
         -> Edge V59-V48 to: V48 {7.0, 4.0}
         -> Edge V59-V58 to: V58 {7.0, 5.0}
         -> Edge V59-V68 to: V68 {7.0, 6.0}
         -> Edge V59-V49 to: V49 {8.0, 4.0}
         -> Edge V59-V69 to: V69 {8.0, 6.0}
         -> Edge V59-V50 to: V50 {9.0, 4.0}
         -> Edge V59-V60 to: V60 {9.0, 5.0}
         -> Edge V59-V70 to: V70 {9.0, 6.0}
        Vertex: V48, Coordinates: {7.0, 4.0}
         -> Edge V48-V37 to: V37 {6.0, 3.0} (Weight: 1)
         -> Edge V48-V47 to: V47 {6.0, 4.0} (Weight: 1)
         -> Edge V48-V57 to: V57 {6.0, 5.0} (Weight: 1)
         -> Edge V48-V38 to: V38 {7.0, 3.0} (Weight: 1)
         -> Edge V48-V58 to: V58 {7.0, 5.0} (Weight: 1)
         -> Edge V48-V39 to: V39 {8.0, 3.0} (Weight: 1)
         -> Edge V48-V49 to: V49 {8.0, 4.0} (Weight: 1)
         -> Edge V48-V59 to: V59 {8.0, 5.0} (Weight: 1)
        Vertex: V1, Coordinates: {0.0, 0.0}
         -> Edge V1-V11 to: V11 {0.0, 1.0} (Weight: 1)
         -> Edge V1-V2 to: V2 {1.0, 0.0} (Weight: 1)
         -> Edge V1-V12 to: V12 {1.0, 1.0} (Weight: 1)
        Vertex: V2, Coordinates: {1.0, 0.0}
         -> Edge V2-V1 to: V1 {0.0, 0.0} (Weight: 1)
         -> Edge V2-V11 to: V11 {0.0, 1.0} (Weight: 1)
         -> Edge V2-V12 to: V12 {1.0, 1.0} (Weight: 1)
         -> Edge V2-V3 to: V3 {2.0, 0.0} (Weight: 1)
         -> Edge V2-V13 to: V13 {2.0, 1.0} (Weight: 1)
        */

        IntStream.rangeClosed(0, coordinateMax).forEach(x ->
                IntStream.rangeClosed(0, coordinateMax)
                        .forEach(y -> {
                            String vertexId =
                                    calculateVertexId(x, y, coordinateMax);
                            connectToNeighbors(
                                    graph,
                                    x,
                                    y,
                                    coordinateMax,
                                    vertexId
                            );
                        })
        );
        return graph;
    }

    public static void connectToNeighbors(
            WeightedGraph graph,
            double x,
            double y,
            double coordinateMax,
            String vertexId
    ) {
        Direction[] directions = {
                new Direction.Builder().x(-1.00).y(0.00).build(),  // horizontal left
                new Direction.Builder().x(0.00).y(-1.00).build(),  // vertical down
                new Direction.Builder().x(0.00).y(1.00).build(),   // vertical up
                new Direction.Builder().x(1.00).y(0.00).build()   // horizontal right
        };

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
                .forEach(neighborId -> {
                    // add edges between the current vertex and its neighbor
                    graph.addEdge(vertexId, neighborId, 1); // set weight to 1 for all
                });
    }

    public static String calculateVertexId(int x, int y, int coordinateMax) {
        int yShift = y * (coordinateMax + 1);
        int xOffset = x + 1;
        int vertexIndex = yShift + xOffset;

        return String.format("V%d", vertexIndex);
    }


    private GraphUtils() {
        ExceptionUtils.preventUtilityInstantiation();
    }
}
