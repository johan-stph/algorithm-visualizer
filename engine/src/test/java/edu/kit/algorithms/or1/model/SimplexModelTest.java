package edu.kit.algorithms.or1.model;

import edu.kit.algorithms.utils.Tupel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimplexModelTest {

    private SimplexModel simplexModelOne;
    private SimplexModel simplexModelTwo;

    private SimplexModel simplexModelThree;


    @BeforeEach
    void setUp() {
        setUpOne();
        setUpTwo();
        setUpThree();
    }

    void setUpThree() {
        String[] variables = {"x1", "x2"};
        double[] goalCoefficient = {1, 1};
        double[][] constraints = {
                {1, 1, 1},
                {1, 1, 0},
        };
        List<Tupel<EquationUtils, Double>> constraintsList = List.of(
                new Tupel<>(EquationUtils.LESS_THAN_OR_EQUAL_TO, 1.0),
                new Tupel<>(EquationUtils.LESS_THAN_OR_EQUAL_TO, 1.0)
        );

        simplexModelThree = new SimplexModel(
                variables,
                goalCoefficient,
                MaxOrMin.MAX,
                constraints,
                constraintsList
        );
    }


    void setUpTwo() {
        String[] variables = {"x1", "x2", "x3"};
        double[] goalCoefficient = {1, 1, 1};
        double[][] constraints = {
                {1, 1, 1},
                {1, 1, 0},
                {1, 0, 1},
                {0, 1, 1}
        };
        List<Tupel<EquationUtils, Double>> constraintsList = List.of(
                new Tupel<>(EquationUtils.LESS_THAN_OR_EQUAL_TO, 1.0),
                new Tupel<>(EquationUtils.LESS_THAN_OR_EQUAL_TO, 1.0),
                new Tupel<>(EquationUtils.LESS_THAN_OR_EQUAL_TO, 1.0),
                new Tupel<>(EquationUtils.LESS_THAN_OR_EQUAL_TO, 1.0)
        );

        simplexModelTwo = new SimplexModel(
                variables,
                goalCoefficient,
                MaxOrMin.MAX,
                constraints,
                constraintsList
        );
    }



    void setUpOne() {
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

        simplexModelOne = new SimplexModel(
                variables,
                goalCoefficient,
                MaxOrMin.MAX,
                constraints,
                constraintsList
        );

    }

    @Test
    void isStandardForm() {
        assertTrue(simplexModelOne.isStandardForm());
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
                0,
                null
        );

        SimplexTableau simplexTableau = simplexModelOne.toTablou();
        assertEquals(simplex, simplexTableau);
    }

    @Test
    void toTablou2() {
        SimplexTableau simplex = new SimplexTableau(
                new double[][]{
                        {1, 1, 1},
                        {1, 1, 0},
                        {1, 0, 1},
                        {0, 1, 1}
                },
                new String[]{"x4", "x5", "x6", "x7"},
                new String[]{"x1", "x2", "x3"},
                new double[]{1.0, 1.0, 1.0, 1.0},
                new double[]{-1.0, -1.0, -1.0},
                0,
                null
        );

        SimplexTableau simplexTableau = simplexModelTwo.toTablou();
        assertEquals(simplex, simplexTableau);
    }

    @Test
    void toTablou3() {
        SimplexTableau simplex = new SimplexTableau(
                new double[][]{
                        {1, 1, 1},
                        {1, 1, 0},
                },
                new String[]{"x3", "x4"},
                new String[]{"x1", "x2"},
                new double[]{1.0, 1.0},
                new double[]{-1.0, -1.0},
                0,
                null
        );

        SimplexTableau simplexTableau = simplexModelThree.toTablou();
        assertEquals(simplex, simplexTableau);
    }

}