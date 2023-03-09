package edu.kit.algorithms.one_median_problems;

import edu.kit.algorithms.metric.L1Metric;
import edu.kit.algorithms.utils.Point;
import edu.kit.algorithms.utils.PointWithWeight;
import lombok.RequiredArgsConstructor;

import java.util.*;



@RequiredArgsConstructor
public class MedianProblemSolver {

    private final List<PointWithWeight> points;


    public ResultOfMedianProblem solveOneMedianWithL1(double speed) {
        double totalWeight = points.stream().reduce(0.0, (a, b) -> a + b.weight(), Double::sum);
        Optional<PointWithWeight> dominantPoint = points.stream().filter(p -> p.weight() > totalWeight / 2).findFirst();
        if (dominantPoint.isPresent()) {
            return new ResultOfMedianProblem(Collections.singletonList(dominantPoint.get().p()), 0);
        }
        // Find optimal x coordinate
        List<PointWithWeight> sortedX = points.stream().sorted(Comparator.comparingDouble(a -> a.p().x())).toList();
        double counter = 0;
        List<Double> optimalX = new ArrayList<>();
        for (int i = 0; counter < totalWeight / 2; i++) {
            counter += sortedX.get(i).weight();
            if (counter > totalWeight / 2) {
                optimalX = Collections.singletonList(sortedX.get(i).p().x());
            }
            if (counter == totalWeight / 2) {
                optimalX = List.of(sortedX.get(i).p().x(), sortedX.get(i + 1).p().x());
            }
        }
        // Find optimal y coordinate
        List<PointWithWeight> sortedY = points.stream().sorted(Comparator.comparingDouble(a -> a.p().y())).toList();
        counter = 0;
        List<Double> optimalY = new ArrayList<>();
        for (int i = 0; counter < totalWeight / 2; i++) {
            counter += sortedY.get(i).weight();
            if (counter > totalWeight / 2) {
                optimalY = Collections.singletonList(sortedY.get(i).p().y());
            }
            if (counter == totalWeight / 2) {
                optimalY = List.of(sortedY.get(i).p().y(), sortedY.get(i + 1).p().y());
            }
        }
        var listOfOptimalPoints = new ArrayList<Point>();
        for (double x : optimalX) {
            for (double y : optimalY) {
                listOfOptimalPoints.add(new Point(x, y));
            }
        }
        double cost = points.stream().mapToDouble(pointWithWeight -> L1Metric.getInstance().calculate(listOfOptimalPoints.get(0), pointWithWeight.p())* pointWithWeight.weight()).sum();
        return new ResultOfMedianProblem(listOfOptimalPoints, speed * cost);
    }


    public record ResultOfMedianProblem(List<Point> optimalPoint, double cost) {
    }
}
