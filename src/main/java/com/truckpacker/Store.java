package com.truckpacker;

import java.util.List;

/**
 * @brief Store class representing a store and its requested items.
 */
public class Store {
    private final String name;
    private final List<String> requestedItems;

    /**
     * @brief Constructor for the Store class.
     * @param name Name of the store.
     * @param requestedItems List of item names requested by the store.
     */
    public Store(String name, List<String> requestedItems) {
        this.name = name;
        this.requestedItems = requestedItems;
    }

    /**
     * @return Name of the store.
     */
    public String getName() {
        return name;
    }

    /**
     * @return List of item names requested by the store.
     */
    public List<String> getRequestedItems() {
        return requestedItems;
    }

}
