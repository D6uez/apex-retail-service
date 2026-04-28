package com.apexretail.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import com.apexretail.domain.Sale;
import com.apexretail.domain.SaleItem;

/**
 * SQL Server implementation of {@link SaleRepository}.
 * Provides methods to persist sale headers, sale items, and to update product
 * stock.
 * All methods assume a valid connection is provided (typically part of a
 * transaction).
 *
 * @author David
 * @version 1.0.0
 */
public class MSSQLSaleRepository implements SaleRepository {

    /**
     * Inserts a sale header into the database and assigns the generated ID to the
     * sale object.
     *
     * @param conn the database connection (must be open and in a transaction if
     *             needed)
     * @param sale the sale to insert (must not be null)
     * @return the generated sale ID
     * @throws RuntimeException if the insert fails or no ID is generated
     */
    @Override
    public long insertSale(Connection conn, Sale sale) {
        String sqlStatement = "INSERT INTO Sale (sale_date, total_price) VALUES (?, ?);";

        try (PreparedStatement stmt = conn.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setTimestamp(1, Timestamp.valueOf(sale.getSaleDate()));
            stmt.setBigDecimal(2, sale.getTotalPrice());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException("Insert failed, no rows affected.");
            }

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    long generatedId = keys.getLong(1);
                    sale.setId(generatedId);
                    return generatedId;
                } else {
                    throw new RuntimeException("Insert succeeded but no ID returned.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert Sale: " + sale.toString(), e);
        }
    }

    /**
     * Inserts a batch of sale items for a given sale using batch execution.
     *
     * @param conn   the database connection
     * @param saleId the ID of the sale to which the items belong
     * @param items  the list of sale items to insert (must not be empty)
     * @throws RuntimeException if any item fails to insert
     */
    @Override
    public void insertSaleItems(Connection conn, long saleId, List<SaleItem> items) {
        String sqlStatement = "INSERT INTO SaleItem (sale_id, product_id, quantity, unit_price, subtotal) VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement stmt = conn.prepareStatement(sqlStatement)) {
            for (SaleItem currentItem : items) {

                stmt.setLong(1, saleId);
                stmt.setLong(2, currentItem.getProductId());
                stmt.setInt(3, currentItem.getQuantity());
                stmt.setBigDecimal(4, currentItem.getUnitPrice());
                stmt.setBigDecimal(5, currentItem.getSubtotal());

                stmt.addBatch();
            }

            int[] results = stmt.executeBatch();

            for (int result : results) {
                if (result == 0) {
                    throw new RuntimeException("Insert failed for one or more SaleItems.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert SaleItems", e);
        }
    }

    /**
     * Updates product stock quantities by subtracting the sold amounts.
     * Uses batch execution and includes a condition to ensure sufficient stock
     * (prevents negative inventory).
     *
     * @param conn  the database connection
     * @param items the list of sale items containing quantities to deduct
     * @throws RuntimeException if any product update fails (e.g., insufficient
     *                          stock)
     */
    @Override
    public void updateStock(Connection conn, List<SaleItem> items) {
        String sqlStatement = "UPDATE Product SET quantity_in_stock = quantity_in_stock - ? WHERE id = ? AND quantity_in_stock >= ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sqlStatement)) {
            for (SaleItem currentItem : items) {
                stmt.setInt(1, currentItem.getQuantity());
                stmt.setLong(2, currentItem.getProductId());
                stmt.setInt(3, currentItem.getQuantity());

                stmt.addBatch();
            }

            int[] results = stmt.executeBatch();

            for (int result : results) {
                if (result == 0) {
                    throw new RuntimeException("Update failed for one or more products.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update Products", e);
        }
    }
}