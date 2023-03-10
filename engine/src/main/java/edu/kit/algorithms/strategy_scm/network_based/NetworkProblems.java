package edu.kit.algorithms.strategy_scm.network_based;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.Arrays;

public class NetworkProblems {

    private final RealMatrix adjacencyMatrix;

    private final double[] nodeWeights;


    public NetworkProblems(double[][] adjacencyMatrix, double[] nodeWeights) {
        this.adjacencyMatrix = MatrixUtils.createRealMatrix(adjacencyMatrix);
        this.nodeWeights = nodeWeights;
    }


    public double getShortestDistance(int startEdge, int endEdge, double howFar, int goal) {
        double costOfEdge = adjacencyMatrix.getEntry(startEdge, endEdge);
        return Math.min(costOfEdge * howFar + adjacencyMatrix.getEntry(startEdge, goal),
                costOfEdge * (1 - howFar) +
                adjacencyMatrix.getEntry(endEdge, goal));
    }

    public int getWeightedMedian() {
        var nodeWeightMatrix = MatrixUtils.createRealDiagonalMatrix(nodeWeights);
        var weightedAdjacencyMatrix = nodeWeightMatrix.multiply(adjacencyMatrix);
        int smallestIndex = 0;
        double smallestSum = Double.MAX_VALUE;
        for (int i = 0; i < weightedAdjacencyMatrix.getColumnDimension(); i++) {
            double sum = Arrays.stream(weightedAdjacencyMatrix.getColumn(i)).sum();
            if (sum < smallestSum) {
                smallestSum = sum;
                smallestIndex = i;
            }
        }
        return smallestIndex;
    }

    public int getWeightedCenter() {
        var nodeWeightMatrix = MatrixUtils.createRealDiagonalMatrix(nodeWeights);
        var weightedAdjacencyMatrix = nodeWeightMatrix.multiply(adjacencyMatrix);
        int smallestIndex = 0;
        double smallestEntry = Double.MAX_VALUE;
        for (int i = 0; i < weightedAdjacencyMatrix.getColumnDimension(); i++) {
            var min = Arrays.stream(weightedAdjacencyMatrix.getColumn(i)).max().orElse(Double.MAX_VALUE);
            if (min < smallestEntry) {
                smallestEntry = min;
                smallestIndex = i;
            }
        }
        return smallestIndex;
    }

}
