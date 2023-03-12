package edu.kit.algorithms.or1.model;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;

public record SimplexTableau(
        double[][] simplexTable,
        String[] baseVariables,
        String[] nonBaseVariables,
        double[] rightSide,
        double[] goalCoefficients,
        double goalFunctionValue,

        Object notes



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

    public boolean isSolved() {
        return Arrays.stream(goalCoefficients).allMatch(x -> x >= 0);
    }

    public double[][] makeToBigSimplexTable() {
        double[][] newSimplexTable = new double[simplexTable.length + 1][simplexTable[0].length + 1];
        for (int i = 0; i < simplexTable.length; i++) {
            System.arraycopy(simplexTable[i], 0, newSimplexTable[i + 1], 0, simplexTable[0].length);
        }
        for (int i = 0; i < simplexTable.length; i++) {
            newSimplexTable[i + 1][simplexTable[0].length] = rightSide[i];
        }
        System.arraycopy(goalCoefficients, 0, newSimplexTable[0], 0, simplexTable[0].length);
        newSimplexTable[0][simplexTable[0].length] = goalFunctionValue;
        return newSimplexTable;
    }

    public static SimplexTableau fromBigSimplexTable(double[][] simplexTable, String[] baseVariable, String[] nonBaseVariable) {
        double[] rightSide = new double[simplexTable.length - 1];
        double[] goalCoefficients = new double[simplexTable[0].length - 1];
        double goalFunctionValue = simplexTable[0][simplexTable[0].length - 1];
        double[][] newSimplexTable = new double[simplexTable.length - 1][simplexTable[0].length - 1];
        for (int i = 0; i < simplexTable.length - 1; i++) {
            System.arraycopy(simplexTable[i + 1], 0, newSimplexTable[i], 0, simplexTable[0].length - 1);
        }
        for (int i = 0; i < simplexTable.length - 1; i++) {
            rightSide[i] = simplexTable[i + 1][simplexTable[0].length - 1];
        }
        System.arraycopy(simplexTable[0], 0, goalCoefficients, 0, simplexTable[0].length - 1);
        return new SimplexTableau(newSimplexTable, baseVariable, nonBaseVariable, rightSide, goalCoefficients, goalFunctionValue, null);
    }

}
