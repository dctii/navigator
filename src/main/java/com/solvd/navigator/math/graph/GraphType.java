package com.solvd.navigator.math.graph;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum GraphType {
    WEIGHTED_UNDIRECTED("Weighted Undirected Graph");

    private static final Logger LOGGER = LogManager.getLogger(GraphType.class);
    private final String name;

    GraphType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
