package edu.kit.algorithms.api.controllers;


import edu.kit.algorithms.strategy_scm.cost_efficient_allocation.CostEfficientAllocation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cost-efficient-allocation")
public class CostEfficientAllocationController {

        @GetMapping
        @CrossOrigin(origins = "http://localhost:3000")
        public ResponseEntity<CostEfficientAllocation.ResultOfCostEfficientAllocation> calculateCostEfficientAllocation(@RequestParam(name = "Q") double squareMeters,
                                                       @RequestParam(name = "Pz") double centerPricePerSquareMeter,
                                                       @RequestParam(name = "Po") double outerPricePerSquareMeter,
                                                       @RequestParam(name = "dZ") double distanceToCenter,
                                                       @RequestParam(name = "K") double costPerKilometer,
                                                       @RequestParam(name = "V") double amountOfTravels) {

            CostEfficientAllocation.ResultOfCostEfficientAllocation resultOfCostEfficientAllocation = CostEfficientAllocation.calculateCostEfficientAllocation(squareMeters, centerPricePerSquareMeter, outerPricePerSquareMeter, distanceToCenter, costPerKilometer, amountOfTravels);
            return ResponseEntity.ok(resultOfCostEfficientAllocation);
        }
}

