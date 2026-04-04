package com.apexretail.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.apexretail.config.DatabaseConnection;
import com.apexretail.domain.Product;

/**
 * SQL-based implementation of {@link InventoryRepository}.
 * Uses a database with tables Product and Category.
 * Category is referenced by name, automatically resolved to its ID.
 *
 * @author David
 * @version 1.0.0
 */
public class SqlInventoryRepository implements InventoryRepository {

    /**
     * Retrieves all products from the database, joined with their category names.
     *
     * @return list of all products
     * @throws RuntimeException if a database error occurs
     */
    @Override
    public List<Product> findAll() {
        List<Product> inventory = new ArrayList<>();

        String sqlStatement = "SELECT p.id, p.name, p.price, p.quantity_in_stock, c.name as category_name FROM Product p LEFT JOIN Category c ON p.category_id = c.id;";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet result = stmt.executeQuery(sqlStatement)) {

            while (result.next()) {
                Product product = new Product(result.getLong("id"), result.getString("name"),
                        result.getBigDecimal("price"), result.getInt("quantity_in_stock"),
                        result.getString("category_name"));

                inventory.add(product);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch products", e);
        }

        return inventory;
    }

    /**
     * Finds a product by its ID, including its category name.
     *
     * @param id the product ID to search for
     * @return an Optional containing the product if found, empty otherwise
     * @throws RuntimeException if a database error occurs
     */
    @Override
    public Optional<Product> findById(long id) {
        String sqlStatement = "SELECT p.id, p.name, p.price, p.quantity_in_stock, c.name as category_name FROM Product p LEFT JOIN Category c ON p.category_id = c.id WHERE p.id = ?;";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sqlStatement)) {

            stmt.setLong(1, id);

            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    Product product = new Product(result.getLong("id"), result.getString("name"),
                            result.getBigDecimal("price"), result.getInt("quantity_in_stock"),
                            result.getString("category_name"));

                    return Optional.of(product);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch product by id: " + id, e);
        }
        return Optional.empty();
    }

    /**
     * Saves a product (inserts if ID is 0, otherwise updates).
     * After insert, the generated ID is set on the product object.
     *
     * @param product the product to save
     * @throws RuntimeException if the save operation fails
     */
    @Override
    public void save(Product product) {
        if (product.getId() == 0) {
            insert(product);
        } else {
            update(product);
        }
    }

    /**
     * Inserts a new product into the database.
     * The product's ID must be 0; after insertion, the generated ID is assigned.
     *
     * @param product the product to insert
     * @throws RuntimeException if the insert fails or no ID is generated
     */
    private void insert(Product product) {
        String sqlStatement = "INSERT INTO Product (name, price, quantity_in_stock, category_id) VALUES (?, ?, ?, ?);";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS)) {

            long categoryId = getCategoryIdByName(product.getCategory());

            stmt.setString(1, product.getName());
            stmt.setBigDecimal(2, product.getPrice());
            stmt.setInt(3, product.getQuantityInStock());
            stmt.setLong(4, categoryId);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException("Insert failed, no rows affected.");
            }

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    long generatedId = keys.getLong(1);
                    product.setId(generatedId);
                } else {
                    throw new RuntimeException("Insert succeeded but no ID returned.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert product: " + product.getName(), e);
        }
    }

    /**
     * Updates an existing product in the database.
     * The product must have a non-zero ID.
     *
     * @param product the product to update
     * @throws RuntimeException if no product with that ID exists or update fails
     */
    private void update(Product product) {
        String sqlStatement = "UPDATE Product SET name = ?, price = ?, quantity_in_stock = ?, category_id = ? WHERE id = ?;";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sqlStatement)) {

            long categoryId = getCategoryIdByName(product.getCategory());

            stmt.setString(1, product.getName());
            stmt.setBigDecimal(2, product.getPrice());
            stmt.setInt(3, product.getQuantityInStock());
            stmt.setLong(4, categoryId);
            stmt.setLong(5, product.getId());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException(
                        "Update failed. No product found with id: " + product.getId());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update product with id: " + product.getId(), e);
        }
    }

    /**
     * Retrieves the category ID for a given category name.
     * The name is trimmed before lookup.
     *
     * @param categoryName the name of the category
     * @return the category ID
     * @throws RuntimeException if the category is not found or a database error
     *                          occurs
     */
    private long getCategoryIdByName(String categoryName) {
        String sqlStatement = "SELECT id FROM Category WHERE name = ?;";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sqlStatement)) {

            stmt.setString(1, categoryName.trim());

            try (ResultSet result = stmt.executeQuery()) {

                if (result.next()) {
                    return result.getLong("id");
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find Category ID for : " + categoryName, e);
        }

        throw new RuntimeException("Category not found: " + categoryName);
    }
}