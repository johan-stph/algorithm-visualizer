package edu.kit.algorithms.strategy_scm.metric;

import edu.kit.algorithms.utils.Point;

public class L2Metric implements Metric{
    private static L2Metric instance;

    public static L2Metric getInstance() {
        if (instance == null) {
            instance = new L2Metric();
        }
        return instance;
    }

    private L2Metric() {
    }


    @Override
    public double calculate(Point point1, Point point2) {
return Math.sqrt(Math.pow(point1.x() - point2.x(), 2) + Math.pow(point1.y() - point2.y(), 2));
    }
}
