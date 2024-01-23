package com.solvd.navigator.math.graph;

import com.solvd.navigator.exception.InvalidGraphTypeException;
import com.solvd.navigator.math.util.GraphUtils;
import com.solvd.navigator.util.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GraphFactory {
    private static final Logger LOGGER = LogManager.getLogger(GraphFactory.class);

    private static final String INVALID_GRAPH_TYPE_MSG =
            "That GraphType does not exist. Types: GraphType.WEIGHTED_UNDIRECTED";

    public static IGraph createGraph(GraphType graphType) {
        switch (graphType) {
            case WEIGHTED_UNDIRECTED:
                return new WeightedGraph();
            default:
                LOGGER.error(INVALID_GRAPH_TYPE_MSG);
                throw new InvalidGraphTypeException(INVALID_GRAPH_TYPE_MSG);
        }
    }

    public static IGraph createGraph(GraphType graphType, int coordinateMax) {
        switch (graphType) {
            case WEIGHTED_UNDIRECTED:
                return GraphUtils.generateFixedPositionGraph(coordinateMax);
            default:
                LOGGER.error(INVALID_GRAPH_TYPE_MSG);
                throw new InvalidGraphTypeException(INVALID_GRAPH_TYPE_MSG);
        }
    }


    private GraphFactory() {
        ExceptionUtils.preventFactoryInstantiation();
    }
}
