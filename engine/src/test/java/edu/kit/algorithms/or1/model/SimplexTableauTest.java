package edu.kit.algorithms.or1.model;

import edu.kit.algorithms.utils.Tupel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimplexTableauTest {

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
    void makeToBigSimplexTable() {
        double [][] expected = {
                {-30.0, -25, 0},
                {1, 1, 10},
                {5, 2, 30},
                {0, 1, 9}
        };
        double[][] result = simplexModel.toTablou().makeToBigSimplexTable();
        assertArrayEquals(expected, result);
    }

    @Test
    void testConversion() {
        SimplexTableau expected = simplexModel.toTablou();
        double[][] result = expected.makeToBigSimplexTable();
        SimplexTableau tableau = SimplexTableau.fromBigSimplexTable(result,
                expected.baseVariables(),
                expected.nonBaseVariables()
                );
        assertEquals(expected, tableau);
    }
}