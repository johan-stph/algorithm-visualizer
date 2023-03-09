package edu.kit.algorithms.metric;

import edu.kit.algorithms.utils.Point;

public class L1Metric implements Metric{

    private static L1Metric instance;

    public static L1Metric getInstance() {
        if (instance == null) {
            instance = new L1Metric();
        }
        return instance;
    }
    private L1Metric() {
    }

    @Override
    public double calculate(Point point1, Point point2) {
        return Math.abs(point1.x() - point2.x()) + Math.abs(point1.y() - point2.y());
    }
}
