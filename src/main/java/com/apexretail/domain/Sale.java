package com.apexretail.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Sale {

    private long id;

    private LocalDateTime saleDate;

    private BigDecimal totalPrice;

    private List<SaleItem> items;

    public Sale(long sId, LocalDateTime sSaleDate, BigDecimal sTotalPrice) {
        validateID(sId);
        validateSaleDate(sSaleDate);
        validateTotalPrice(sTotalPrice);

        this.id = sId;
        this.saleDate = sSaleDate;
        this.totalPrice = sTotalPrice;
        this.items = new ArrayList<>();
    }

    private void validateID(long sId) {
        if (!(sId >= 0)) {
            throw new IllegalArgumentException("ID must be greater than or equal to 0.");
        }
    }

    private void validateSaleDate(LocalDateTime sSaleDate) {
        if (sSaleDate == null) {
            throw new IllegalArgumentException("Sales date cannot be null");
        }
    }

    private void validateTotalPrice(BigDecimal sTotalPrice) {
        if (sTotalPrice == null || sTotalPrice.signum() < 0) {
            throw new IllegalArgumentException("Total price must be greater than or equal to 0.");
        }
    }

    public void addItem(SaleItem item) {
        if (item == null) {
            throw new IllegalArgumentException("SaleItem cannot be null");
        }
        items.add(item);
        recalculateTotal();
    }

    private void recalculateTotal() {
        totalPrice = items.stream().map(SaleItem::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public String toString() {
        return "Sale [id=" + id + ", saleDate=" + saleDate + ", totalPrice=" + totalPrice + ", items=" + items + "]";
    }

}
