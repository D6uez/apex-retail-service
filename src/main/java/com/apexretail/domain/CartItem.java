package com.apexretail.domain;

/**
 * Represents a single line item within a {@link Cart}.
 * Contains a product ID and the quantity of that product to be purchased.
 * Quantity can be updated after creation.
 *
 * @author David
 * @version 1.0.0
 */
public class CartItem {
    private final long productID;
    private int quantity;

    /**
     * Constructs a new CartItem with the specified product ID and quantity.
     * Validates that ID is non‑negative and quantity is positive.
     *
     * @param id  the product ID (must be ≥ 0)
     * @param qty the initial quantity (must be > 0)
     * @throws IllegalArgumentException if id is negative or qty ≤ 0
     */
    public CartItem(long id, int qty) {
        validateID(id);
        validateQuantity(qty);
        this.productID = id;
        this.quantity = qty;
    }

    /**
     * Returns the product ID associated with this cart item.
     *
     * @return the product ID
     */
    public long getProductID() {
        return productID;
    }

    /**
     * Returns the current quantity of this cart item.
     *
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Updates the quantity of this cart item.
     * Validates that the new quantity is positive.
     *
     * @param qty the new quantity (must be > 0)
     * @throws IllegalArgumentException if qty ≤ 0
     */
    public void setQuantity(int qty) {
        validateQuantity(qty);
        this.quantity = qty;
    }

    /**
     * Validates that the product ID is non‑negative.
     *
     * @param id the ID to validate
     * @throws IllegalArgumentException if id < 0
     */
    private void validateID(long id) {
        if (id < 0) {
            throw new IllegalArgumentException("Product ID must be greater than or equal to 0.");
        }
    }

    /**
     * Validates that the quantity is positive.
     *
     * @param qty the quantity to validate
     * @throws IllegalArgumentException if qty ≤ 0
     */
    private void validateQuantity(int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }
    }
}