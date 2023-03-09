package edu.kit.algorithms.metric;

import edu.kit.algorithms.utils.Point;

public final class Metric {


    private Metric() {
        throw new IllegalStateException("Utility class");
    }

    public static double l1Metric(Point point1, Point point2) {
        return Math.abs(point1.x() - point2.x()) + Math.abs(point1.y() - point2.y());
    }

    public static double l2Metric(Point point1, Point point2) {
        return Math.sqrt(Math.pow(point1.x() - point2.x(), 2) + Math.pow(point1.y() - point2.y(), 2));
    }


}
