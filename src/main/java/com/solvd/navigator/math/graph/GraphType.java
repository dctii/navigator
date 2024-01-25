package com.solvd.navigator.math.graph;

public enum GraphType {
    WEIGHTED_UNDIRECTED("Weighted Undirected Graph");

    private final String name;

    GraphType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
