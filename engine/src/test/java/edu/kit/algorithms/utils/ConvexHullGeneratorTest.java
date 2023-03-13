package edu.kit.algorithms.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConvexHullGeneratorTest {

    private ConvexHullGenerator convexHullGenerator;

    @BeforeEach
    void setUp() {
        this.convexHullGenerator = new ConvexHullGenerator(List.of(
                new PointWithName("1", new Point(0, 3)),
                new PointWithName("2", new Point(0, 10)),
                new PointWithName("3", new Point(1, 7)),
                new PointWithName("4", new Point(2, 0)),
                new PointWithName("5", new Point(3, 5)),
                new PointWithName("6", new Point(4, 3)),
                new PointWithName("7", new Point(4, 8)),
                new PointWithName("8", new Point(5, 0)),
                new PointWithName("9", new Point(6, 2)),
                new PointWithName("10", new Point(6, 10))
        ));

    }

    @Test
    void getIntersectionXPoints() {
        List<Point> intersectionPoints = this.convexHullGenerator
                .getIntersectionXDirectionPoints(3);
        assertEquals(2, intersectionPoints.size());
        assertIterableEquals(List.of(new Point(3, 10), new Point(3, 0)),
                intersectionPoints);
    }

    @Test
    void getIntersectionYPoints() {
        List<Point> intersectionPoints = this.convexHullGenerator
                .getIntersectionYDirectionPoints(7);
        assertEquals(2, intersectionPoints.size());
        assertIterableEquals(List.of(new Point(0, 7), new Point(6, 7)),
                intersectionPoints);
    }

    @Test
    void generateConvexHull() {
        List<PointWithName> convexHull = this.convexHullGenerator.generateConvexHull();
        assertEquals(6, convexHull.size());
        assertIterableEquals(List.of("1", "2", "10", "9", "8", "4"),
                convexHull.stream().map(PointWithName::name).toList());
    }



    @Test
    void testAngle() {
        double result = this.convexHullGenerator.calculateAngle(new Point(1, 0), new Point(0, 1));
        assertEquals(90, result);
    }

    @Test
    void testAngle2() {
        double result = this.convexHullGenerator.calculateAngle(new Point(0, 1), new Point(1, 0));
        assertEquals(-90, result);
    }

    @Test
    void testAngle3() {
        double result = this.convexHullGenerator.calculateAngle(new Point(0, 1), new Point(0, 1));
        assertEquals(0, result);
    }
}