package com.solvd.navigator.math.graph;


import com.solvd.navigator.exception.DuplicateEdgeException;
import com.solvd.navigator.exception.EmptyAdjacencyListException;
import com.solvd.navigator.exception.EmptyVerticesMapException;
import com.solvd.navigator.exception.InvalidEdgeException;
import com.solvd.navigator.exception.InvalidVertexException;
import com.solvd.navigator.exception.VertexNotFoundException;
import com.solvd.navigator.util.BooleanUtils;
import com.solvd.navigator.util.StringConstants;
import com.solvd.navigator.util.StringFormatters;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WeightedGraph implements IGraph {
    private static final Logger LOGGER = LogManager.getLogger(WeightedGraph.class);
    private final Map<String, Vertex> vertices;
    private final Map<String, List<Edge>> adjacencyList;


    public WeightedGraph() {
        this.vertices = new HashMap<>();
        this.adjacencyList = new HashMap<>();
    }

    public WeightedGraph(Map<String, Vertex> vertices) {
        if (BooleanUtils.isEmptyOrNullMap(vertices)) {
            LOGGER.error("Vertices map cannot be null or empty.");
            throw new EmptyVerticesMapException("Vertices map cannot be null or empty.");
        }

        this.vertices = vertices;
        this.adjacencyList = new HashMap<>();
    }

    public WeightedGraph(Map<String, List<Edge>> adjacencyList, Map<String, Vertex> vertices) {
        if (BooleanUtils.isEmptyOrNullMap(vertices)) {
            LOGGER.error("Vertices map cannot be null or empty.");
            throw new EmptyVerticesMapException("Vertices map cannot be null or empty.");
        }

        if (BooleanUtils.isEmptyOrNullMap(adjacencyList)) {
            LOGGER.error("Adjacency list cannot be null or empty.");
            throw new EmptyAdjacencyListException("Adjacency list cannot be null or empty.");
        }

        this.vertices = vertices;
        this.adjacencyList = adjacencyList;
    }


    @Override
    public void addVertex(Vertex vertex) {
        if (BooleanUtils.isInvalidVertex(vertex)) {
            LOGGER.error("Vertex must not be null and must have a non-empty ID.");
            throw new InvalidVertexException("Vertex must not be null and must have a non-empty ID.");
        }

        if (BooleanUtils.vertexExists(vertex.getVertexId(), this.vertices)) {
            Vertex existingVertex = vertices.get(vertex.getVertexId());
            LOGGER.warn("Vertex with ID '" + vertex.getVertexId() + "' already exists and will not be added again.");

            if (BooleanUtils.areSameCoordinates(vertex, existingVertex)) {
                LOGGER.info("The coordinates for vertex '" + vertex.getVertexId() + "' are the same as the existing vertex.");
            } else {
                LOGGER.warn("The coordinates for vertex '" + vertex.getVertexId() + "' are different from the existing vertex.");
            }
            return; // don't add if already exists
        }

        vertices.put(vertex.getVertexId(), vertex);
        adjacencyList.put(vertex.getVertexId(), new ArrayList<>());
    }


    public void addVertex(String vertexId, String vertexName, Point coordinates) {
        Vertex newVertex = new Vertex(vertexId, vertexName, coordinates);
        addVertex(newVertex);
    }

    public void addVertex(String vertexId, String vertexName, double x, double y) {
        Vertex newVertex = new Vertex(vertexId, vertexName, x, y);
        addVertex(newVertex);
    }

    public void addVertex(String vertexId, Point coordinates) {
        Vertex newVertex = new Vertex(vertexId, coordinates);
        addVertex(new Vertex(vertexId, coordinates));
    }

    public void addVertex(String vertexId, double x, double y) {
        Vertex newVertex = new Vertex(vertexId, x, y);
        addVertex(newVertex);
    }

    @Override
    public boolean removeVertex(String vertexId) {
        if (BooleanUtils.isBlankString(vertexId)) {
            LOGGER.error("Vertex ID must not be blank.");
            return false;
        }

        Vertex vertex = vertices.get(vertexId);
        if (BooleanUtils.isInvalidVertex(vertex)) {
            LOGGER.warn("Cannot remove a vertex that does not exist.");
            return false;
        }
        // removal of the edges associated with this vertex in other lists
        adjacencyList.values().forEach(list -> list.removeIf(edge ->
                edge.getVertex1().equals(vertex) || edge.getVertex2().equals(vertex)
        ));

        // remove the vertex
        adjacencyList.remove(vertexId);
        vertices.remove(vertexId);
        return true;
    }

    @Override
    public void addEdge(String vertexId1, String vertexId2, double distance) {
        if (BooleanUtils.areAnyStringsBlank(vertexId1, vertexId2)) {
            LOGGER.warn("Vertex IDs must not be blank.");
            throw new InvalidEdgeException("Vertex IDs must not be blank.");
        }

        if (!BooleanUtils.doAnyVerticesExist(this.vertices, vertexId1, vertexId2)) {
            LOGGER.warn("Cannot add the edge {}-{} where one or both vertices do not exist.",
                    vertexId1,
                    vertexId2
            );
            throw new InvalidEdgeException("Cannot add an edge where one or both vertices do not exist.");
        }

        if (BooleanUtils.edgeExists(vertexId1, vertexId2, this.adjacencyList)) {
            LOGGER.warn("Edge {}-{} already exists", vertexId1, vertexId2);
            throw new DuplicateEdgeException("Edge between '" + vertexId1 + "' and '" + vertexId2 + "' already exists");
        }

        Edge edge = new Edge(
                vertices.get(vertexId1),
                vertices.get(vertexId2),
                distance
        );

        adjacencyList.get(vertexId1).add(edge);

        //    LOGGER.debug("Edge between '" + vertexId1 + "' and '" + vertexId2 + "' added successfully.");
    }


    public void addEdge(Vertex vertex1, Vertex vertex2, double distance) {
        if (!vertices.containsKey(vertex1.getVertexId())) {
            addVertex(vertex1);
        }
        if (!vertices.containsKey(vertex2.getVertexId())) {
            addVertex(vertex2);
        }
        addEdge(vertex1.getVertexId(), vertex2.getVertexId(), distance);
    }

    public void addEdge(String vertexId1, String vertexName1, Point coordinates1,
                        String vertexId2, String vertexName2, Point coordinates2,
                        double distance) {
        if (!vertices.containsKey(vertexId1)) {
            addVertex(vertexId1, vertexName1, coordinates1);
        }
        if (!vertices.containsKey(vertexId2)) {
            addVertex(vertexId2, vertexName2, coordinates2);
        }
        addEdge(vertexId1, vertexId2, distance);
    }

    public void addEdge(String vertexId1, String vertexName1, double x1, double y1,
                        String vertexId2, String vertexName2, double x2, double y2,
                        double distance) {

        Point coordinates1 = new Point(x1, y1);
        Point coordinates2 = new Point(x2, y2);

        addEdge(vertexId1, vertexName1, coordinates1,
                vertexId2, vertexName2, coordinates2,
                distance);
    }

    public void addEdge(String vertexId1, Point coordinates1,
                        String vertexId2, Point coordinates2,
                        double distance) {
        addEdge(
                vertexId1, null, coordinates1,
                vertexId2, null, coordinates2,
                distance
        );
    }

    public void addEdge(String vertexId1, double x1, double y1,
                        String vertexId2, double x2, double y2,
                        double distance) {
        Point coordinates1 = new Point(x1, y1);
        Point coordinates2 = new Point(x2, y2);

        addEdge(
                vertexId1, null, coordinates1,
                vertexId2, null, coordinates2,
                distance
        );
    }

    @Override
    public boolean removeEdge(String vertexId1, String vertexId2) {
        if (BooleanUtils.areAnyStringsBlank(vertexId1, vertexId2)) {
            LOGGER.error("Vertex IDs must not be blank.");
            return false;
        }

        Vertex vertex1 = vertices.get(vertexId1);
        Vertex vertex2 = vertices.get(vertexId2);
        if (BooleanUtils.areInvalidVertices(vertex1, vertex2)) {
            LOGGER.warn("Cannot remove an edge where one or both vertices do not exist.");
            return false;
        }
        // remove the edge from both vertices'
        adjacencyList.get(vertexId1).removeIf(edge ->
                edge.getVertex2().equals(vertex2)
        );
        adjacencyList.get(vertexId2).removeIf(edge ->
                edge.getVertex1().equals(vertex1)
        );
        return true;
    }

    public Vertex getVertex(String vertexId) {
        Vertex vertex = vertices.get(vertexId);
        if (BooleanUtils.isInvalidVertex(vertex)) {
            LOGGER.error("Vertex with ID '" + vertexId + "' does not exist.");
            throw new VertexNotFoundException("Vertex with ID '" + vertexId + "' does not exist.");
        }
        return vertex;
    }

    public Set<String> getVertexIdsForGraph() {
        return adjacencyList.keySet();
    }

    public List<Edge> getEdges(String vertexId) {
        if (BooleanUtils.isBlankString(vertexId)) {
            LOGGER.error("Vertex ID must not be blank.");
            throw new IllegalArgumentException("Vertex ID must not be blank.");
        }

        if (!adjacencyList.containsKey(vertexId)) {
            LOGGER.error("Vertex does not exist.");
            throw new VertexNotFoundException("Vertex does not exist.");
        }
        return new ArrayList<>(adjacencyList.get(vertexId));
    }

    @Override
    public void printGraph() {
        LOGGER.info("Printing Graph: ");
        getVertexIdsForGraph().forEach(vertexId -> {
            Vertex vertex = getVertex(vertexId);
            LOGGER.info("Vertex: " + vertexId + ", Coordinates: " + vertex.getCoordinatesString());

            // shows all outgoing edges from this vertex
            getEdges(vertexId).forEach(edge -> {
                if (edge.getVertex1().equals(vertex)) {
                    Vertex otherVertex = edge.getVertex2();
                    String edgeString = "Edge " + edge.getEdgeId() + " to:";
                    String distanceString = StringFormatters.nestInParentheses("Distance: " + edge.getDistance());
                    LOGGER.info(
                            " -> " + StringUtils.joinWith(
                                    StringConstants.SINGLE_WHITESPACE,
                                    edgeString,
                                    otherVertex.getVertexId(),
                                    otherVertex.getCoordinatesString(),
                                    distanceString
                            )
                    );
                }
            });
        });
    }


    @Override
    public String toString() {
        Class<?> currClass = WeightedGraph.class;
        String[] fieldNames = {
                "vertices",
                "adjacencyList"
        };

        String fieldsString = StringFormatters.buildFieldsString(this, fieldNames);
        return StringFormatters.buildToString(currClass, fieldNames, fieldsString);
    }
}
