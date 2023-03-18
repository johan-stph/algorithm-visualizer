package edu.kit.algorithms.strategy_scm.recursive_partitioning;

import edu.kit.algorithms.utils.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecursivePartioningAlgorithmTest {

    private RecursivePartioningAlgorithm recursivePartioningAlgorithm;
    private List<PointWithWeightName> activities;
    @BeforeEach
    void setUp() {
        this.activities = List.of(
                        new PointWithWeightName("1", new PointWithWeight(new Point(0, 3), 3)),
                        new PointWithWeightName("2", new PointWithWeight(new Point(0, 10), 3)),
                        new PointWithWeightName("3", new PointWithWeight(new Point(1, 7), 8)),
                        new PointWithWeightName("4", new PointWithWeight(new Point(2, 0), 2)),
                        new PointWithWeightName("5", new PointWithWeight(new Point(3, 5), 7)),
                        new PointWithWeightName("6", new PointWithWeight(new Point(4, 3), 4)),
                        new PointWithWeightName("7", new PointWithWeight(new Point(4, 8), 6)),
                        new PointWithWeightName("8", new PointWithWeight(new Point(5, 0), 6)),
                        new PointWithWeightName("9", new PointWithWeight(new Point(6, 2), 6)),
                        new PointWithWeightName("10", new PointWithWeight(new Point(6, 10), 5))
                );
        this.recursivePartioningAlgorithm = new RecursivePartioningAlgorithm(
                0.5
        );
    }

    @Test
    void doPartition() {
        PartitionStep result = this.recursivePartioningAlgorithm.doPartition(
                this.activities, 2, 2
        );
        assertEquals(4, result.bl().size());
        assertEquals(6, result.br().size());
    }

    @Test
    void getBLeftBRightXDirection() {
        Tupel<List<PointWithWeightName>, List<PointWithWeightName>> bLandBrRight = this.recursivePartioningAlgorithm
                .getBLandBrRight(this.activities, true);
        assertEquals(5, bLandBrRight.first().size());
        assertEquals(5, bLandBrRight.second().size());
        assertIterableEquals(List.of(
                new PointWithWeightName("1", new PointWithWeight(new Point(0, 3), 3)),
                new PointWithWeightName("2", new PointWithWeight(new Point(0, 10), 3)),
                new PointWithWeightName("3", new PointWithWeight(new Point(1, 7), 8)),
                new PointWithWeightName("4", new PointWithWeight(new Point(2, 0), 2)),
                new PointWithWeightName("5", new PointWithWeight(new Point(3, 5), 7))
        ), bLandBrRight.first());
        assertIterableEquals(List.of(
                new PointWithWeightName("6", new PointWithWeight(new Point(4, 3), 4)),
                new PointWithWeightName("7", new PointWithWeight(new Point(4, 8), 6)),
                new PointWithWeightName("8", new PointWithWeight(new Point(5, 0), 6)),
                new PointWithWeightName("9", new PointWithWeight(new Point(6, 2), 6)),
                new PointWithWeightName("10", new PointWithWeight(new Point(6, 10), 5))
        ), bLandBrRight.second());
    }

    @Test
    void getBLeftBRightDirectionY() {
        Tupel<List<PointWithWeightName>, List<PointWithWeightName>> bLandBrRight = this.recursivePartioningAlgorithm
                .getBLandBrRight(this.activities, false);
        assertEquals(4, bLandBrRight.first().size());
        assertEquals(6, bLandBrRight.second().size());
    }

    @Test
    void calculateRK() {
    }

    @Test
    void calculateMaxBalance() {
    }

    @Test
    void calculateMaxCp() {
    }
}