package com.apexretail.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a completed sales transaction.
 * Contains sale ID, date and time, total price, and a list of sale items.
 * The total price is automatically recalculated when items are added.
 *
 * <p>
 * The sale ID is only set after the sale is persisted (e.g., by a repository).
 * The ID must be a positive number (≥ 1). A temporary Sale object may have its
 * ID left as 0 until {@link #setId(long)} is called with a valid positive ID.
 *
 * @author David
 * @version 1.0.0
 */
public class Sale {

    private long id; // 0 means not yet persisted; must become >0 when set
    private LocalDateTime saleDate;
    private BigDecimal totalPrice;
    private List<SaleItem> items;

    /**
     * Constructs a Sale with the given date and a zero total.
     * The item list is initially empty; use {@link #addItem(SaleItem)} to add
     * items.
     * The ID is not set – it must be assigned later via {@link #setId(long)}.
     *
     * @param saleDate date and time of the sale (cannot be null)
     * @throws IllegalArgumentException if saleDate is null
     */
    public Sale(LocalDateTime saleDate) {
        validateSaleDate(saleDate);
        this.saleDate = saleDate;
        this.items = new ArrayList<>();
        this.totalPrice = BigDecimal.ZERO;
    }

    /**
     * Adds a sale item to the transaction and recalculates the total price.
     *
     * @param item the SaleItem to add (cannot be null)
     * @throws IllegalArgumentException if item is null
     */
    public void addItem(SaleItem item) {
        if (item == null) {
            throw new IllegalArgumentException("SaleItem cannot be null");
        }
        items.add(item);
        recalculateTotal();
    }

    /**
     * Sets the sale ID. Can be called only once (typically after persistence).
     * The ID must be a positive number (≥ 1).
     *
     * @param id positive ID (must be > 0)
     * @throws IllegalArgumentException if id ≤ 0
     */
    public void setId(long id) {
        validateID(id);
        this.id = id;
    }

    /**
     * Returns the sale ID.
     *
     * @return the sale ID (0 if not yet persisted, otherwise > 0)
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the date and time when the sale occurred.
     *
     * @return the sale date
     */
    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    /**
     * Returns the total price of the sale (sum of all item subtotals).
     *
     * @return the total price
     */
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    /**
     * Returns an unmodifiable view of the list of sale items.
     *
     * @return an immutable list of SaleItem objects
     */
    public List<SaleItem> getItems() {
        return List.copyOf(items);
    }

    /**
     * Validates that the sale date is not null.
     *
     * @param saleDate date to validate
     * @throws IllegalArgumentException if date is null
     */
    private void validateSaleDate(LocalDateTime saleDate) {
        if (saleDate == null) {
            throw new IllegalArgumentException("Sale date cannot be null");
        }
    }

    /**
     * Validates that the sale ID is strictly positive.
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
     * Validates that the total price is non‑negative.
     * (Currently not used because total is recalculated from items,
     * but kept for completeness or future manual setting.)
     *
     * @param totalPrice price to validate
     * @throws IllegalArgumentException if price is null or negative
     */
    private void validateTotalPrice(BigDecimal totalPrice) {
        if (totalPrice == null || totalPrice.signum() < 0) {
            throw new IllegalArgumentException("Total price must be greater than or equal to 0.");
        }
    }

    /**
     * Recalculates the total price by summing the subtotals of all items.
     */
    private void recalculateTotal() {
        totalPrice = items.stream()
                .map(SaleItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public String toString() {
        return "Sale [id=" + id + ", saleDate=" + saleDate + ", totalPrice=" + totalPrice + ", items=" + items + "]";
    }
}