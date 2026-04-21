package com.apexretail.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a shopping cart containing items to be purchased.
 * Each item is tracked by product ID and quantity.
 * Operations include adding, removing, updating quantities, and clearing the
 * cart.
 * All modifications validate input parameters before execution.
 *
 * @author David
 * @version 1.0.0
 */
public class Cart {

    /** Internal list of cart items. */
    private List<CartItem> currentCart = new ArrayList<>();

    /**
     * Adds a product to the cart with the specified quantity.
     * If the product already exists in the cart, the quantity is increased.
     *
     * @param prodID the product ID (must be ≥ 0)
     * @param qty    the quantity to add (must be > 0)
     * @throws IllegalArgumentException if ID is invalid or quantity is not positive
     */
    public void addItem(long prodID, int qty) {
        validateID(prodID);
        validateQuantity(qty);

        for (CartItem citm : currentCart) {
            if (citm.getProductID() == prodID) {
                int currentQty = citm.getQuantity();
                citm.setQuantity(currentQty + qty);
                return;
            }
        }

        currentCart.add(new CartItem(prodID, qty));
    }

    /**
     * Removes a product from the cart by its ID.
     *
     * @param prodID the product ID to remove (must be ≥ 0)
     * @return true if an item was removed, false otherwise
     * @throws IndexOutOfBoundsException if the cart is empty
     * @throws IllegalArgumentException  if ID is invalid
     */
    public boolean removeItem(long prodID) {
        validateID(prodID);

        if (currentCart.isEmpty()) {
            throw new IndexOutOfBoundsException("Current Cart is empty.");
        }

        return currentCart.removeIf(item -> item.getProductID() == prodID);
    }

    /**
     * Updates the quantity of an existing product in the cart.
     * If the product is not present, an exception is thrown.
     *
     * @param prodID the product ID (must be ≥ 0)
     * @param qty    the new quantity (must be > 0)
     * @throws IllegalArgumentException if ID is invalid, quantity is not positive,
     *                                  or product is not found in the cart
     */
    public void updateQuantity(long prodID, int qty) {
        validateID(prodID);
        validateQuantity(qty);

        for (CartItem citm : currentCart) {
            if (citm.getProductID() == prodID) {
                citm.setQuantity(qty);
                return;
            }
        }
        throw new IllegalArgumentException("Product not found in cart: " + prodID);
    }

    /**
     * Returns an unmodifiable view of the current cart items.
     *
     * @return an immutable list of cart items
     */
    public List<CartItem> getItems() {
        return List.copyOf(currentCart);
    }

    /**
     * Removes all items from the cart.
     */
    public void clear() {
        currentCart.clear();
    }

    /**
     * Validates that the quantity is positive.
     *
     * @param qty the quantity to validate
     * @throws IllegalArgumentException if quantity is not positive
     */
    private void validateQuantity(int qty) {
        if (!(qty > 0)) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }
    }

    /**
     * Validates that the product ID is non-negative.
     *
     * @param prodID the ID to validate
     * @throws IllegalArgumentException if ID is negative
     */
    private void validateID(long prodID) {
        if (!(prodID >= 0)) {
            throw new IllegalArgumentException("ID must be greater than or equal to 0.");
        }
    }
}