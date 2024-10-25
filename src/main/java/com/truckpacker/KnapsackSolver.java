package com.truckpacker;

import java.util.*;

/**
 * @brief Knapsack solver class for the truckpacker knapsack problem.
 */
public class KnapsackSolver {

    /**
     * @brief Solves the knapsack problem using dynamic programming, considering store requests.
     * @param items List of goods items.
     * @param maxVolume Maximum volume the knapsack can hold.
     * @param maxItems Maximum number of items that can be packed.
     * @param stores List of stores with their requested items.
     * @return Map containing the list of goods items that can be packed and the stores that need to be visited.
     */
    public Map<List<GoodsItem>, Set<String>> solveKnapsack(List<GoodsItem> items, int maxVolume, int maxItems, List<Store> stores) {
        // Step 1: Filter items to include only those requested by stores
        Set<String> requestedItemNames = new HashSet<>();
        for (Store store : stores) {
            requestedItemNames.addAll(store.getRequestedItems());  // Collect all requested item names
        }

        // Create a list of filtered items based on store requests
        List<GoodsItem> filteredItems = new ArrayList<>();
        for (GoodsItem item : items) {
            if (requestedItemNames.contains(item.getName())) {
                filteredItems.add(item);
            }
        }

        

        // Step 2: Initialize dynamic programming (DP) table
        int numItems = filteredItems.size();  // Total number of filtered items
        int[][][] dp = new int[numItems + 1][maxVolume + 1][maxItems + 1];

        // Fill the DP table
        for (int itemIndex = 1; itemIndex <= numItems; itemIndex++) {
            GoodsItem currentItem = filteredItems.get(itemIndex - 1);  // Get current item
            int currentItemVolume = currentItem.getVolume();  // Volume of the current item
            int currentItemValue = currentItem.getValue();  // Value of the current item

            // Loop through each possible volume and number of items
            for (int volumeLeft = 0; volumeLeft <= maxVolume; volumeLeft++) {
                for (int itemsLeft = 0; itemsLeft <= maxItems; itemsLeft++) {
                    // Case 1: Do not take the current item
                    int prev_item_index = itemIndex - 1;
                    dp[itemIndex][volumeLeft][itemsLeft] = dp[prev_item_index][volumeLeft][itemsLeft];

                    // Case 2: Try taking the current item in various counts (if allowed by volume and item limit)
                    for (int count = 1; count <= currentItem.getMaxCount(); count++) {
                        int totalItemVolume = currentItemVolume * count;
                        int totalItemValue = currentItemValue * count;

                        // Check if the current count of items can fit within the remaining volume and item limits
                        if (totalItemVolume <= volumeLeft && count <= itemsLeft) {
                            int previousValue = dp[prev_item_index][volumeLeft - totalItemVolume][itemsLeft - count];
                            int newValue = previousValue + totalItemValue;

                            // Update the DP table if taking the items leads to a higher value
                            if (newValue > dp[itemIndex][volumeLeft][itemsLeft]) {
                                dp[itemIndex][volumeLeft][itemsLeft] = newValue;
                            }
                        }
                    }
                }
            }
        }

        // Step 3: Retrieve the selected items by backtracking through the DP table
        List<GoodsItem> selectedItems = new ArrayList<>();
        int remainingVolume = maxVolume;  // Remaining volume in the knapsack
        int remainingItems = maxItems;  // Remaining item slots in the knapsack

        for (int itemIndex = numItems; itemIndex > 0; itemIndex--) {
            int prev_item_index = itemIndex - 1;
            GoodsItem currentItem = filteredItems.get(prev_item_index);  // Get current item
            int currentItemVolume = currentItem.getVolume();  // Volume of the current item
            int currentItemValue = currentItem.getValue();  // Value of the current item

            // Check if the item was selected by comparing the DP values of adjacent states
            while (remainingVolume >= currentItemVolume && remainingItems > 0 &&
                   dp[itemIndex][remainingVolume][remainingItems] != dp[prev_item_index][remainingVolume][remainingItems]) {
                for (int count = 1; count <= currentItem.getMaxCount(); count++) {
                    int totalItemVolume = currentItemVolume * count;
                    int totalItemValue = currentItemValue * count;

                    // Check if this count of the item fits and matches the DP transition
                    if (remainingVolume >= totalItemVolume && remainingItems >= count &&
                        dp[itemIndex][remainingVolume][remainingItems] ==
                        dp[prev_item_index][remainingVolume - totalItemVolume][remainingItems - count] + totalItemValue) {
                        // Add the selected items to the result list
                        for (int c = 0; c < count; c++) {
                            selectedItems.add(currentItem);
                        }
                        // Update the remaining volume and items
                        remainingVolume -= totalItemVolume;
                        remainingItems -= count;
                        break;
                    }
                }
            }
        }

        // Step 4: Determine which stores must be visited based on the selected items
        Set<String> storesToVisit = new HashSet<>();
        for (GoodsItem item : selectedItems) {
            for (Store store : stores) {
                if (store.getRequestedItems().contains(item.getName())) {
                    storesToVisit.add(store.getName());  // Add store if it requested the selected item
                }
            }
        }

        // Step 5: Return the result as a map containing the packed items and the stores to visit
        Map<List<GoodsItem>, Set<String>> result = new HashMap<>();
        result.put(selectedItems, storesToVisit);
        return result;
    }
}
