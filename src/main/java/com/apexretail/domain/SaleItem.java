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
     * Constructs a SaleItem with the specified ID, product, quantity, and unit
     * price.
     * The subtotal is automatically calculated as unitPrice × quantity.
     *
     * @param id        the sale item ID (must be ≥ 0)
     * @param product   the product being sold (cannot be null)
     * @param quantity  the quantity sold (must be > 0)
     * @param unitPrice the price per unit at the time of sale (must be
     *                  non‑negative)
     * @throws IllegalArgumentException if product is null, quantity ≤ 0, or
     *                                  unitPrice is null or negative
     */
    public SaleItem(long id, Product product, int quantity, BigDecimal unitPrice) {
        validateProduct(product);
        validateQuantity(quantity);
        validateUnitPrice(unitPrice);

        this.id = id;
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
     * Returns the subtotal for this line item.
     *
     * @return the calculated subtotal
     */
    public BigDecimal getSubtotal() {
        return subTotal;
    }

    @Override
    public String toString() {
        return "SaleItem [id=" + id + ", product=" + product + ", quantity=" + quantity + ", unitPrice=" + unitPrice
                + ", subTotal=" + subTotal + "]";
    }
}