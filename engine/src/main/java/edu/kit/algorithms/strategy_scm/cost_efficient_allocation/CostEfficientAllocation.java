package edu.kit.algorithms.strategy_scm.cost_efficient_allocation;

public class CostEfficientAllocation {


    public static ResultOfCostEfficientAllocation calculateCostEfficientAllocation(
            double squareMeters,
            double centerPricePerSquareMeter,
            double outerPricePerSquareMeter,
            double distanceToCenter,
            double costPerKilometer,
            double amountOfTravels
    ) {
        double r = -1 * Math.log(outerPricePerSquareMeter / centerPricePerSquareMeter) / distanceToCenter;
        double optimalDistance = Math.max((1 / r) * (Math.log(r * squareMeters * centerPricePerSquareMeter) - Math.log(costPerKilometer * amountOfTravels)), 0);
        double cost = squareMeters * centerPricePerSquareMeter * Math.exp(-r * optimalDistance) + costPerKilometer * amountOfTravels * optimalDistance;
        return new ResultOfCostEfficientAllocation(optimalDistance, cost, r);
    }


    public record ResultOfCostEfficientAllocation(double optimalDistance,
                                                  double cost,
                                                  double r) {
    }

}
