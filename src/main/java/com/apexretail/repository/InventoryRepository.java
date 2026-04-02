package com.apexretail.repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.apexretail.domain.Product;

/**
 * Repository interface for inventory data access.
 * Provides basic CRUD operations for Product objects.
 *
 * @author David
 * @version 1.0.0
 */
public interface InventoryRepository {

    /**
     * Returns a list of all products in the inventory.
     *
     * @return list of all products
     * @throws IOException if an error occurs during data access
     */
    public List<Product> findAll() throws IOException;

    /**
     * Finds a product by its unique ID.
     *
     * @param id the product ID to search for
     * @return an Optional containing the product if found, empty otherwise
     */
    public Optional<Product> findById(long id);

    /**
     * Saves a product (adds if new, updates if existing).
     *
     * @param product the product to save
     */
    public void save(Product product);
}