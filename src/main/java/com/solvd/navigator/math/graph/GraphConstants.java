package com.solvd.navigator.math.graph;

import com.solvd.navigator.util.ClassConstants;
import com.solvd.navigator.util.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class GraphConstants {
    private static final Logger LOGGER = LogManager.getLogger(ClassConstants.GRAPH_CONSTANTS);

    public static final int COORDINATE_MIN = 0;
    public static final int ABSCISSA_MIN = COORDINATE_MIN;
    public static final int ORDINATE_MIN = COORDINATE_MIN;
    public static final int COORDINATE_MAX = 9; // determines `n`, so if '9', then graph will be a 10x10 table
    public static final int ABSCISSA_MAX = COORDINATE_MAX;
    public static final int ORDINATE_MAX = COORDINATE_MAX;
    public static final double POINT_ADJACENT_DISTANCE = 0.3;
    public static final Set<Point> RIVER = Set.of(
            new Point(0, 3), new Point(1, 3), new Point(2, 3), new Point(3, 3),
            // a "bridge" is to be at coordinates (4, 3)
            new Point(5, 3), new Point(6, 3), new Point(7, 3), new Point(8, 3),
            new Point(9, 3)
    );
    public static final Set<Point> BIG_BUILDING_ONE = Set.of(
            new Point(2, 0), new Point(3, 0),
            new Point(2, 1), new Point(3, 1)
    );
    public static final Set<Point> BIG_BUILDING_TWO = Set.of(
            new Point(5, 5), new Point(6, 5),
            new Point(5, 6), new Point(6, 6)
    );

    public static final Set<Point> EXCLUDED_COORDINATES =
            Stream.of(RIVER, BIG_BUILDING_ONE, BIG_BUILDING_TWO)
                    .flatMap(Set::stream)
                    .collect(Collectors.toUnmodifiableSet());

    public static final Set<Integer> EXCLUDED_LOCATION_IDS = Set.of(
            // RIVER
            31, 32, 33, 34, 36, 37, 38, 39, 40,
            // BIG_BUILDING_ONE
            3, 4, 13, 14,
            // BIG_BUILDING_TWO
            56, 57, 66, 67

    ); // 17 in total


    private GraphConstants() {
        ExceptionUtils.preventConstantsInstantiation();
    }
}
