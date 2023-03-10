package edu.kit.algorithms.or1.model;

import java.util.Arrays;
import java.util.Objects;

public record SimplexTableau(
        double[][] simplexTable,
        String[] baseVariables,
        String[] nonBaseVariables,
        double[] rightSide,
        double[] goalCoefficients,
        double goalFunctionValue



) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimplexTableau that = (SimplexTableau) o;
        return Double.compare(that.goalFunctionValue, goalFunctionValue) == 0 && Arrays.deepEquals(simplexTable, that.simplexTable) && Arrays.equals(baseVariables, that.baseVariables) && Arrays.equals(nonBaseVariables, that.nonBaseVariables) && Arrays.equals(rightSide, that.rightSide) && Arrays.equals(goalCoefficients, that.goalCoefficients);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(goalFunctionValue);
        result = 31 * result + Arrays.deepHashCode(simplexTable);
        result = 31 * result + Arrays.hashCode(baseVariables);
        result = 31 * result + Arrays.hashCode(nonBaseVariables);
        result = 31 * result + Arrays.hashCode(rightSide);
        result = 31 * result + Arrays.hashCode(goalCoefficients);
        return result;
    }

    @Override
    public String toString() {
        return "SimplexTableau{" +
                "simplexTable=" +
                Arrays.deepToString(simplexTable) +
                ", baseVariables=" +
                Arrays.toString(baseVariables) +
                ", nonBaseVariables=" +
                Arrays.toString(nonBaseVariables) +
                ", rightSide=" +
                Arrays.toString(rightSide) +
                ", goalCoefficients=" +
                Arrays.toString(goalCoefficients) +
                ", goalFunctionValue=" +
                goalFunctionValue +
                '}';
    }
}
