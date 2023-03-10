package edu.kit.algorithms.strategy_scm.cost_efficient_allocation;


import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class ThuenenModellTest {

    @Test
    void solveThuenenModell() {
        var inputList = List.of(new ThuenenModell.InputOfThuenenModell(
               "Kartoffelanbau",
                38,
                7
        ),
                new ThuenenModell.InputOfThuenenModell(
                        "Getreideanbau",
                        20,
                        4
                ),
                new ThuenenModell.InputOfThuenenModell(
                        "Viehzucht",
                        65,
                        12
                ),
                new ThuenenModell.InputOfThuenenModell(
                        "Industrie",
                        110,
                        40
                ),
                new ThuenenModell.InputOfThuenenModell(
                        "Dienstleistungen",
                        120,
                        25
                ));

        List<ThuenenModell.ResultOfThuenenModell> resultOfThuenenModells = ThuenenModell.solveThuenenModell(inputList);
        assertIterableEquals(
                List.of(
                        new ThuenenModell.ResultOfThuenenModell(
                                "Dienstleistungen",
                                0,
                                4.230769230769231
                        ),
                        new ThuenenModell.ResultOfThuenenModell(
                                "Viehzucht",
                                4.230769230769231,
                                5.4
                        ),
                        new ThuenenModell.ResultOfThuenenModell(
                                "Kartoffelanbau",
                                5.4,
                                5.428571428571429
                        )
                ),
                resultOfThuenenModells
        );

    }


}