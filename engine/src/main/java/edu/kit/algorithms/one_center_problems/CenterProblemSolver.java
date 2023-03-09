package edu.kit.algorithms.one_center_problems;

import edu.kit.algorithms.utils.Point;
import edu.kit.algorithms.utils.PointWithWeight;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.List;

import static edu.kit.algorithms.metric.Metric.l1Metric;


public class CenterProblemSolver {

    private final List<Point> points;
    private final List<PointWithWeight> pointsWithWeight;
    private final RealMatrix transformationMatrix = MatrixUtils.createRealMatrix(
            new double[][]{
                    {1, -1},
                    {1, 1}
            }
    );


    public CenterProblemSolver(List<PointWithWeight> points) {
        this.pointsWithWeight = points;
        this.points = points.stream().map(PointWithWeight::p).toList();
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
            return new ResultOfCenterProblem(List.of(
                    new Point(
                            (bottomLeft.x() + topRight.x()) / 2,
                            (bottomLeft.y() + topRight.y()) / 2
                    )
            ), 0);
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
                , calculateMaxDistance(transformedMiddlePoints.get(0), points) * speed);
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


    double calculateMaxDistance(Point point, List<Point> points) {
        return points.stream().mapToDouble(p -> l1Metric(point, p)).max().orElse(-1);
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
