package com.truckpacker;

/**
 * @brief GoodsItem class for the truckpacker knapsack problem.
 */
public class GoodsItem {
    private final String name;
    private final int volume;
    private final int value;
    private final int maxCount;

    /**
     * @brief Constructor for the GoodsItem class.
     * @param name Name of the item
     * @param volume Volume of the item
     * @param value Value of the item
     * @param maxCount Maximum number of items that can be packed
     */
    public GoodsItem(String name, int volume, int value, int maxCount) {
        this.name = name;
        this.volume = volume;
        this.value = value;
        this.maxCount = maxCount;
    }

    /**
     * @return Name of the item
     */
    public String getName() {
        return name;
    }

    /**
     * @return Volume of the item
     */
    public int getVolume() {
        return volume;
    }

    /**
     * @return Value of the item
     */
    public int getValue() {
        return value;
    }

    /**
     * @return Maximum number of items that can be packed
     */
    public int getMaxCount() {
        return maxCount;
    }
}
