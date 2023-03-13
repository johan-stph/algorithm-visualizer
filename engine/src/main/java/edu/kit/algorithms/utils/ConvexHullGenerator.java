package edu.kit.algorithms.utils;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class ConvexHullGenerator {

    private final List<PointWithName> points;
    private List<PointWithName> convexHull;

    public List<PointWithName> generateConvexHull() {
        if (convexHull != null) {
            return convexHull;
        }
        List<PointWithName> output = new ArrayList<>();
        PointWithName leftMostPoint = this.points.stream().min((p1, p2) -> {
                    if (p1.point().x() == p2.point().x()) {
                        return Double.compare(p1.point().y(), p2.point().y());
                    }
                    return Double.compare(p1.point().x(), p2.point().x());
                })
                .orElseThrow(
                        () -> new IllegalStateException("No left most point found")
                );
        output.add(leftMostPoint);


        while (true) {
            List<PointWithName> vectors = points.stream()
                    .filter(p -> !p.equals(output.get(output.size() - 1)))
                    .map(p -> new PointWithName(p.name(),
                            new Point(
                                    p.point().x() - output.get(output.size() - 1).point().x(), p.point().y() - output.get(output.size() - 1).point().y())
                    ))
                    .toList();
            PointWithName randomVector = vectors.get(0);
            //find the point with the smallest angle
            Map<PointWithName, Double> pointWithAngle = vectors.stream()
                    .collect(Collectors.toMap(p -> p, p -> calculateAngle(randomVector.point(), p.point())));

            PointWithName nextPoint = pointWithAngle.entrySet().stream()
                    .max(Comparator.comparingDouble(Map.Entry::getValue))
                    .orElseThrow(
                            () -> new IllegalStateException("No next point found")
                    ).getKey();

            if (nextPoint.name().equals(leftMostPoint.name())) {
                break;
            }
            output.add(
                    this.points.stream().filter(
                                    p -> p.name().equals(nextPoint.name()
                                    )).findFirst().
                            orElseThrow(
                                    () -> new IllegalStateException("No next point found")
                            )
            );
        }
        convexHull = output;
        return output;
    }

    double calculateAngle(Point vector1, Point vector2) {
        double x1 = vector1.x();
        double y1 = vector1.y();
        double x2 = vector2.x();
        double y2 = vector2.y();
        return Math.atan2(x1*y2-y1*x2,x1*x2+y1*y2) * 180 / Math.PI;
    }

    public List<Point> getIntersectionXDirectionPoints(double xCoordinate) {
        List<Point> intersectionPoints = new ArrayList<>();
        for (int i = 0; i < convexHull.size(); i++) {
            PointWithName currentPoint = convexHull.get(i);
            PointWithName nextPoint = convexHull.get((i + 1) % convexHull.size());
            if (currentPoint.point().x() <= xCoordinate && nextPoint.point().x() >= xCoordinate
                    || currentPoint.point().x() >= xCoordinate && nextPoint.point().x() <= xCoordinate) {
                //get intersection point
                double m = (nextPoint.point().y() - currentPoint.point().y()) / (nextPoint.point().x() - currentPoint.point().x());
                double b = currentPoint.point().y() - m * currentPoint.point().x();
                intersectionPoints.add(new Point(xCoordinate, m * xCoordinate + b));
            }
        }
        return intersectionPoints;
    }

    public List<Point> getIntersectionYDirectionPoints(double yCoordinate) {
        List<Point> intersectionPoints = new ArrayList<>();
        for (int i = 0; i < convexHull.size(); i++) {
            PointWithName currentPoint = convexHull.get(i);
            PointWithName nextPoint = convexHull.get((i + 1) % convexHull.size());
            if (currentPoint.point().y() <= yCoordinate && nextPoint.point().y() >= yCoordinate
                    || currentPoint.point().y() >= yCoordinate && nextPoint.point().y() <= yCoordinate) {
                //get intersection point
                if (nextPoint.point().x() == currentPoint.point().x()) {
                    intersectionPoints.add(new Point(currentPoint.point().x(), yCoordinate));
                    continue;
                }
                double m = (nextPoint.point().y() - currentPoint.point().y()) / (nextPoint.point().x() - currentPoint.point().x());
                double b = currentPoint.point().y() - m * currentPoint.point().x();
                intersectionPoints.add(new Point((yCoordinate - b) / m, yCoordinate));
            }
        }
        return intersectionPoints;
    }

}

