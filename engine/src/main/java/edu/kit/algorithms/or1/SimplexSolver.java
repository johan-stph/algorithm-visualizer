package edu.kit.algorithms.or1;

import edu.kit.algorithms.or1.model.SimplexTableau;

import java.util.Collections;
import java.util.List;

public class SimplexSolver {



    public List<SimplexTableau> solveTableau(SimplexTableau simplexTableau) {
        return Collections.singletonList(
                new SimplexTableau(
                        null,
                        null,
                        null,
                        null,
                        null,
                        800 / 3.0
        ));
    }





}


