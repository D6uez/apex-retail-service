package com.apexretail.service;

import com.apexretail.domain.Product;

/**
 * Service for managing product inventory operations.
 * 
 * <p>
 * This service provides business operations for inventory management,
 * including selling products and restocking inventory. All operations
 * validate their inputs before execution.
 *
 * <p>
 * Example:
 * 
 * <pre>{@code
 * InventoryService inventory = new InventoryService();
 * inventory.restockProduct(laptop, 5); // Add 5 units to stock
 * inventory.sellProduct(laptop, 2); // Sell 2 units
 * }</pre>
 *
 * @author David
 * @version 1.0.0
 */
public class InventoryService {

    /**
     * Sells a specified quantity of a product, reducing its stock.
     * 
     * <p>
     * Validates the product and amount before calling the product's
     * decreaseStock method. This operation is atomic and will only
     * complete if sufficient stock is available.
     *
     * @param prod   product to sell (must not be null)
     * @param amount quantity to sell (must be > 0)
     * @throws IllegalArgumentException if product is null or amount is invalid
     * @throws IllegalArgumentException if insufficient stock is available
     * @see Product#decreaseStock(int)
     */
    public void sellProduct(Product prod, int amount) {
        validateProduct(prod);
        validateStockAdjustment(amount);
        prod.decreaseStock(amount);
    }

    /**
     * Restocks a product by adding the specified quantity to inventory.
     * 
     * <p>
     * Validates the product and amount before calling the product's
     * increaseStock method.
     *
     * @param prod   product to restock (must not be null)
     * @param amount quantity to add (must be > 0)
     * @throws IllegalArgumentException if product is null or amount is invalid
     * @see Product#increaseStock(int)
     */
    public void restockProduct(Product prod, int amount) {
        validateProduct(prod);
        validateStockAdjustment(amount);
        prod.increaseStock(amount);
    }

    /**
     * Validates that a product reference is not null.
     * 
     * @param prod product to validate
     * @throws IllegalArgumentException if product is null
     */
    private void validateProduct(Product prod) {
        if (prod == null) {
            throw new IllegalArgumentException("Invalid product.");
        }
    }

    /**
     * Validates that a stock adjustment amount is positive.
     * 
     * @param amount quantity to validate
     * @throws IllegalArgumentException if amount is not positive
     */
    private void validateStockAdjustment(int amount) {
        if (!(amount > 0)) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }
    }
}