package com.solvd.navigator.math;

public interface Graph {
    void addVertex(Vertex vertex);

    void addVertex(String vertexId, String vertexName, Point coordinates);

    void addVertex(String vertexId, String vertexName, double x, double y);

    void addVertex(String vertexId, Point coordinates);

    void addVertex(String vertexId, double x, double y);


    void addEdge(String vertex1, String vertex2, int weight);
}
