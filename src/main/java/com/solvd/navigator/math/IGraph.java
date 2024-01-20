package com.solvd.navigator.math;

public interface IGraph {
    void addVertex(Vertex vertex);

    boolean removeVertex(String vertexId);

    void addEdge(String vertexId1, String vertex2, int weight);

    boolean removeEdge(String vertexId1, String vertexId2);

    void printGraph();
}
