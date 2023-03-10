package edu.kit.algorithms.or1.model;

import lombok.Getter;

@Getter
public enum MaxOrMin {
    MAX("max"),
    MIN("min");

    private final String symbol;

    MaxOrMin(String symbol) {
        this.symbol = symbol;
    }


}
