package edu.kit.algorithms.api.controllers.or1;


import edu.kit.algorithms.api.controllers.or1.dto.SimplexDTO;
import edu.kit.algorithms.or1.ResultSimplexTableaus;
import edu.kit.algorithms.or1.SimplexSolver;
import edu.kit.algorithms.or1.model.EquationUtils;
import edu.kit.algorithms.or1.model.MaxOrMin;
import edu.kit.algorithms.or1.model.SimplexModel;
import edu.kit.algorithms.or1.model.SimplexTableau;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@RequestMapping("/api/v1/simplex")
@RestController
public class SimplexController {


    @PostMapping()
    @CrossOrigin(origins = "*")
    public ResponseEntity<ResultSimplexTableaus> calculateSimplex(@RequestBody SimplexDTO dto) {
        String[] variables = IntStream.range(0, dto.goalCoefficients().length).mapToObj(i -> "x" + (i + 1)).toArray(String[]::new);
        MaxOrMin maxOrMin = MaxOrMin.valueOf(dto.minOrMax());
        double[][] constraintCoefficients = Arrays.stream(dto.constraintCoefficients())
                .map(row -> Arrays.copyOf(row, row.length - 1))
                .toArray(double[][]::new);
        double[] bVector = Arrays.stream(dto.constraintCoefficients())
                .mapToDouble(row -> row[row.length - 1])
                .toArray();
        List<edu.kit.algorithms.utils.Tupel<edu.kit.algorithms.or1.model.EquationUtils, Double>> constraintEquations = IntStream.range(0, dto.constraintCoefficients().length)
                .mapToObj(i -> new edu.kit.algorithms.utils.Tupel<>(EquationUtils.fromString(dto.constraintSigns().get(i)), bVector[i]))
                .toList();
        SimplexModel model = new SimplexModel(variables, dto.goalCoefficients(), maxOrMin, constraintCoefficients, constraintEquations);
        SimplexTableau tableau = model.toTablou();
        if (tableau != null) {
            return ResponseEntity.ok(new SimplexSolver().solveTableau(tableau));
        }
        return ResponseEntity.ok(null);
    }

}
