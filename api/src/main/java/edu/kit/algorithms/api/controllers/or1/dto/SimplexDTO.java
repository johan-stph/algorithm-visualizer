package edu.kit.algorithms.api.controllers.or1.dto;





import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public record SimplexDTO(
        @NotNull @NotEmpty double[] goalCoefficients,
        @NotNull @NotEmpty double[][] constraintCoefficients,
        @NotNull @NotEmpty List<String> constraintSigns,
        @NotNull String minOrMax
) {
    @Override
    public String toString() {
        return "SimplexDTO{" +
                "goalCoefficients=" + Arrays.toString(goalCoefficients) +
                ", constraintCoefficients=" + Arrays.toString(constraintCoefficients) +
                ", constraintSigns=" + constraintSigns +
                ", minOrMax='" + minOrMax + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimplexDTO that = (SimplexDTO) o;
        return Arrays.equals(goalCoefficients, that.goalCoefficients) && Arrays.deepEquals(constraintCoefficients, that.constraintCoefficients) && Objects.equals(constraintSigns, that.constraintSigns) && Objects.equals(minOrMax, that.minOrMax);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(constraintSigns, minOrMax);
        result = 31 * result + Arrays.hashCode(goalCoefficients);
        result = 31 * result + Arrays.deepHashCode(constraintCoefficients);
        return result;
    }
}
