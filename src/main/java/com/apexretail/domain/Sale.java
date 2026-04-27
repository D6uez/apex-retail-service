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
 * @author David
 * @version 1.0.0
 */
public class Sale {

    private long id;
    private LocalDateTime saleDate;
    private BigDecimal totalPrice;
    private List<SaleItem> items;

    /**
     * Constructs a Sale with the specified ID, date, and total price.
     * The item list is initialized empty; use {@link #addItem(SaleItem)} to add
     * items.
     *
     * @param sId         sale ID (must be ≥ 0)
     * @param sSaleDate   date and time of the sale (cannot be null)
     * @param sTotalPrice initial total price (must be ≥ 0, can be zero)
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public Sale(LocalDateTime saleDate) {
        validateSaleDate(saleDate);

        this.saleDate = saleDate;
        this.items = new ArrayList<>();
        this.totalPrice = BigDecimal.ZERO;
    }

    /**
     * Validates that the sale date is not null.
     *
     * @param sSaleDate date to validate
     * @throws IllegalArgumentException if date is null
     */
    private void validateSaleDate(LocalDateTime sSaleDate) {
        if (sSaleDate == null) {
            throw new IllegalArgumentException("Sale date cannot be null");
        }
    }

    /**
     * Validates that the total price is non‑negative.
     *
     * @param sTotalPrice price to validate
     * @throws IllegalArgumentException if price is null or negative
     */
    private void validateTotalPrice(BigDecimal sTotalPrice) {
        if (sTotalPrice == null || sTotalPrice.signum() < 0) {
            throw new IllegalArgumentException("Total price must be greater than or equal to 0.");
        }
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
     * Recalculates the total price by summing the subtotals of all items.
     */
    private void recalculateTotal() {
        totalPrice = items.stream()
                .map(SaleItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void setId(long id) {
        validateID(id);
        this.id = id;
    }

    /**
     * Validates that the sale ID is non‑negative.
     *
     * @param sId ID to validate
     * @throws IllegalArgumentException if ID is negative
     */
    private void validateID(long sId) {
        if (sId <= 0) {
            throw new IllegalArgumentException("ID must be greater than or equal to 0.");
        }
    }

    @Override
    public String toString() {
        return "Sale [id=" + id + ", saleDate=" + saleDate + ", totalPrice=" + totalPrice + ", items=" + items + "]";
    }
}