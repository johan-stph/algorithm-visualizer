package edu.kit.algorithms.one_center_problems;

import edu.kit.algorithms.utils.Point;
import edu.kit.algorithms.utils.PointWithWeight;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class CenterProblemSolverTest {


    @Test
    void solveOneCenterWeighted() {
        List<PointWithWeight> points = List.of(
                new PointWithWeight(new Point(4, 12), 4),
                new PointWithWeight(new Point(8, 2), 2),
                new PointWithWeight(new Point(14, 10), 2),
                new PointWithWeight(new Point(2, 3), 8),
                new PointWithWeight(new Point(14, 4), 9)
        );
        var centerSolver = new CenterProblemSolver(points);
        var result = centerSolver.solveOneCenterWeightedWithL1(1);
        assertEquals(1, result.optimalPoint().size());
        assertEquals(new Point(8.17, 3.71), new Point(
                Math.round(result.optimalPoint().get(0).x() * 100) / 100.0,
                Math.round(result.optimalPoint().get(0).y() * 100) / 100.0)
        );
        assertEquals(55.06, Math.round(
                result.cost() * 100) / 100.0
        );
    }

    @Test
    void solveOneCenterUnweightedWithL1() {
        List<Point> points = List.of(
                new Point(4, 12),
                new Point(8, 2),
                new Point(14, 10),
                new Point(2, 3),
                new Point(14, 4)
        );
        var centerSolver = new CenterProblemSolver(points.stream().map(p -> new PointWithWeight(p, 1.0)).toList());
        var result = centerSolver.solveOneCenterUnweightedWithL1(1);
        assertEquals(2, result.optimalPoint().size());
        assertIterableEquals(List.of(
                        new Point(8, 6.5),
                        new Point(7.5, 7)
                ), List.of(
                        new Point(
                                Math.round(result.optimalPoint().get(0).x() * 100) / 100.0,
                                Math.round(result.optimalPoint().get(0).y() * 100) / 100.0
                        ),
                        new Point(
                                Math.round(result.optimalPoint().get(1).x() * 100) / 100.0,
                                Math.round(result.optimalPoint().get(1).y() * 100) / 100.0
                        )
                )
        );
        assertEquals(9.5, Math.round(
                result.cost() * 100) / 100.0
        );

    }

    @Test
    void testTransformation() {
        List<Point> points = List.of(
                new Point(4, 12),
                new Point(8, 2),
                new Point(14, 10),
                new Point(2, 3),
                new Point(14, 4)
        );
        var centerSolver = new CenterProblemSolver(points.stream().map(p -> new PointWithWeight(p, 1.0)).toList());
        var transformedPoints = centerSolver.transformPoints(points);
        assertEquals(5, transformedPoints.size());
        assertEquals(new Point(16, 8), transformedPoints.get(0));
        assertEquals(new Point(10, -6), transformedPoints.get(1));
        assertEquals(new Point(24, -4), transformedPoints.get(2));
        assertEquals(new Point(5, 1), transformedPoints.get(3));
        assertEquals(new Point(18, -10), transformedPoints.get(4));
    }

    @Test
    void testTransformationBack() {
        var centerSolver = new CenterProblemSolver(new ArrayList<>());
        var transformedBack = centerSolver.transformBack(new Point(14.5, -0.5));
        assertEquals(new Point(7.5, 7), new Point(
                Math.round(transformedBack.x() * 100) / 100.0,
                Math.round(transformedBack.y() * 100) / 100.0)
        );

    }
}