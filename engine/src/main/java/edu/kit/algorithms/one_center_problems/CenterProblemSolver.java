package edu.kit.algorithms.one_center_problems;

import edu.kit.algorithms.utils.Point;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.List;

import static edu.kit.algorithms.metric.Metric.l1Metric;


@RequiredArgsConstructor
public class CenterProblemSolver {


    private final List<Point> points;

    private final RealMatrix transformationMatrix = MatrixUtils.createRealMatrix(
            new double[][] {
                    {1, -1},
                    {1, 1}
            }
    );


    public ResultOfCenterProblem solveOneCenterWithL1(double speed) {
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
        return null;
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
