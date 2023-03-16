package edu.kit.algorithms.or1;

import edu.kit.algorithms.or1.model.TransportProblem;

public class NorthWestSolver {

    public int[][] solveNorthWest(TransportProblem problem){
        int m = problem.getSupply().length;
        int n = problem.getDemand().length;
        int[] a = problem.getSupply().clone();
        int[] b = problem.getDemand().clone();
        int [][] x = new int[m][n];
        int i = 0, j = 0;
        while (i < m && j < n){
            x[i][j] = Math.min(a[i], b[j]);
            a[i] = a[i] - x[i][j];
            b[j] = b[j] - x[i][j];
            if(a[i] == 0){
                i += 1;
            }else j += 1;
        }
        return x;
    }

}
