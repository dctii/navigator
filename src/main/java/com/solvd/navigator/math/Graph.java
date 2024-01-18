package com.solvd.navigator.math;

public interface Graph {
    void addVertex(String vertex);

    void addEdge(String vertex1, String vertex2, int weight);
}
