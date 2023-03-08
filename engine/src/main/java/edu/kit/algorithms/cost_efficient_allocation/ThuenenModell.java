package edu.kit.algorithms.cost_efficient_allocation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ThuenenModell {


    public static List<ResultOfThuenenModell> solveThuenenModell(List<InputOfThuenenModell> equations) {
        Optional<InputOfThuenenModell> max = equations.stream().max(Comparator.comparingDouble(InputOfThuenenModell::p));
        if (max.isEmpty() || max.get().p() <= 0) {
            throw new IllegalArgumentException("p must be greater than 0");
        }
        if (intersectionWith0(max.get().p(), -1 * max.get().k()) < 0) {
            throw new IllegalArgumentException("p must be greater than 0");
        }
        List<InputOfThuenenModell> optimalProductions = new ArrayList<>();
        optimalProductions.add(max.get());
        boolean improvementMade;
        double lastIntervall = 0;
        while (true) {
            List<InputOfThuenenModell> possibleImprovement = equations.stream().filter(equation -> !optimalProductions.contains(equation)).toList();
            improvementMade = false;
            InputOfThuenenModell newMax = null;
            double newMaxIntersection = 0;
            for (InputOfThuenenModell equation : possibleImprovement) {
                double intersection = intersectionOf2Lines(optimalProductions.get(optimalProductions.size() - 1).p(),
                        -1 * optimalProductions.get(optimalProductions.size() - 1).k(), equation.p(),
                        -1 * equation.k());
                if (getCorrespondingOutput(equation.p(), equation.k(), intersection) > 0
                        && ((newMax == null || intersection < newMaxIntersection) && (intersection > lastIntervall))) {

                    newMax = equation;
                    newMaxIntersection = intersection;
                    improvementMade = true;

                }
            }
            if (!improvementMade) {
                break;
            }
            optimalProductions.add(newMax);
            lastIntervall = newMaxIntersection;
        }
        return generateResult(optimalProductions);

    }


    private static List<ResultOfThuenenModell> generateResult(List<InputOfThuenenModell> input) {
        List<ResultOfThuenenModell> result = new ArrayList<>();
        double lastIntervall = 0;
        for (int i = 0; i < input.size(); i++) {
            if (i == input.size() - 1) {
                result.add(new ResultOfThuenenModell(input.get(i).nameOfActivity(), lastIntervall, intersectionWith0(input.get(i).p(), -1 * input.get(i).k())));
            } else {
                var newLastIntervall = intersectionOf2Lines(input.get(i).p(), -1 * input.get(i).k(), input.get(i + 1).p(), -1 * input.get(i + 1).k());
                result.add(new ResultOfThuenenModell(input.get(i).nameOfActivity(), lastIntervall, newLastIntervall));
                lastIntervall = newLastIntervall;
            }
        }
        return result;
    }


    private static double getCorrespondingOutput(double p, double k, double x) {
        return p - k * x;
    }

    private static double intersectionOf2Lines(double p1, double k1, double p2, double k2) {
        return (p1 - p2) / (k2 - k1);
    }

    private static double intersectionWith0(double p, double k) {
        return -p / k;
    }


    public record InputOfThuenenModell(String nameOfActivity,
                                       double p,
                                       double k) {

    }

    public record ResultOfThuenenModell(String nameOfActivity,
                                        double start,
                                        double end) {

    }
}
