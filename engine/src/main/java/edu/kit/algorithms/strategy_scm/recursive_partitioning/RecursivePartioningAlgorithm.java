package edu.kit.algorithms.strategy_scm.recursive_partitioning;

import edu.kit.algorithms.strategy_scm.metric.L2Metric;
import edu.kit.algorithms.utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class RecursivePartioningAlgorithm {


    private final double betaFactor;

    private final L2Metric l2 = L2Metric.getInstance();


    public RecursivePartioningAlgorithm(
                                        double betaFactor
                                       ) {
        this.betaFactor = betaFactor;

    }

    public PartitionStep doPartition(List<PointWithWeightName> activityList, double ql, double qr) {
        if (activityList.size() == 1) {
            return new PartitionStep(activityList, new ArrayList<>());
        }
        double totalWeight = activityList.stream().mapToDouble(p -> p.pointWithWeight().weight()).sum();
        //x split
        var blAndBr = getBLandBrRight(activityList, true);
        double xSplitBalance = calculateMaxBalance(blAndBr.first(), blAndBr.second(),
                ql,
                qr,
                totalWeight / (ql + qr));
        double xSplitCp = calculateCp(
                blAndBr.first(),
                blAndBr.second(),
                true
        );
        //y split
        var blAndBrY = getBLandBrRight(activityList, false);
        double ySplitBalance = calculateMaxBalance(
                blAndBrY.first(),
                blAndBrY.second(),
                ql,
                qr,
                totalWeight / (ql + qr));
        double ySplitCp = calculateCp(
                blAndBrY.first(), blAndBrY.second(), false);
        Tupel<Double, Double> rk = calculateRK(xSplitBalance, ySplitBalance, xSplitCp, ySplitCp);
        if (rk.first() <= rk.second()) {
            return new PartitionStep(blAndBr.first(), blAndBr.second());
        } else {
            return new PartitionStep(blAndBrY.first(), blAndBrY.second());
        }
    }

    public Tupel<List<PointWithWeightName>, List<PointWithWeightName>> getBLandBrRight(List<PointWithWeightName> actvitylist, boolean xDirection) {
        double totalWeight = actvitylist.stream().mapToDouble(p -> p.pointWithWeight().weight()).sum();
        List<PointWithWeightName> bl = new ArrayList<>();
        List<PointWithWeightName> br = new ArrayList<>();
        List<PointWithWeightName> sortedActivities = actvitylist.stream().sorted((p1, p2) -> {
            if (xDirection) {
                return Double.compare(p1.pointWithWeight().p().x(), p2.pointWithWeight().p().x());
            } else {
                return -1 * Double.compare(p1.pointWithWeight().p().y(), p2.pointWithWeight().p().y());
            }
        }).toList();

        List<Tupel<Double, List<PointWithWeightName>>> grouped =
                sortedActivities.stream()
                        .collect(Collectors.groupingBy(p -> {
                            if (xDirection) {
                                return p.pointWithWeight().p().x();
                            } else {
                                return p.pointWithWeight().p().y();
                            }
                        }, Collectors.toList()))
                        .entrySet().stream()
                        .map(e -> new Tupel<>(e.getKey(), e.getValue()))
                        .sorted((t1, t2) -> {
                                    if (xDirection) {
                                        return Double.compare(t1.first(), t2.first());
                                    } else {
                                        return -1 * Double.compare(t1.first(), t2.first());
                                    }
                                }
                        )
                        .toList();

        double currentWeight = 0;
        for (int i = 0; i < grouped.size(); i++) {
            var current = grouped.get(i);
            double newWeight = current.second().stream().mapToDouble(p -> p.pointWithWeight().weight()).sum();
            if (currentWeight + newWeight <= totalWeight / 2) {
                bl.addAll(current.second());
                currentWeight += newWeight;
                continue;
            }
            if ((currentWeight + newWeight) - (totalWeight / 2) >= (totalWeight / 2) - currentWeight) {
                grouped.subList(i, grouped.size()).forEach(t -> br.addAll(t.second()));
                return new Tupel<>(bl, br);
            }
            bl.addAll(current.second());
            grouped.subList(i + 1, grouped.size()).forEach(t -> br.addAll(t.second()));
            return new Tupel<>(bl, br);
        }

        return new Tupel<>(bl, br);
    }


    public Tupel<Double, Double> calculateRK(double lp1Balance,
                                             double lp2Balance,
                                             double lp1Cp,
                                             double lp2Cp) {

        double lp1 = this.betaFactor * (lp1Balance / Math.max(lp1Balance, lp2Balance)) + (1 - this.betaFactor) *
                (lp1Cp / Math.max(lp1Cp, lp2Cp));
        double lp2 = this.betaFactor * (lp2Balance / Math.max(lp1Balance, lp2Balance)) + (1 - this.betaFactor) *
                (lp2Cp / Math.max(lp1Cp, lp2Cp));
        return new Tupel<>(lp1, lp2);
    }

    public double calculateMaxBalance(List<PointWithWeightName> bl, List<PointWithWeightName> br, double ql, double qr,
                                      double mu) {
        double blWeight = bl.stream().mapToDouble(p -> p.pointWithWeight().weight()).sum();
        double brWeight = br.stream().mapToDouble(p -> p.pointWithWeight().weight()).sum();
        return 1 / mu * Math.max(Math.abs((blWeight / ql) - mu), Math.abs((brWeight / qr) - mu));
    }

    public double calculateCp(List<PointWithWeightName> bl, List<PointWithWeightName> br, boolean xDirection) {
        var convexHull = new ConvexHullGenerator(
                Stream.concat(bl.stream(), br.stream())
                        .map(p -> new PointWithName(p.name(), p.pointWithWeight().p()))
                        .toList()
        );
        List<Point> intersections;

        if (xDirection) {
            double maxX = bl.stream().mapToDouble(p -> p.pointWithWeight().p().x()).max().orElse(0);
            intersections = convexHull.getIntersectionXDirectionPoints(maxX);
        }
        else {
            double minY = bl.stream().mapToDouble(p -> p.pointWithWeight().p().y()).min().orElse(0);
            intersections = convexHull.getIntersectionYDirectionPoints(minY);
        }
        return l2.calculate(intersections.get(0), intersections.get(1));
    }

}

record PartitionStep(
        List<PointWithWeightName> bl,
        List<PointWithWeightName> br

) {

}
