package edu.kit.algorithms.strategy_scm.recursive_partitioning;

import edu.kit.algorithms.strategy_scm.metric.L2Metric;
import edu.kit.algorithms.utils.*;
import lombok.RequiredArgsConstructor;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class RecursivePartioningAlgorithm {

    private final List<PointWithWeightName> activities;

    private final double betaFactor;
    private final int amountOfPartitions;
    private final L2Metric l2 = L2Metric.getInstance();


    public RecursivePartioningAlgorithm(List<PointWithWeightName> activities,
                                        double betaFactor,
                                        int amountOfPartitions) {
        this.activities = activities;
        this.betaFactor = betaFactor;
        this.amountOfPartitions = amountOfPartitions;

    }

    public PartitionStep doPartition(List<PointWithWeightName> activityList, double ql, double qr) {
        if (activityList.size() == 1) {
            return new PartitionStep(activityList, new ArrayList<>());
        }
        double totalWeight = activityList.stream().mapToDouble(p -> p.pointWithWeight().weight()).sum();
        //x split
        var blAndBr = getBLandBrRight(activityList, true);
        double xSplitBalance = calculateMaxBalance(blAndBr.first(),
                blAndBr.second(),
                ql,
                qr,
                totalWeight / (ql + qr));
        double xSplitCp = calculateMaxCp(
                blAndBr.first(),
                blAndBr.second()
        );
        //y split
        var blAndBrY = getBLandBrRight(activityList, false);
        double ySplitBalance = calculateMaxBalance(
                blAndBrY.first(),
                blAndBrY.second(),
                ql,
                qr,
                totalWeight / (ql + qr));
        double ySplitCp = calculateMaxCp(
                blAndBrY.first(),
                blAndBrY.second()
        );

        Tupel<Double, Double> rk = calculateRK(xSplitBalance, ySplitBalance, xSplitCp, ySplitCp);
        if (rk.first() > rk.second()) {
            return new PartitionStep(blAndBr.first(), blAndBr.second());
        } else {
            return new PartitionStep(blAndBrY.first(), blAndBrY.second());
        }
    }

    public Tupel<List<PointWithWeightName>, List<PointWithWeightName>> getBLandBrRight(List<PointWithWeightName> actvitylist,
                                                                                       boolean xDirection) {
        double totalWeight = actvitylist.stream().mapToDouble(p -> p.pointWithWeight().weight()).sum();
        double currentWeight = 0;
        List<PointWithWeightName> bl = new ArrayList<>();
        List<PointWithWeightName> br = new ArrayList<>();
        List<PointWithWeightName> sortedActivities = actvitylist.stream().sorted((p1, p2) -> {
            if (xDirection) {
                return Double.compare(p1.pointWithWeight().p().x(), p2.pointWithWeight().p().x());
            } else {
                return -1 * Double.compare(p1.pointWithWeight().p().y(), p2.pointWithWeight().p().y());
            }
        }).toList();

        for (int i = 0; i < sortedActivities.size(); i++) {
            if (currentWeight + sortedActivities.get(i).pointWithWeight().weight() > totalWeight / 2) {
                if (Math.abs(currentWeight - totalWeight / 2) < Math.abs(currentWeight + sortedActivities.get(i).pointWithWeight().weight() - totalWeight / 2)) {
                    br.addAll(sortedActivities.subList(i, sortedActivities.size()));
                } else {
                    bl.add(sortedActivities.get(i));
                    br.addAll(sortedActivities.subList(i + 1, sortedActivities.size()));
                }
                break;
            }
            currentWeight += sortedActivities.get(i).pointWithWeight().weight();
            bl.add(sortedActivities.get(i));
        }
        return new Tupel<>(bl, br);
    }




    public Tupel<Double, Double> calculateRK(double lp1Balance,
                                             double lp2Balance,
                                             double lp1Cp,
                                             double lp2Cp) {

        double lp1 =  this.betaFactor * (lp1Balance / Math.max(lp1Balance, lp2Balance)) + (1 - this.betaFactor) *
                (lp1Cp / Math.max(lp1Cp, lp2Cp));
        double lp2 =  this.betaFactor * (lp2Balance / Math.max(lp1Balance, lp2Balance)) + (1 - this.betaFactor) *
                (lp2Cp / Math.max(lp1Cp, lp2Cp));
        return new Tupel<>(lp1, lp2);
    }

    public double calculateMaxBalance(List<PointWithWeightName> bl, List<PointWithWeightName>  br, double ql, double qr,
                                      double mu) {
        double blWeight = bl.stream().mapToDouble(p -> p.pointWithWeight().weight()).sum();
        double brWeight =  br.stream().mapToDouble(p -> p.pointWithWeight().weight()).sum();
        return 1 / mu * Math.max(Math.abs((blWeight / ql) - mu), Math.abs((brWeight / qr) - mu));
    }

    public double calculateMaxCp(List<PointWithWeightName> bl, List<PointWithWeightName> br) {
        var convexHull = new ConvexHullGenerator(
                Stream.concat(bl.stream(), br.stream())
                        .map(p -> new PointWithName(p.name(), p.pointWithWeight().p()))
                        .toList()
        );
        double maxX = bl.stream().mapToDouble(p -> p.pointWithWeight().p().x()).max().orElseThrow();
        double minY = bl.stream().mapToDouble(p -> p.pointWithWeight().p().y()).min().orElseThrow();
        var xIntersection = convexHull.getIntersectionXDirectionPoints(maxX);
        var yIntersection = convexHull.getIntersectionYDirectionPoints(minY);

        return Math.max(
                l2.calculate(
                        xIntersection.get(0),
                        xIntersection.get(1)
                ), l2.calculate(
                        yIntersection.get(0),
                        yIntersection.get(1)
                ));

    }

}

record PartitionStep(
        List<PointWithWeightName> bl,
        List<PointWithWeightName> br

) {

}
