package com.apexretail.domain;

import java.math.BigDecimal;

/**
 * Represents a single line item within a {@link Sale}.
 * Contains a product, quantity, unit price at the time of sale, and the
 * calculated subtotal.
 *
 * @author David
 * @version 1.0.0
 */
public class SaleItem {

    private long id;
    private Product product;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal subTotal;

    /**
     * Constructs a SaleItem with the specified product, quantity, and unit price.
     * The subtotal is automatically calculated as unitPrice × quantity.
     * The ID is not set in the constructor – it must be assigned later via
     * {@link #setId(long)} (typically after persistence).
     *
     * @param product   the product being sold (cannot be null)
     * @param quantity  the quantity sold (must be > 0)
     * @param unitPrice the price per unit at the time of sale (must be
     *                  non‑negative)
     * @throws IllegalArgumentException if product is null, quantity ≤ 0,
     *                                  or unitPrice is null or negative
     */
    public SaleItem(Product product, int quantity, BigDecimal unitPrice) {
        validateProduct(product);
        validateQuantity(quantity);
        validateUnitPrice(unitPrice);

        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subTotal = calculateSubtotal();
    }

    /**
     * Calculates the subtotal for this line item: unitPrice × quantity.
     *
     * @return the computed subtotal
     */
    private BigDecimal calculateSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    /**
     * Validates that the product is not null.
     *
     * @param product the product to validate
     * @throws IllegalArgumentException if product is null
     */
    private void validateProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
    }

    /**
     * Validates that the quantity is positive.
     *
     * @param quantity the quantity to validate
     * @throws IllegalArgumentException if quantity ≤ 0
     */
    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
    }

    /**
     * Validates that the unit price is non‑negative.
     *
     * @param price the price to validate
     * @throws IllegalArgumentException if price is null or negative
     */
    private void validateUnitPrice(BigDecimal price) {
        if (price == null || price.signum() < 0) {
            throw new IllegalArgumentException("Price must be non-negative");
        }
    }

    /**
     * Validates that the item ID is strictly positive.
     * (Used when setting the ID after persistence.)
     *
     * @param id ID to validate
     * @throws IllegalArgumentException if id ≤ 0
     */
    private void validateID(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be greater than 0.");
        }
    }

    /**
     * Sets the sale item ID. Typically called only once after the item has been
     * persisted to the database. The ID must be a positive number (≥ 1).
     *
     * @param id positive ID (must be > 0)
     * @throws IllegalArgumentException if id ≤ 0
     */
    public void setId(long id) {
        validateID(id);
        this.id = id;
    }

    /**
     * Returns the sale item ID.
     *
     * @return the ID (0 if not yet persisted, otherwise positive)
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the product ID associated with this sale item.
     *
     * @return the product ID
     */
    public long getProductId() {
        return product.getId();
    }

    /**
     * Returns the quantity of the product sold.
     *
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Returns the unit price at the time of sale.
     *
     * @return the unit price
     */
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    /**
     * Returns the subtotal for this line item (unitPrice × quantity).
     *
     * @return the calculated subtotal
     */
    public BigDecimal getSubtotal() {
        return subTotal;
    }

    @Override
    public String toString() {
        return "SaleItem [id=" + id + ", product=" + product + ", quantity=" + quantity
                + ", unitPrice=" + unitPrice + ", subTotal=" + subTotal + "]";
    }
}