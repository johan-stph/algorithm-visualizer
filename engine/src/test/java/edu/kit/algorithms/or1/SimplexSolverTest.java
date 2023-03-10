package edu.kit.algorithms.or1;

import edu.kit.algorithms.or1.model.EquationUtils;
import edu.kit.algorithms.or1.model.MaxOrMin;
import edu.kit.algorithms.or1.model.SimplexModel;
import edu.kit.algorithms.utils.RoundingUtils;
import edu.kit.algorithms.utils.Tupel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimplexSolverTest {

    private SimplexModel simplexModel;
    @BeforeEach
    void setUp() {
        String[] variables = {"x1", "x2"};
        double[] goalCoefficient = {30, 25};
        double[][] constraints = {
                {1, 1},
                {5, 2},
                {0, 1}
        };
        List<Tupel<EquationUtils, Double>> constraintsList = List.of(
                new Tupel<>(EquationUtils.LESS_THAN_OR_EQUAL_TO, 10.0),
                new Tupel<>(EquationUtils.LESS_THAN_OR_EQUAL_TO, 30.0),
                new Tupel<>(EquationUtils.LESS_THAN_OR_EQUAL_TO, 9.0)
        );

        simplexModel = new SimplexModel(
                variables,
                goalCoefficient,
                MaxOrMin.MAX,
                constraints,
                constraintsList
        );

    }

    @Test
    void solve() {
        SimplexSolver simplexSolver = new SimplexSolver();
        var result = simplexSolver.solveTableau(simplexModel.toTablou());
        assertEquals(round(800/3.0),
                round(result.get(result.size() - 1).goalFunctionValue()));
    }


    private double round(double value) {
        return RoundingUtils.round(value, 3);
    }
}