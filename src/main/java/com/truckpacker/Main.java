package com.truckpacker;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @brief Main class for the truckpacker knapsack problem.
 * @details The class is now refactored to accept inputs directly without reading files.
 */
public class Main {
    
    public static void main(String[] args) {
        int maxItems = 5;
        int maxVolume = 50;
        List<GoodsItem> items = List.of(
            new GoodsItem("item1", 10, 20, 2),
            new GoodsItem("item2", 5, 15, 3),
            new GoodsItem("item3", 8, 25, 1)
        );
        List<Store> stores = List.of(
            new Store("Store1", List.of("item1", "item2")),
            new Store("Store2", List.of("item3"))
        );

        executeKnapsack(maxItems, maxVolume, items, stores);
    }

    /**
     * @brief Executes the knapsack algorithm and prints the results.
     * @param maxItems Maximum number of items that can be packed.
     * @param maxVolume Maximum volume of the knapsack.
     * @param items List of available goods items.
     * @param stores List of stores requesting items.
     */
    public static void executeKnapsack(int maxItems, int maxVolume, List<GoodsItem> items, List<Store> stores) {
        // Create a KnapsackSolver instance and solve the knapsack problem
        KnapsackSolver solver = new KnapsackSolver();
        Map<List<GoodsItem>, Set<String>> result = solver.solveKnapsack(items, maxVolume, maxItems, stores);

        // Handle the result
        
        List<GoodsItem> packedItems = result.keySet().iterator().next();
        Set<String> storesToVisit = result.values().iterator().next();

        // Print packed items
        System.out.println("Packed items:");
        for (GoodsItem item : packedItems) {
            System.out.println(item);
        }

        // Print stores to visit
        System.out.println("\nStores to visit:");
        for (String store : storesToVisit) {
            System.out.println(store);
        }
    }
}

