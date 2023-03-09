package edu.kit.algorithms.one_center_problems;

import edu.kit.algorithms.metric.L1Metric;
import edu.kit.algorithms.metric.L2Metric;
import edu.kit.algorithms.metric.Metric;
import edu.kit.algorithms.utils.Point;
import edu.kit.algorithms.utils.PointWithWeight;
import edu.kit.algorithms.utils.Tupel;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


public class CenterProblemSolver {

    private final List<Point> points;
    private final List<PointWithWeight> pointsWithWeight;
    private final RealMatrix transformationMatrix = MatrixUtils.createRealMatrix(
            new double[][]{
                    {1, -1},
                    {1, 1}
            }
    );
    private final L1Metric l1;
    private final L2Metric l2;


    public CenterProblemSolver(List<PointWithWeight> points) {
        this.pointsWithWeight = points;
        this.points = points.stream().map(PointWithWeight::p).toList();
        this.l1 = L1Metric.getInstance();
        this.l2= L2Metric.getInstance();
    }

    public ResultOfCenterProblem solveOneCenterUnweightedWithL2(double speedKmPerHour) {
        //Calculate MUEK for 2 points
        Map<List<Point>, Tupel<List<Point>, Double>> isInCircle = new HashMap<>();
        boolean optimalFoundWith2Points = false;
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                Point p1 = points.get(i);
                Point p2 = points.get(j);
                Point midPoint = new Point((p1.x() + p2.x()) / 2, (p1.y() + p2.y()) / 2);
                double radius = l2.calculate(p1, midPoint);
                List<Point> pointsInCircle = points.stream()
                        .filter(point -> l2.calculate(point, midPoint) <= radius)
                        .toList();
                if (pointsInCircle.size() == points.size()) {
                    optimalFoundWith2Points = true;
                }
                isInCircle.put(List.of(p1, p2), new Tupel<>(pointsInCircle, radius));
            }
        }
        if (optimalFoundWith2Points) {
            List<List<Point>> optimalPoints = isInCircle.entrySet().stream()
                    .filter(entry -> entry.getValue().first().size() == points.size())
                    .map(Map.Entry::getKey)
                    .toList();
            return new ResultOfCenterProblem(optimalPoints.get(0), isInCircle.get(optimalPoints.get(0)).second() / speedKmPerHour);
        }
        //Calculate MUEK for 3 points
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                for (int k = j + 1; k < points.size(); k++) {
                    Point p1 = points.get(i);
                    Point p2 = points.get(j);
                    Point p3 = points.get(k);
                    boolean isAlreadyInMuek = isInCircle.entrySet().stream().anyMatch(
                            entry -> new HashSet<>(entry.getValue().first()).containsAll(
                                    List.of(p1, p2, p3)
                            )
                    );
                    if (isAlreadyInMuek) {
                        continue;
                    }
                    Point sp = calculateSP(p1, p2, p3);
                    double maxDistance = calculateMaxDistance(sp, points, l2) / speedKmPerHour;
                    return new ResultOfCenterProblem(List.of(sp), maxDistance);
                }
            }
        }
        return null;
    }
    private Tupel<Double, Double> getMidLineEquation(Point p1, Point p2) {
        double ai1 = p1.x();
        double ai2 = p1.y();
        double aj1 = p2.x();
        double aj2 = p2.y();
        double m = (ai1 - aj1) / (aj2 - ai2);
        double b = (Math.pow(aj1, 2) + Math.pow(aj2, 2) - Math.pow(ai1, 2) - Math.pow(ai2, 2)) / (2 * (aj2 - ai2));
        return new Tupel<>(m, b);
    }

    private Point calculateSP(Point i, Point j, Point k) {
        var ij = getMidLineEquation(i, j);
        var ik = getMidLineEquation(i, k);

        double x = (ik.second() - ij.second()) / (ij.first() - ik.first());
        double y = (ij.first() * ik.second() - ij.second() * ik.first()) / (ij.first() - ik.first());
        return new Point(x, y);
    }


    public ResultOfCenterProblem solveOneCenterUnweightedWithL1(double speed) {
        List<Point> transformedPoints = transformPoints(points);
        var bottomLeft = new Point(
                transformedPoints.stream().mapToDouble(Point::x).min().orElseThrow(),
                transformedPoints.stream().mapToDouble(Point::y).min().orElseThrow()
        );
        var topRight = new Point(
                transformedPoints.stream().mapToDouble(Point::x).max().orElseThrow(),
                transformedPoints.stream().mapToDouble(Point::y).max().orElseThrow()
        );
        double deltaX = topRight.x() - bottomLeft.x();
        double deltaY = topRight.y() - bottomLeft.y();


        if (deltaY == deltaX) {
            var transformedOptimalPoint = transformBack(
                    new Point(
                            (bottomLeft.x() + topRight.x()) / 2,
                            (bottomLeft.y() + topRight.y()) / 2
                    )
            );

            return new ResultOfCenterProblem(List.of(transformedOptimalPoint),
                    calculateMaxDistance(transformedOptimalPoint, points, l1) * speed);
        }
        double delta = Math.abs(deltaX - deltaY);
        Point newBottomLeft;
        Point newTopRight;

        Point firstMiddlePoint;
        Point secondMiddlePoint;
        List<Point> transformedMiddlePoints;

        if (deltaY > deltaX) {
            newBottomLeft = new Point(
                    bottomLeft.x() - delta,
                    bottomLeft.y()
            );
            newTopRight = new Point(
                    topRight.x() + delta,
                    topRight.y()
            );


        } else {
            newBottomLeft = new Point(
                    bottomLeft.x(),
                    bottomLeft.y() - delta
            );
            newTopRight = new Point(
                    topRight.x(),
                    topRight.y() + delta);

        }
        firstMiddlePoint = new Point(
                (newBottomLeft.x() + topRight.x()) / 2,
                (newBottomLeft.y() + topRight.y()) / 2
        );
        secondMiddlePoint = new Point(
                (bottomLeft.x() + newTopRight.x()) / 2,
                (bottomLeft.y() + newTopRight.y()) / 2
        );
        transformedMiddlePoints = List.of(
                transformBack(firstMiddlePoint),
                transformBack(secondMiddlePoint)
        );
        return new ResultOfCenterProblem(
                transformedMiddlePoints
                , calculateMaxDistance(transformedMiddlePoints.get(0), points, l1) * speed);
    }

    public ResultOfCenterProblem solveOneCenterWeightedWithL1(double speed) {
        List<PointWithWeight> transformedWeightedPoints = transformPointsWithWeight(pointsWithWeight);
        double maxXDelta = 0;
        double maxYDelta = 0;
        double optimalX = 0;
        double optimalY = 0;
        for (int i = 0; i < transformedWeightedPoints.size(); i++) {
            for (int j = i + 1; j < transformedWeightedPoints.size(); j++) {
                if (delta(transformedWeightedPoints.get(i), transformedWeightedPoints.get(j), true) >= maxXDelta) {
                    maxXDelta = delta(transformedWeightedPoints.get(i), transformedWeightedPoints.get(j), true);
                    optimalX = optimalPoint(transformedWeightedPoints.get(i), transformedWeightedPoints.get(j), true);
                }
                if (delta(transformedWeightedPoints.get(i), transformedWeightedPoints.get(j), false) >= maxYDelta) {
                    maxYDelta = delta(transformedWeightedPoints.get(i), transformedWeightedPoints.get(j), false);
                    optimalY = optimalPoint(transformedWeightedPoints.get(i), transformedWeightedPoints.get(j), false);
                }
            }
        }

        return new ResultOfCenterProblem(
                List.of(transformBack(new Point(optimalX, optimalY))),
                Math.max(maxXDelta, maxYDelta) * speed
        );

    }

    private double optimalPoint(PointWithWeight p1, PointWithWeight p2, boolean xDirection) {
        double divider = p1.weight() + p2.weight();
        if (xDirection) {
            return (p1.weight() * p1.p().x() + p2.weight() * p2.p().x()) / (divider);
        } else {
            return (p1.weight() * p1.p().y() + p2.weight() * p2.p().y()) / (divider);
        }
    }

    private double delta(PointWithWeight p1, PointWithWeight p2, boolean xDirection) {
        double weightFactor = p1.weight() * p2.weight() / (p1.weight() + p2.weight());
        if (xDirection) {
            return weightFactor * Math.abs(p1.p().x() - p2.p().x());
        } else {
            return weightFactor * Math.abs(p1.p().y() - p2.p().y());
        }
    }


    double calculateMaxDistance(Point point, List<Point> points, Metric metric) {
        return points.stream().mapToDouble(p -> metric.calculate(point, p)).max().orElse(-1);
    }

    Point transformBack(Point point) {
        //invert transformation matrix
        return new Point(
                MatrixUtils.inverse(transformationMatrix).preMultiply(new double[]{point.x(), point.y()})[0],
                MatrixUtils.inverse(transformationMatrix).preMultiply(new double[]{point.x(), point.y()})[1]
        );
    }

    List<PointWithWeight> transformPointsWithWeight(List<PointWithWeight> points) {
        return points.stream()
                .map(
                        point -> new PointWithWeight(
                                new Point(
                                        transformationMatrix.preMultiply(new double[]{point.p().x(), point.p().y()})[0],
                                        transformationMatrix.preMultiply(new double[]{point.p().x(), point.p().y()})[1]
                                        ),
                                point.weight()
                        )
                ).toList();
    }

    List<Point> transformPoints(List<Point> points) {
        return points.stream()
                .map(
                        point -> new Point(
                                transformationMatrix.preMultiply(new double[]{point.x(), point.y()})[0],
                                transformationMatrix.preMultiply(new double[]{point.x(), point.y()})[1]
                        )
                ).toList();
    }


    public record ResultOfCenterProblem(List<Point> optimalPoint, double cost) {
    }
}
