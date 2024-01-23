package com.solvd.navigator.util;

import com.solvd.navigator.math.graph.Edge;
import com.solvd.navigator.math.graph.GraphType;
import com.solvd.navigator.math.graph.Vertex;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public class BooleanUtils {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.BOOLEAN_UTILS);

    /*
        Abstract checkers
    */
    public static boolean isEmptyOrNullArray(Object[] array) {
        return array == null
                || array.length == 0
                || Arrays.stream(array).allMatch(Objects::isNull);
    }

    public static boolean isNotEmptyOrNullArray(Object[] array) {
        return !isEmptyOrNullArray(array);
    }

    public static boolean isEmptyOrNullMap(Map<?, ?> map) {
        return map == null
                || map.isEmpty();
    }

    public static boolean isNotEmptyOrNullMap(Map<?, ?> map) {
        return !isEmptyOrNullMap(map);
    }

    public static boolean isEmptyOrNullCollection(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmptyOrNullCollection(Collection<?> collection) {
        return isEmptyOrNullCollection(collection);
    }

    public static <T> boolean anyMatchInArray(T[] array, Predicate<T> predicate) {
        return Arrays.stream(array).anyMatch(predicate);
    }

    public static <T> boolean allMatchInArray(T[] array, Predicate<T> predicate) {
        return Arrays.stream(array).allMatch(predicate);
    }

    public static <T> boolean areEqual(T object1, T object2) {
        return Objects.equals(object1, object2);
    }

    public static boolean areCoordinatesWithinRange(
            Double[] coordinates,
            Double[] coordinatesRange
    ) {
        if (coordinatesRange.length % 2 != 0) {
            throw new IllegalArgumentException("coordinatesRange array must have an even length");
        }

        if (coordinates.length * 2 != coordinatesRange.length) {
            throw new IllegalArgumentException("coordinates array length must be half of coordinatesRange array length");
        }

        return allMatchInArray(coordinates, value -> {
            int i = Arrays.asList(coordinates).indexOf(value);
            double min = Math.min(coordinatesRange[i], coordinatesRange[i + 1]);
            double max = Math.max(coordinatesRange[i], coordinatesRange[i + 1]);
            return value >= min && value <= max;
        });
    }

    public static <K, V> boolean containsKeyWithPredicate(Map<K, V> map, K key) {
        return map != null && map.containsKey(key);
    }

    public static boolean isBlankString(String string) {
        return StringUtils.isBlank(string);
    }

    public static boolean isNotBlankString(String string) {
        return !isBlankString(string);
    }

    public static boolean areAnyStringsBlank(String... strings) {
        return anyMatchInArray(strings, BooleanUtils::isBlankString);
    }

    /*
        Specific checkers
    */

    public static boolean isSameDate(LocalDate comparans, LocalDate comparandum) {
        return areEqual(comparans, comparandum);
    }

    public static boolean isSameDate(ZonedDateTime comparans, ZonedDateTime comparandum) {
        return areEqual(comparans, comparandum);
    }


    public static boolean isValidGraphType(GraphType graphType) {
        return anyMatchInArray(
                GraphType.values(),
                validGraphType -> validGraphType == graphType
        );
    }

    public static boolean isInvalidVertex(Vertex vertex) {
        return vertex == null || isBlankString(vertex.getVertexId());
    }

    public static boolean areInvalidVertices(Vertex... vertices) {
        return anyMatchInArray(vertices, BooleanUtils::isInvalidVertex);
    }

    public static boolean vertexExists(String vertexId, Map<String, Vertex> vertices) {
        return containsKeyWithPredicate(vertices, vertexId);
    }

    public static boolean doAnyVerticesExist(Map<String, Vertex> vertices, String... vertexIds) {
        return anyMatchInArray(vertexIds, vertices::containsKey);
    }

    public static boolean edgeExists(String vertexId1, String vertexId2, Map<String, List<Edge>> adjacencyList) {
        return
                containsKeyWithPredicate(adjacencyList, vertexId1)
                        && adjacencyList
                        .get(vertexId1)
                        .stream()
                        .anyMatch(edge -> {
                                    String currEdgeVertexId2 = edge.getVertex2().getVertexId();
                                    return areEqual(currEdgeVertexId2, vertexId2);
                                }
                        );
    }

    public static boolean areSameCoordinates(Vertex vertex, Vertex existingVertex) {
        return areEqual(existingVertex.getPoint(), vertex.getPoint());
    }

    public static boolean isDoubleNaN(double value) {
        return Double.isNaN(value);
    }

    public static boolean areDoublesNan(Double... values) {
        return allMatchInArray(values, BooleanUtils::isDoubleNaN);
    }


    private BooleanUtils() {
        ExceptionUtils.preventUtilityInstantiation();
    }
}
