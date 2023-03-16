package edu.kit.algorithms.or1.model;

import java.util.Arrays;
import java.util.stream.IntStream;


public class TransportProblem {

    //C
    private final int[][] costs;

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

    public TransportProblem(int[][] costs, int[] supply, int[] demand) {
        this.costs = costs;
        this.supply = supply;
        this.demand = demand;
    }

    public int getValue(int[][] x) {

        return IntStream.range(0, x.length * x[0].length).parallel().map(id -> Arrays.stream(x).flatMapToInt(Arrays::stream).toArray()[id] * Arrays.stream(costs).flatMapToInt(Arrays::stream).toArray()[id]).reduce(0, Integer::sum);
    }
}
