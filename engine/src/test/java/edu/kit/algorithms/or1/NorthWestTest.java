package edu.kit.algorithms.or1;

import edu.kit.algorithms.or1.model.TransportProblem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NorthWestTest {
    private TransportProblem transportProblem;
    private NorthWestSolver solver;

    @BeforeEach
    void setUp() {
        this.solver = new NorthWestSolver();
    }

    @Test
    void solve1() {
        transportProblem = createTransportProblem1();
        var result = solver.solveNorthWest(transportProblem);
        assertEquals(6, result[0][0]);
        assertEquals(4, result[0][1]);
        assertEquals(1, result[1][1]);
        assertEquals(7, result[1][2]);
        assertEquals(1, result[2][2]);
        assertEquals(6, result[2][3]);
        assertEquals(106, transportProblem.getValue(result));
    }

    @Test
    void solve2() {
        transportProblem = createTransportProblem2();
        var result = solver.solveNorthWest(transportProblem);
        assertEquals(3, result[0][0]);
        assertEquals(2, result[0][1]);
        assertEquals(1, result[1][1]);
        assertEquals(1, result[1][2]);
        assertEquals(1, result[2][2]);
        assertEquals(2, result[2][3]);
        assertEquals(48, transportProblem.getValue(result));
    }

    public TransportProblem createTransportProblem1() {
        double[][] costs = {{7, 2, 4, 7}, {9, 5, 3, 3}, {7, 7, 6, 4}};
        int[] supply = {10, 8, 7};
        int[] demand = {6, 5, 8, 6};

        return new TransportProblem(costs, supply, demand);
    }

    public TransportProblem createTransportProblem2() {
        double[][] costs = {{3, 7, 6, 4}, {2, 4, 3, 2}, {4, 3, 8, 5}};
        int[] supply = {5, 2, 3};
        int[] demand = {3, 3, 2, 2};

        return new TransportProblem(costs, supply, demand);
    }

}
