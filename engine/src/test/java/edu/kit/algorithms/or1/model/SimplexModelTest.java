package edu.kit.algorithms.or1.model;

import edu.kit.algorithms.utils.Tupel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimplexModelTest {

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
    void isStandardForm() {
        assertTrue(simplexModel.isStandardForm());
    }

    @Test
    void toTablou() {
        SimplexTableau simplex = new SimplexTableau(
                new double[][]{
                        {1, 1},
                        {5, 2},
                        {0, 1}
                },
                new String[]{"x3", "x4", "x5"},
                new String[]{"x1", "x2"},
                new double[]{10.0, 30.0, 9.0},
                new double[]{-30.0, -25.0},
                0
        );

        SimplexTableau simplexTableau = simplexModel.toTablou();
        assertEquals(simplex, simplexTableau);
    }
}