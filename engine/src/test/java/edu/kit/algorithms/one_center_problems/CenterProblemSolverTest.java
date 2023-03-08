package edu.kit.algorithms.one_center_problems;

import edu.kit.algorithms.utils.Point;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CenterProblemSolverTest {

    @Test
    void solveOneCenterWithL1() {
        assert true;

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
        var centerSolver = new CenterProblemSolver(points);
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
        var centerSolver = new CenterProblemSolver(null);
        var transformedBack = centerSolver.transformBack(new Point(14.5, -0.5));
        assertEquals(new Point(7.5, 7), new Point(
                Math.round(transformedBack.x() * 100) / 100.0,
                Math.round(transformedBack.y() * 100) / 100.0)
        );

    }
}