package edu.kit.algorithms.or1;

import edu.kit.algorithms.or1.model.PivotWrapper;
import edu.kit.algorithms.or1.model.SimplexTableau;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class SimplexSolver {


    public ResultSimplexTableaus solveTableau(SimplexTableau simplexTableau) {
        // check if tableau is already solved
        if (simplexTableau.isSolved()) {
            return new ResultSimplexTableaus(Collections.singletonList(simplexTableau), false);
        }
        List<SimplexTableau> output = new ArrayList<>();
        output.add(simplexTableau);
        SimplexTableau currentTableau = simplexTableau;

        while (!currentTableau.isSolved()) {
            if (isUnLimited(currentTableau)) {
                return new ResultSimplexTableaus(output, true);
            }
            currentTableau = performSimplexStepPaul(currentTableau);
            output.add(currentTableau);
        }
        return new ResultSimplexTableaus(output, false);
    }

    private SimplexTableau performSimplexStepPaul(SimplexTableau simplexTableau) {
        PivotWrapper pivotElement = getPivotElement(simplexTableau);

        // new baseVariables
        String[] newBaseVariables = simplexTableau.baseVariables().clone();
        String[] newNonBaseVariables = simplexTableau.nonBaseVariables().clone();
        newBaseVariables[pivotElement.pivotRow()] = simplexTableau.nonBaseVariables()[pivotElement.pivotColumn()];
        newNonBaseVariables[pivotElement.pivotColumn()] = simplexTableau.baseVariables()[pivotElement.pivotRow()];

        return SimplexTableau.fromBigSimplexTable(
                calculateNewBigTableau(pivotElement, simplexTableau),
                newBaseVariables,
                newNonBaseVariables
        );
    }

    private double[][] calculateNewBigTableau(PivotWrapper pivotElement, SimplexTableau simplexTableau) {
        double[][] oldBigSimplexTable = simplexTableau.makeToBigSimplexTable();
        double[][] newBigSimplexTable = new double[oldBigSimplexTable.length][oldBigSimplexTable[0].length];
        int pivotRowBigTableau = pivotElement.pivotRow() + 1;
        for (int i = 0; i < oldBigSimplexTable.length; i++) {
            for (int j = 0; j < oldBigSimplexTable[0].length; j++) {
                if (i == pivotRowBigTableau + 1 && j == pivotElement.pivotColumn()) {
                    newBigSimplexTable[i][j] = 1 / pivotElement.pivotElement();
                } else if (i == pivotRowBigTableau) {
                    newBigSimplexTable[i][j] = oldBigSimplexTable[i][j] / pivotElement.pivotElement();
                } else if (j == pivotElement.pivotColumn()) {
                    newBigSimplexTable[i][j] = oldBigSimplexTable[i][j] / (-pivotElement.pivotElement());
                } else {
                    newBigSimplexTable[i][j] = oldBigSimplexTable[i][j]
                            - (oldBigSimplexTable[pivotRowBigTableau][j] * oldBigSimplexTable[i][pivotElement.pivotColumn()])
                                    / pivotElement.pivotElement();
                }
            }
        }
        return newBigSimplexTable;
    }

    private PivotWrapper getPivotElement(SimplexTableau simplexTableau) {
        int columnIndex = getPivotColumn(simplexTableau.goalCoefficients());
        int rowIndex = getPivotRow(columnIndex, simplexTableau);
        return new PivotWrapper(columnIndex, rowIndex, simplexTableau.simplexTable()[rowIndex][columnIndex]);
    }

    private int getPivotColumn(double[] goalCoefficients) {
        return IntStream.range(0, goalCoefficients.length)
                .reduce((i, j) -> goalCoefficients[i] < goalCoefficients[j] ? i : j)
                .orElseThrow(() -> new IllegalStateException("pivot-column not found")
        );
    }

    private int getPivotRow(int pivotColumn, SimplexTableau simplexTableau) {
        int[] validIndicesList = IntStream.range(0, simplexTableau.simplexTable().length)
                .filter(i -> simplexTableau.simplexTable()[i][pivotColumn] > 0).toArray();
        return IntStream.of(validIndicesList)
                .reduce((i, j) -> (simplexTableau.rightSide()[i] / simplexTableau.simplexTable()[i][pivotColumn] < simplexTableau.rightSide()[j] / simplexTableau.simplexTable()[j][pivotColumn]) ? i : j)
                .orElseThrow(() -> new IllegalStateException("pivot-row not found")
        );
    }

    private boolean isUnLimited(SimplexTableau simplexTableau) {
        int pivotColumn = getPivotColumn(simplexTableau.goalCoefficients());
        return IntStream.range(0, simplexTableau.simplexTable().length).allMatch(i -> simplexTableau.simplexTable()[i][pivotColumn] <= 0);
    }

}


record ResultSimplexTableaus(
        List<SimplexTableau> simplexTableaus,
        boolean isUnlimited
) {
}


