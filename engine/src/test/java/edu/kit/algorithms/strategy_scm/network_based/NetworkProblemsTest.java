package edu.kit.algorithms.strategy_scm.network_based;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NetworkProblemsTest {

    private double[][] adjacencyMatrix;
    private double[] nodeWeights;

    @BeforeEach
    void setUp() {
        this.adjacencyMatrix = new double[][]{
                {0, 70, 240, 120, 85},
                {70, 0, 274, 83, 15},
                {240, 274, 0, 191, 281},
                {120, 83, 191, 0, 90},
                {85, 15, 281, 90, 0}
        };
        this.nodeWeights = new double[]{100, 200, 300, 150, 120};
    }

    @Test
    void getShortestDistance() {
        var network = new NetworkProblems(adjacencyMatrix, nodeWeights);
        assertEquals(291.5, network.getShortestDistance(0, 1, 0.75, 2));
    }

    @Test
    void getWeightedMedian() {
        var network = new NetworkProblems(adjacencyMatrix, nodeWeights);
        assertEquals(3, network.getWeightedMedian());
    }

    @Test
    void getWeightedCenter() {
        var network = new NetworkProblems(adjacencyMatrix, nodeWeights);
        assertEquals(2, network.getWeightedCenter());
    }
}