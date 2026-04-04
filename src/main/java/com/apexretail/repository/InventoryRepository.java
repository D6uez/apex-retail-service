package com.apexretail.repository;

import java.util.List;
import java.util.Optional;

import com.apexretail.domain.Product;

/**
 * Repository interface for inventory data access.
 * Provides basic CRUD operations for Product objects.
 * <p>
 * Implementations may handle persistence details such as file I/O or database
 * operations. Exceptions are handled internally or thrown as unchecked runtime
 * exceptions where appropriate.
 *
 * @author David
 * @version 1.0.0
 */
public interface InventoryRepository {

    /**
     * Returns a list of all products in the inventory.
     *
     * @return list of all products
     */
    List<Product> findAll();

    /**
     * Finds a product by its unique ID.
     *
     * @param id the product ID to search for
     * @return an Optional containing the product if found, empty otherwise
     */
    Optional<Product> findById(long id);

    /**
     * Saves a product (adds if new, updates if existing).
     *
     * @param product the product to save
     */
    void save(Product product);
}