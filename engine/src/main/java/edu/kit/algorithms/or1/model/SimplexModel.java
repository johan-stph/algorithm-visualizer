package edu.kit.algorithms.or1.model;

import edu.kit.algorithms.utils.Tupel;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;


public class SimplexModel {

    private String[] variables;
    private double[] goalCoefficients;
    private MaxOrMin maxOrMin;
    private double[][] constraintCoefficients;
    private List<Tupel<EquationUtils, Double>> constraintEquations;

    public SimplexModel(String[] variables, double[] goalCoefficients, MaxOrMin maxOrMin, double[][] constraintCoefficients, List<Tupel<EquationUtils, Double>> constraintEquations) {
        this.variables = variables;
        this.goalCoefficients = goalCoefficients;
        this.maxOrMin = maxOrMin;
        this.constraintCoefficients = constraintCoefficients;
        this.constraintEquations = constraintEquations;
    }

    public boolean isStandardForm() {
        if (maxOrMin == MaxOrMin.MIN) {
            return false;
        }
        return constraintEquations.stream().allMatch(tupel -> tupel.first() == EquationUtils.LESS_THAN_OR_EQUAL_TO
        && tupel.second() >= 0);
    }

    public SimplexTableau toTablou() {
        if (isStandardForm()) {
            return new SimplexTableau(constraintCoefficients,
                    IntStream.range(0, constraintEquations.size()).mapToObj(i -> "x" + (i + constraintEquations.size())).toArray(String[]::new),
                    variables,
                    constraintEquations.stream().mapToDouble(Tupel::second).toArray(),
                    Arrays.stream(goalCoefficients).map(x -> -x).toArray(),
                    0);
        }
        return null;
    }

}
