package edu.kit.algorithms.or1;

import edu.kit.algorithms.or1.model.SimplexTableau;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class SimplexSolver {


    public List<SimplexTableau> solveTableau(SimplexTableau simplexTableau) {
        // check if tableau is already solved
        if (simplexTableau.isSolved()) {
            return Collections.singletonList(simplexTableau);
        }
        List<SimplexTableau> output = new ArrayList<>();
        output.add(simplexTableau);
        SimplexTableau currentTableau = simplexTableau;

        while (!currentTableau.isSolved()) {
            currentTableau = performSimplexStep(currentTableau);
            output.add(currentTableau);
        }
        return output;
    }

    private SimplexTableau performSimplexStep(SimplexTableau simplexTableau) {
        PivotWrapper pivotElement = getPivotElement(simplexTableau);

        // new baseVariables
        String[] newBaseVariables = simplexTableau.baseVariables().clone();
        String[] newNonBaseVariables = simplexTableau.nonBaseVariables().clone();
        newBaseVariables[pivotElement.pivotRow()] = simplexTableau.nonBaseVariables()[pivotElement.pivotColumn()];
        newNonBaseVariables[pivotElement.pivotColumn()] = simplexTableau.baseVariables()[pivotElement.pivotRow()];

        return SimplexTableau.fromBigSimplexTable(
                performTriangleRule(pivotElement, simplexTableau),
                newBaseVariables,
                newNonBaseVariables
        );
    }

    private PivotWrapper getPivotElement(SimplexTableau currentTableau) {
        int getPivotColumn = IntStream.range(0, currentTableau.goalCoefficients().length)
                .reduce((i, j) -> currentTableau.goalCoefficients()[i] < currentTableau.goalCoefficients()[j] ? i : j)
                .orElseThrow(
                        () -> new IllegalStateException("No most negative row index found")
                );
        double[] pivotRowHelper = IntStream.range(0, currentTableau.rightSide().length)
                .mapToDouble(i -> currentTableau.rightSide()[i] /currentTableau.simplexTable()[i][getPivotColumn])
                .toArray();
        int getPivotRowIndex = 0;
        double smallestPivotHelper = -1;
        for (int i = 0; i < pivotRowHelper.length; i++) {
            if (pivotRowHelper[i] > 0 && (pivotRowHelper[i] < smallestPivotHelper || smallestPivotHelper == -1)) {
                smallestPivotHelper = pivotRowHelper[i];
                getPivotRowIndex = i;
            }
        }

        double pivotElement = currentTableau.simplexTable()[getPivotRowIndex][getPivotColumn];
        return new PivotWrapper(getPivotColumn, getPivotRowIndex, pivotElement);
    }

    private double[][] performTriangleRule(PivotWrapper pivotElement, SimplexTableau simplexTableau) {
        //update goal
        double[][] bigSimplexTable = simplexTableau.makeToBigSimplexTable();
        double[][] newBigSimplexTable = new double[bigSimplexTable.length][bigSimplexTable[0].length];
        int newPivotRowIndex = pivotElement.pivotRow() +1;
        int newPivotColumnIndex = pivotElement.pivotColumn();
        for (int i = 0; i < bigSimplexTable.length; i++) {
            for (int j = 0; j < bigSimplexTable[0].length; j++) {
                if (i == newPivotRowIndex && j == newPivotColumnIndex) {
                    newBigSimplexTable[i][j] = 1 / pivotElement.pivotElement();
                } else if (i == newPivotRowIndex) {
                    newBigSimplexTable[i][j] = bigSimplexTable[i][j] / pivotElement.pivotElement();
                } else if (j == newPivotColumnIndex) {
                    newBigSimplexTable[i][j] = -bigSimplexTable[i][j] / pivotElement.pivotElement();
                } else {
                    newBigSimplexTable[i][j] = bigSimplexTable[i][j] -
                            (bigSimplexTable[i][newPivotColumnIndex] * bigSimplexTable[newPivotRowIndex][j])
                                    / pivotElement.pivotElement();
                }
            }
        }

        return newBigSimplexTable;
    }

}




record PivotWrapper(
        int pivotColumn,
        int pivotRow,
        double pivotElement
) {
}


