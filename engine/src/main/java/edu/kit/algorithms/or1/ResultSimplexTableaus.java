package edu.kit.algorithms.or1;

import edu.kit.algorithms.or1.model.SimplexTableau;

import java.util.List;

public record ResultSimplexTableaus(
        List<SimplexTableau> simplexTableaus,
        boolean isUnlimited
) {
}
