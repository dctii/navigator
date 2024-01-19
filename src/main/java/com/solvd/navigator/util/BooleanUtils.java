package com.solvd.navigator.util;

import com.solvd.navigator.math.Edge;
import com.solvd.navigator.math.GraphType;
import com.solvd.navigator.math.Vertex;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BooleanUtils {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.BOOLEAN_UTILS);

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

    public static boolean isBlankString(String string) {
        return StringUtils.isBlank(string);
    }

    public static boolean isNotBlankString(String string) {
        return !isBlankString(string);
    }

    public static boolean areAnyStringsBlank(String... strings) {
        return Arrays.stream(strings).anyMatch(BooleanUtils::isBlankString);
    }


    public static boolean isSameDate(LocalDate comparans, LocalDate comparandum) {
        return comparans.isEqual(comparandum);
    }

    public static boolean isSameDate(ZonedDateTime comparans, ZonedDateTime comparandum) {
        return comparans.isEqual(comparandum);
    }

    public static boolean isWeekend(DayOfWeek dayOfWeek) {
        final DayOfWeek[] weekends = {
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
        };

        return Arrays
                .asList(weekends)
                .contains(dayOfWeek);
    }

    public static boolean isValidEmail(String email) {
        int atSignPosition = email.indexOf(StringConstants.AT_SIGN);
        int dotPosition = email.lastIndexOf(StringConstants.FULL_STOP);

        return atSignPosition > 0
                && dotPosition > atSignPosition + 1
                && dotPosition < email.length() - 1;
    }

    public static boolean isValidGraphType(GraphType graphType) {
        return Arrays.stream(GraphType.values())
                .anyMatch(validGraphType -> validGraphType == graphType);
    }

    public static boolean isInvalidVertex(Vertex vertex) {
        return vertex == null || isBlankString(vertex.getVertexId());
    }

    public static boolean areInvalidVertices(Vertex... vertices) {
        return Arrays.stream(vertices)
                .anyMatch(BooleanUtils::isInvalidVertex);
    }

    public static boolean vertexExists(String vertexId, Map<String, Vertex> vertices) {
        return vertices.containsKey(vertexId);
    }

    public static boolean doAnyVerticesExist(Map<String, Vertex> vertices, String... vertexIds) {
        return Arrays.stream(vertexIds).anyMatch(vertices::containsKey);
    }

    public static boolean edgeExists(String vertexId1, String vertexId2, Map<String, List<Edge>> adjacencyList) {
        List<Edge> edges = adjacencyList.get(vertexId1);
        return
                edges != null
                        && edges.stream()
                        .anyMatch(edge -> edge.getVertex2().getVertexId().equals(vertexId2));
    }

    public static boolean areSameCoordinates(Vertex vertex, Vertex existingVertex) {
        return
                existingVertex.getCoordinates() != null
                        && existingVertex.getCoordinates().equals(vertex.getCoordinates());
    }


    private BooleanUtils() {
        ExceptionUtils.preventUtilityInstantiation();
    }
}
