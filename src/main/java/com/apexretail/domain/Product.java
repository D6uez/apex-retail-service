package com.apexretail.domain;

import java.math.BigDecimal;

/**
 * Represents a product in the retail inventory system.
 * 
 * <p>
 * This class maintains product information and validates all attributes
 * to ensure data integrity. It includes inventory management capabilities.
 *
 * <p>
 * Example:
 * 
 * <pre>{@code
 * Product laptop = new Product(1001, "Laptop", new BigDecimal("999.99"), 10, Category.ELECTRONICS);
 * }</pre>
 *
 * @author David
 * @version 1.0.0
 */
public class Product {

    /** Unique product identifier - cannot be changed after creation. */
    private final long id;

    /** Product name. */
    private String name;

    /** Product price. */
    private BigDecimal price;

    /** Current quantity available in inventory. */
    private int quantityInStock;

    /** Product classification category. */
    private Category category;

    /**
     * Creates a new product with validated attributes.
     * 
     * @param pId              unique product identifier (must be ≥ 0)
     * @param pName            product name (cannot be null, empty, or blank)
     * @param pPrice           product price (must be ≥ 0)
     * @param pQuantityInStock initial stock quantity (must be ≥ 0)
     * @param pCategory        product category (cannot be null)
     * @throws IllegalArgumentException if any parameter fails validation
     */
    public Product(long pId, String pName, BigDecimal pPrice, int pQuantityInStock, Category pCategory) {
        validateID(pId);
        validateName(pName);
        validatePrice(pPrice);
        validateInitialStock(pQuantityInStock);
        validateCategory(pCategory);

        this.id = pId;
        this.name = pName;
        this.price = pPrice;
        this.quantityInStock = pQuantityInStock;
        this.category = pCategory;
    }

    /**
     * Increases stock quantity by specified amount.
     * 
     * @param amount quantity to add to inventory (must be > 0)
     * @throws IllegalArgumentException if amount is not positive
     */
    public void increaseStock(int amount) {
        validateStockAdjustment(amount);
        this.quantityInStock += amount;
    }

    /**
     * Decreases stock quantity by specified amount.
     * 
     * @param amount quantity to remove from inventory (must be > 0)
     * @throws IllegalArgumentException if amount is not positive or exceeds current
     *                                  stock
     */
    public void decreaseStock(int amount) {
        validateStockAdjustment(amount);
        hasSufficientStock(amount);
        this.quantityInStock -= amount;
    }

    /**
     * Checks if sufficient stock is available for requested amount.
     * 
     * @param requestedAmount quantity being requested
     * @throws IllegalArgumentException if requested amount is negative or exceeds
     *                                  available stock
     */
    private void hasSufficientStock(int requestedAmount) {
        if (requestedAmount > this.quantityInStock) {
            throw new IllegalArgumentException("Requested amount exceeds amount in stock.");
        }
    }

    /**
     * Validates stock adjustment amount is positive.
     * 
     * @param amount quantity to validate
     * @throws IllegalArgumentException if amount is not positive
     */
    private void validateStockAdjustment(int amount) {
        if (!(amount > 0)) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }
    }

    /**
     * Checks if product is currently available for sale.
     * 
     * @return true if at least one unit is in stock, false otherwise
     */
    public boolean isInStock() {
        return (this.quantityInStock > 0);
    }

    /**
     * Validates category is not null.
     * 
     * @param pCategory category to validate
     * @throws IllegalArgumentException if category is null
     */
    private void validateCategory(Category pCategory) {
        if (pCategory == null) {
            throw new IllegalArgumentException("Category must not be NULL.");
        }
    }

    /**
     * Validates stock quantity is non-negative.
     * 
     * @param pQuantityInStock quantity to validate
     * @throws IllegalArgumentException if quantity is negative
     */
    private void validateInitialStock(int pQuantityInStock) {
        if (!(pQuantityInStock >= 0)) {
            throw new IllegalArgumentException("Quantity must be greater than or equal to 0.");
        }
    }

    /**
     * Validates price is non-negative.
     * 
     * @param pPrice price to validate
     * @throws IllegalArgumentException if price is negative
     */
    private void validatePrice(BigDecimal pPrice) {
        if (!(pPrice.signum() >= 0)) {
            throw new IllegalArgumentException("Price must be greater than or equal to 0.");
        }
    }

    /**
     * Validates name is not null, empty, or blank.
     * 
     * @param pName name to validate
     * @throws IllegalArgumentException if name is null, empty, or contains only
     *                                  whitespace
     */
    private void validateName(String pName) {
        if (pName == null || pName.isBlank()) {
            throw new IllegalArgumentException("Invalid name.");
        }
    }

    /**
     * Validates ID is non-negative.
     * 
     * @param pId ID to validate
     * @throws IllegalArgumentException if ID is negative
     */
    private void validateID(long pId) {
        if (!(pId >= 0)) {
            throw new IllegalArgumentException("ID must be greater than or equal to 0.");
        }
    }

    /**
     * Returns the product's unique identifier.
     * 
     * @return product ID
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the product name.
     * 
     * @return product name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the product price.
     * 
     * @return product price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Returns the current stock quantity.
     * 
     * @return current stock quantity
     */
    public int getQuantityInStock() {
        return quantityInStock;
    }

    /**
     * Returns the product category.
     * 
     * @return product category
     */
    public Category getCategory() {
        return category;
    }
}