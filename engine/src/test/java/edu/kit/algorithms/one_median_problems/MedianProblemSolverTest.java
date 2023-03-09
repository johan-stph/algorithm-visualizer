package edu.kit.algorithms.one_median_problems;

import edu.kit.algorithms.utils.Point;
import edu.kit.algorithms.utils.PointWithWeight;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MedianProblemSolverTest {



    @Test
    void solveOneMedianWithL1() {
        List<PointWithWeight> points = List.of(
                new PointWithWeight(new Point(6, 5), 3),
                new PointWithWeight(new Point(10, 3), 2),
                new PointWithWeight(new Point(12, 7), 2),
                new PointWithWeight(new Point(10, 7), 1.5)
        );
        var medianSolver = new MedianProblemSolver(points);
        var result = medianSolver.solveOneMedianWithL1(0.5);
        assertEquals(1, result.optimalPoint().size());
        assertEquals(new Point(10, 5), result.optimalPoint().get(0));
        assertEquals(13.5, result.cost());
    }

}