package com.apexretail.domain;

import java.math.BigDecimal;

public class SaleItem {

    private long id;
    private Product product;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal subTotal;

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

    private BigDecimal calculateSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    private void validateProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
    }

    private void validateUnitPrice(BigDecimal price) {
        if (price == null || price.signum() < 0) {
            throw new IllegalArgumentException("Price must be non-negative");
        }
    }

    public BigDecimal getSubtotal() {
        return subTotal;
    }

    @Override
    public String toString() {
        return "SaleItem [id=" + id + ", product=" + product + ", quantity=" + quantity + ", unitPrice=" + unitPrice
                + ", subTotal=" + subTotal + "]";
    }

}