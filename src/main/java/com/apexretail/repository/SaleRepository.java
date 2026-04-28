package com.apexretail.repository;

import java.sql.Connection;
import java.util.List;

import com.apexretail.domain.Sale;
import com.apexretail.domain.SaleItem;

/**
 * Repository interface for sale-related database operations.
 * Methods expect an active database connection (typically managed by the
 * caller).
 *
 * @author David
 * @version 1.0.0
 */
public interface SaleRepository {

    /**
     * Inserts a sale record into the database.
     * The generated ID is set on the provided Sale object.
     *
     * @param conn the database connection
     * @param sale the sale to insert
     * @return the generated sale ID
     */
    long insertSale(Connection conn, Sale sale);

    /**
     * Inserts a batch of sale items for a given sale.
     *
     * @param conn   the database connection
     * @param saleId the ID of the parent sale
     * @param items  list of sale items to insert
     */
    void insertSaleItems(Connection conn, long saleId, List<SaleItem> items);

    /**
     * Updates product stock levels by subtracting the quantities sold.
     *
     * @param conn  the database connection
     * @param items list of sale items containing quantities to deduct
     */
    void updateStock(Connection conn, List<SaleItem> items);
}