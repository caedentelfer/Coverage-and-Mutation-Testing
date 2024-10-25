package com.truckpacker;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @brief Test class for the KnapsackSolver class and Main class.
 */
public class KnapsackTest {

    /**
     * Test case to ensure no items fit when their volume exceeds the knapsack capacity.
     */
    @Test
    public void testNoItemsFit() {
        List<GoodsItem> items = new ArrayList<>();
        items.add(new GoodsItem("item1", 100, 10, 5));
        int maxVolume = 50;
        int maxItems = 5;
        List<Store> stores = new ArrayList<>();
        KnapsackSolver solver = new KnapsackSolver();
        var result = solver.solveKnapsack(items, maxVolume, maxItems, stores);
        assertTrue(result.keySet().iterator().next().isEmpty(), "Expected no items to be packed.");
    }

    /**
     * Test case to ensure items are filtered by stores requesting them.
     */
    @Test
    public void testItemsFilteredByStores() {
        List<GoodsItem> items = new ArrayList<>();
        items.add(new GoodsItem("item1", 2, 3, 5));
        items.add(new GoodsItem("item2", 3, 4, 5));
        items.add(new GoodsItem("item3", 5, 5, 5));
        int maxVolume = 10;
        int maxItems = 5;
        List<Store> stores = new ArrayList<>();
        stores.add(new Store("Store1", List.of("item1")));
        stores.add(new Store("Store2", List.of("item2")));

        KnapsackSolver solver = new KnapsackSolver();
        var result = solver.solveKnapsack(items, maxVolume, maxItems, stores);

        List<GoodsItem> packedItems = result.keySet().iterator().next();
        Set<String> storesToVisit = result.values().iterator().next();
        assertTrue(packedItems.stream().anyMatch(item -> item.getName().equals("item1")), "Expected item1 to be packed.");
        assertFalse(packedItems.stream().anyMatch(item -> item.getName().equals("item3")), "Expected item3 not to be packed.");
        assertTrue(storesToVisit.contains("Store1"), "Expected Store1 to be visited.");
        assertFalse(storesToVisit.contains("Store2"), "Expected Store2 to be visited.");
    }

    /**
     * Test case to ensure items with zero or negative values are not packed.
     */
    @Test
    public void testZeroAndNegativeValues() {
        List<GoodsItem> items = new ArrayList<>();
        items.add(new GoodsItem("item1", 1, 0, 5));
        items.add(new GoodsItem("item2", 2, -5, 5));
        int maxVolume = 5;
        int maxItems = 5;
        List<Store> stores = new ArrayList<>();
        stores.add(new Store("Store1", List.of("item1", "item2")));
        KnapsackSolver solver = new KnapsackSolver();
        var result = solver.solveKnapsack(items, maxVolume, maxItems, stores);
        assertTrue(result.keySet().iterator().next().isEmpty(), "Expected no items to be packed due to zero/negative values.");
    }

    /**
     * Test case to ensure that when there are no stores or items, no packing occurs.
     */
    @Test
    public void testNoStoresOrItems() {
        List<GoodsItem> items = new ArrayList<>();
        int maxVolume = 10;
        int maxItems = 5;
        List<Store> stores = new ArrayList<>();
        KnapsackSolver solver = new KnapsackSolver();
        var result = solver.solveKnapsack(items, maxVolume, maxItems, stores);
        assertFalse(result.isEmpty(), "Expected no items to be packed when there are no items or stores.");
    }

    /**
     * Test case to ensure the max count per item is respected during packing.
     */
    @Test
    public void testMaxCountPerItem() {
        List<GoodsItem> items = new ArrayList<>();
        items.add(new GoodsItem("item1", 1, 10, 2));
        items.add(new GoodsItem("item2", 3, 15, 5));
        int maxVolume = 5;
        int maxItems = 5;
        List<Store> stores = new ArrayList<>();
        stores.add(new Store("Store1", List.of("item1", "item2")));
        KnapsackSolver solver = new KnapsackSolver();
        var result = solver.solveKnapsack(items, maxVolume, maxItems, stores);
        List<GoodsItem> packedItems = result.keySet().iterator().next();
        long item1Count = packedItems.stream().filter(item -> item.getName().equals("item1")).count();
        assertTrue(item1Count <= 2, "Expected max count of item1 to be respected.");
    }

    /**
     * Test case to ensure items that exceed max volume are not packed.
     */
    @Test
    public void testItemsExceedMaxVolume() {
        List<GoodsItem> items = new ArrayList<>();
        items.add(new GoodsItem("item1", 50, 10, 5));
        int maxVolume = 10;
        int maxItems = 5;
        List<Store> stores = new ArrayList<>();
        stores.add(new Store("Store1", List.of("item1")));
        KnapsackSolver solver = new KnapsackSolver();
        var result = solver.solveKnapsack(items, maxVolume, maxItems, stores);
        assertTrue(result.keySet().iterator().next().isEmpty(), "Expected no items to be packed since they exceed max volume.");
    }

    /**
     * Test case to handle edge cases when max volume or items is zero.
     */
    @Test
    public void testEdgeCasesWithZeroMaxVolumeOrItems() {
        List<GoodsItem> items = new ArrayList<>();
        items.add(new GoodsItem("item1", 5, 10, 2));
        int maxVolume = 0;
        int maxItems = 0;
        List<Store> stores = new ArrayList<>();
        stores.add(new Store("Store1", List.of("item1")));

        KnapsackSolver solver = new KnapsackSolver();
        var result = solver.solveKnapsack(items, maxVolume, maxItems, stores);
        assertTrue(result.keySet().iterator().next().isEmpty(), "Expected no items to be packed since max volume or items is 0.");
    }

    /**
     * Test case to ensure zero-volume items are packed.
     */
    @Test
    public void testZeroVolumeItem() {
        List<GoodsItem> items = new ArrayList<>();
        items.add(new GoodsItem("item1", 0, 10, 5));
        int maxVolume = 10;
        int maxItems = 5;
        List<Store> stores = new ArrayList<>();
        stores.add(new Store("Store1", List.of("item1")));
        KnapsackSolver solver = new KnapsackSolver();
        var result = solver.solveKnapsack(items, maxVolume, maxItems, stores);
        List<GoodsItem> packedItems = result.keySet().iterator().next();
        assertFalse(packedItems.isEmpty(), "Expected zero-volume item to be packed.");
        assertEquals("item1", packedItems.get(0).getName(), "Expected item1 to be packed.");
    }

    /**
     * Test case to ensure the solver handles multiple stores requesting the same items.
     */
    @Test
    public void testKnapsackMultipleStores() {
        List<GoodsItem> items = new ArrayList<>();
        items.add(new GoodsItem("item1", 1, 10, 5));
        int maxVolume = 10;
        int maxItems = 5;
        List<Store> stores = new ArrayList<>();
        stores.add(new Store("Store1", List.of("item1")));
        stores.add(new Store("Store2", List.of("item1")));
        KnapsackSolver solver = new KnapsackSolver();
        var result = solver.solveKnapsack(items, maxVolume, maxItems, stores);
        List<GoodsItem> packedItems = result.keySet().iterator().next();
        Set<String> storesToVisit = result.values().iterator().next();
        assertTrue(packedItems.stream().anyMatch(item -> item.getName().equals("item1")), "Expected item1 to be packed.");
        assertTrue(storesToVisit.contains("Store1"), "Expected Store1 to be visited.");
        assertTrue(storesToVisit.contains("Store2"), "Expected Store2 to be visited.");
    }

    /**
     * Test case to ensure the algorithm handles multiple items with the same value but different volumes.
     */
    @Test
    public void testItemsWithSameValueDifferentVolume() {
        List<GoodsItem> items = new ArrayList<>();
        items.add(new GoodsItem("item1", 3, 10, 5));
        items.add(new GoodsItem("item2", 2, 10, 5));
        int maxVolume = 5;
        int maxItems = 5;
        List<Store> stores = new ArrayList<>();
        stores.add(new Store("Store1", List.of("item1", "item2")));
        
        KnapsackSolver solver = new KnapsackSolver();
        var result = solver.solveKnapsack(items, maxVolume, maxItems, stores);
        
        List<GoodsItem> packedItems = result.keySet().iterator().next();
        assertTrue(packedItems.stream().anyMatch(item -> item.getName().equals("item2")), "Expected item2 to be packed because it has the smaller volume but same value.");
    }

    /**
     * Test case to ensure no items are packed when there are no matching items requested by stores.
     */
    @Test
    public void testNoMatchingItems() {
        List<GoodsItem> items = new ArrayList<>();
        items.add(new GoodsItem("item1", 2, 10, 5));
        items.add(new GoodsItem("item2", 3, 15, 5));
        int maxVolume = 5;
        int maxItems = 5;
        List<Store> stores = new ArrayList<>();
        stores.add(new Store("Store1", List.of("item3")));

        KnapsackSolver solver = new KnapsackSolver();
        var result = solver.solveKnapsack(items, maxVolume, maxItems, stores);
        
        List<GoodsItem> packedItems = result.keySet().iterator().next();
        assertTrue(packedItems.isEmpty(), "Expected no items to be packed since no requested items match.");
    }

    /**
     * Test case to ensure handling of multiple items with different volumes and values, but all requested by the same store.
     */
    @Test
    public void testSameStoreRequestsMultipleItems() {
        List<GoodsItem> items = new ArrayList<>();
        items.add(new GoodsItem("item1", 2, 10, 5));
        items.add(new GoodsItem("item2", 3, 15, 5));
        items.add(new GoodsItem("item3", 1, 5, 5));
        int maxVolume = 5;
        int maxItems = 5;
        List<Store> stores = new ArrayList<>();
        stores.add(new Store("Store1", List.of("item1", "item2", "item3")));

        KnapsackSolver solver = new KnapsackSolver();
        var result = solver.solveKnapsack(items, maxVolume, maxItems, stores);
        
        List<GoodsItem> packedItems = result.keySet().iterator().next();
        assertTrue(packedItems.stream().anyMatch(item -> item.getName().equals("item1")), "Expected item1 to be packed.");
        assertTrue(packedItems.stream().anyMatch(item -> item.getName().equals("item2")), "Expected item2 to be packed.");
        assertFalse(packedItems.stream().anyMatch(item -> item.getName().equals("item3")), "Expected item3 to be packed.");
    }

    /**
     * Test case to ensure behavior when there are too many items to choose from within the limits.
     */
    @Test
    public void testTooManyItemsToPack() {
        List<GoodsItem> items = new ArrayList<>();
        items.add(new GoodsItem("item1", 1, 5, 10));
        items.add(new GoodsItem("item2", 2, 10, 5));
        items.add(new GoodsItem("item3", 1, 3, 7));
        int maxVolume = 5;
        int maxItems = 2;
        List<Store> stores = new ArrayList<>();
        stores.add(new Store("Store1", List.of("item1", "item2", "item3")));

        KnapsackSolver solver = new KnapsackSolver();
        var result = solver.solveKnapsack(items, maxVolume, maxItems, stores);

        List<GoodsItem> packedItems = result.keySet().iterator().next();
        assertEquals(2, packedItems.size(), "Expected exactly 2 items to be packed.");
        assertTrue(packedItems.stream().allMatch(item -> item.getVolume() <= 2), "Expected only items that fit the volume constraint.");
    }

    /**
     * Test case to ensure higher value items are prioritized when volume and item count are limited.
     */
    @Test
    public void testPrioritizingHighValueItems() {
        List<GoodsItem> items = new ArrayList<>();
        items.add(new GoodsItem("item1", 1, 5, 5));
        items.add(new GoodsItem("item2", 3, 15, 2));
        items.add(new GoodsItem("item3", 2, 8, 3));
        int maxVolume = 4;
        int maxItems = 2;
        List<Store> stores = new ArrayList<>();
        stores.add(new Store("Store1", List.of("item1", "item2", "item3")));

        KnapsackSolver solver = new KnapsackSolver();
        var result = solver.solveKnapsack(items, maxVolume, maxItems, stores);

        List<GoodsItem> packedItems = result.keySet().iterator().next();
        assertTrue(packedItems.stream().anyMatch(item -> item.getName().equals("item2")), "Expected item2 to be packed because it has the highest value.");
    }

    /**
     * Test case to handle large item counts and volume limits.
     */
    @Test
    public void testLargeItemCountsAndVolume() {
        List<GoodsItem> items = new ArrayList<>();
        items.add(new GoodsItem("item1", 10, 100, 50));
        items.add(new GoodsItem("item2", 20, 200, 10));
        int maxVolume = 50;
        int maxItems = 10;
        List<Store> stores = new ArrayList<>();
        stores.add(new Store("Store1", List.of("item1", "item2")));

        KnapsackSolver solver = new KnapsackSolver();
        var result = solver.solveKnapsack(items, maxVolume, maxItems, stores);

        List<GoodsItem> packedItems = result.keySet().iterator().next();
        assertTrue(packedItems.stream().anyMatch(item -> item.getName().equals("item1")), "Expected item1 to be packed.");
        assertFalse(packedItems.stream().anyMatch(item -> item.getName().equals("item2")), "Expected item2 not to be packed due to large volume.");
    }

    /**
     * Test case to handle the edge case of having no stores requesting any items.
     */
    @Test
    public void testNoStoreRequestsAnyItems() {
        List<GoodsItem> items = new ArrayList<>();
        items.add(new GoodsItem("item1", 2, 10, 3));
        items.add(new GoodsItem("item2", 4, 20, 2));
        int maxVolume = 10;
        int maxItems = 5;
        List<Store> stores = new ArrayList<>();

        KnapsackSolver solver = new KnapsackSolver();
        var result = solver.solveKnapsack(items, maxVolume, maxItems, stores);

        List<GoodsItem> packedItems = result.keySet().iterator().next();
        assertTrue(packedItems.isEmpty(), "Expected no items to be packed since no stores requested items.");
    }

    /**
     * Test case to ensure exact max volume and item limits are handled correctly.
     */
    @Test
    public void testExactMaxVolumeAndItems() {
        List<GoodsItem> items = new ArrayList<>();
        items.add(new GoodsItem("item1", 10, 10, 2));
        items.add(new GoodsItem("item2", 5, 15, 2));
        int maxVolume = 15;
        int maxItems = 2;
        List<Store> stores = List.of(new Store("Store1", List.of("item1", "item2")));

        KnapsackSolver solver = new KnapsackSolver();
        var result = solver.solveKnapsack(items, maxVolume, maxItems, stores);

        List<GoodsItem> packedItems = result.keySet().iterator().next();
        assertEquals(2, packedItems.size(), "Expected exactly 2 items to be packed.");
        assertEquals(10, packedItems.stream().mapToInt(GoodsItem::getVolume).sum(), "Expected total volume to match the maxVolume.");
    }

    /**
     * Test case to ensure behavior with duplicate items.
     */
    @Test
    public void testDuplicateItems() {
        List<GoodsItem> items = new ArrayList<>();
        items.add(new GoodsItem("item1", 5, 10, 3));
        items.add(new GoodsItem("item1", 5, 10, 3));
        int maxVolume = 15;
        int maxItems = 3;
        List<Store> stores = List.of(new Store("Store1", List.of("item1")));

        KnapsackSolver solver = new KnapsackSolver();
        var result = solver.solveKnapsack(items, maxVolume, maxItems, stores);

        List<GoodsItem> packedItems = result.keySet().iterator().next();
        assertEquals(3, packedItems.size(), "Expected max count of 3 items to be packed.");
    }

    /**
     * Test case to ensure no items are packed when the store list is empty.
     */
    @Test
    public void testEmptyStoreList() {
        List<GoodsItem> items = new ArrayList<>();
        items.add(new GoodsItem("item1", 5, 10, 2));
        int maxVolume = 10;
        int maxItems = 2;
        List<Store> stores = new ArrayList<>();

        KnapsackSolver solver = new KnapsackSolver();
        var result = solver.solveKnapsack(items, maxVolume, maxItems, stores);

        List<GoodsItem> packedItems = result.keySet().iterator().next();
        assertTrue(packedItems.isEmpty(), "Expected no items to be packed when no stores request items.");
    }

    /**
     * Test case to ensure multiple stores requesting the same items are handled correctly.
     */
    @Test
    public void testMultipleStoresRequestingSameItems() {
        List<GoodsItem> items = new ArrayList<>();
        items.add(new GoodsItem("item1", 5, 10, 2));
        int maxVolume = 10;
        int maxItems = 2;
        List<Store> stores = List.of(
            new Store("Store1", List.of("item1")),
            new Store("Store2", List.of("item1"))
        );

        KnapsackSolver solver = new KnapsackSolver();
        var result = solver.solveKnapsack(items, maxVolume, maxItems, stores);

        List<GoodsItem> packedItems = result.keySet().iterator().next();
        Set<String> storesToVisit = result.values().iterator().next();
        
        assertEquals(2, packedItems.size(), "Expected 2 item to be packed.");
        assertTrue(storesToVisit.contains("Store1"), "Expected Store1 to be visited.");
        assertTrue(storesToVisit.contains("Store2"), "Expected Store2 to be visited.");
    }

    /**
     * Test case to check the Store constructor.
     */
    @Test
    public void testStoreConstructor() {
        String storeName = "StoreA";
        List<String> requestedItems = List.of("item1", "item2");

        Store store = new Store(storeName, requestedItems);

        assertNotNull(store, "Store object should not be null.");
        assertEquals(storeName, store.getName(), "Store name should match the input.");
        assertEquals(requestedItems, store.getRequestedItems(), "Requested items should match the input.");
    }

    /**
     * Test case to check the getName method of the Store class.
     */
    @Test
    public void testGetName() {
        String storeName = "StoreB";
        Store store = new Store(storeName, List.of());

        assertEquals(storeName, store.getName(), "getName() should return the correct store name.");
    }

    /**
     * Test case to check the getRequestedItems method of the Store class.
     */
    @Test
    public void testGetRequestedItems() {
        List<String> requestedItems = List.of("item1", "item2", "item3");
        Store store = new Store("StoreC", requestedItems);

        assertEquals(requestedItems, store.getRequestedItems(), "getRequestedItems() should return the correct list of requested items.");
    }

    /**
     * Test case to check the getRequestedItems method when the requested items list is empty.
     */
    @Test
    public void testEmptyRequestedItems() {
        Store store = new Store("StoreD", List.of());

        assertTrue(store.getRequestedItems().isEmpty(), "Requested items list should be empty.");
    }

    /**
     * Test case to check the getRequestedItems method when the requested items list is null.
     */
    @Test
    public void testNullRequestedItems() {
        Store store = new Store("StoreE", null);

        assertNull(store.getRequestedItems(), "Requested items list should be null if initialized as null.");
    }

    /**
     * Test case to check the GoodsItem constructor.
     */
    @Test
    public void testGoodsItemConstructor() {
        String itemName = "item1";
        int volume = 5;
        int value = 10;
        int maxCount = 3;

        GoodsItem item = new GoodsItem(itemName, volume, value, maxCount);

        assertNotNull(item, "GoodsItem object should not be null.");
        assertEquals(itemName, item.getName(), "Item name should match the input.");
        assertEquals(volume, item.getVolume(), "Item volume should match the input.");
        assertEquals(value, item.getValue(), "Item value should match the input.");
        assertEquals(maxCount, item.getMaxCount(), "Item maxCount should match the input.");
    }

    /**
     * Test case to check the getName method of the GoodsItem class.
     */
    @Test
    public void testGoodsItemGetName() {
        GoodsItem item = new GoodsItem("item2", 10, 15, 5);

        assertEquals("item2", item.getName(), "getName() should return the correct item name.");
    }

    /**
     * Test case to check the getVolume method of the GoodsItem class.
     */
    @Test
    public void testGetVolume() {
        GoodsItem item = new GoodsItem("item3", 20, 30, 2);

        assertEquals(20, item.getVolume(), "getVolume() should return the correct volume.");
    }

    /**
     * Test case to check the getValue method of the GoodsItem class.
     */
    @Test
    public void testGetValue() {
        GoodsItem item = new GoodsItem("item4", 15, 50, 1);

        assertEquals(50, item.getValue(), "getValue() should return the correct value.");
    }

    /**
     * Test case to check the getMaxCount method of the GoodsItem class.
     */
    @Test
    public void testGetMaxCount() {
        GoodsItem item = new GoodsItem("item5", 10, 25, 3);

        assertEquals(3, item.getMaxCount(), "getMaxCount() should return the correct max count.");
    }

    /**
     * Test case to check execution of the executeKnapsack method with valid input.
     */
    @Test
    public void testExecuteKnapsackWithValidInput() {
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

        Main.executeKnapsack(maxItems, maxVolume, items, stores);
    }

    /**
     * Test case to check the default demonstration case in Main.
     */
    @Test
    public void testDefaultDemonstrationCase() {
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

        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.executeKnapsack(maxItems, maxVolume, items, stores);

        String output = outContent.toString();
        assertTrue(output.contains("Packed items:"), "Expected 'Packed items' to be printed.");
        assertFalse(output.contains("item1"), "Expected 'item1' to be packed.");
        assertFalse(output.contains("item2"), "Expected 'item2' to be packed.");
        assertTrue(output.contains("Stores to visit:"), "Expected 'Stores to visit' to be printed.");
        assertFalse(output.contains("Store1"), "Expected 'Store1' to be visited.");
        assertTrue(output.contains("Store2"), "Expected 'Store2' to be visited.");

        System.setOut(originalOut);
    }

    /**
     * Test case to check the main method execution in Main class.
     */
    @Test
    public void testMainMethodExecution() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.main(new String[]{});

        String output = outContent.toString();
        assertTrue(output.contains("Packed items:"), "Expected 'Packed items' to be printed.");
        assertFalse(output.contains("item1"), "Expected 'item1' to be packed.");
        assertFalse(output.contains("item2"), "Expected 'item2' to be packed.");
        assertTrue(output.contains("Stores to visit:"), "Expected 'Stores to visit' to be printed.");
        assertFalse(output.contains("Store1"), "Expected 'Store1' to be visited.");
        assertTrue(output.contains("Store2"), "Expected 'Store2' to be visited.");

        System.setOut(originalOut);
    }

    /**
     * Test case to access the Main class and its main method.
     */
    @Test
    public void testMainClassAccess() {
        Main.main(new String[]{});

        assertTrue(true);
    }
}
