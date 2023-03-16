package edu.kit.algorithms.or1.model;

import java.util.Arrays;
import java.util.stream.IntStream;


public class TransportProblem {

    //C
    private final double[][] costs;

    //a
    private final int[] supply;

    //b
    private final int[] demand;

    public int[] getDemand() {
        return demand;
    }

    public int[] getSupply() {
        return supply;
    }

    public TransportProblem(double[][] costs, int[] supply, int[] demand) {
        this.costs = costs;
        this.supply = supply;
        this.demand = demand;
    }

    public double getValue(int[][] x) {

        return IntStream.range(0, x.length * x[0].length).parallel().mapToDouble(id -> Arrays.stream(x).flatMapToInt(Arrays::stream).toArray()[id] * Arrays.stream(costs).flatMapToDouble(Arrays::stream).toArray()[id]).reduce(0, Double::sum);
    }
}
