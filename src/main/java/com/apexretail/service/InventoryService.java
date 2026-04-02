package com.apexretail.service;

import java.io.IOException;
import java.util.List;

import com.apexretail.domain.Product;
import com.apexretail.repository.InventoryRepository;

/**
 * Service for managing product inventory operations.
 * 
 * <p>
 * This service provides business operations for inventory management,
 * including selling products and restocking inventory. All operations
 * validate their inputs before execution. After each successful operation,
 * the modified product is automatically persisted via the repository.
 *
 * <p>
 * Example:
 * 
 * <pre>{@code
 * InventoryRepository repo = new InventoryFileRepository();
 * InventoryService service = new InventoryService(repo);
 * String message = service.restockProductByID(101L, 5);
 * System.out.println(message);
 * message = service.sellProductByID(101L, 2);
 * System.out.println(message);
 * }</pre>
 *
 * @author David
 * @version 1.0.0
 */
public class InventoryService {
    private InventoryRepository repository;

    /**
     * Constructs an InventoryService with the given repository.
     * The repository is expected to handle its own initialization
     * (loading or creating default inventory).
     * 
     * @param repo the repository for data access
     */
    public InventoryService(InventoryRepository repo) {
        this.repository = repo;
    }

    /**
     * Returns an unmodifiable view of the current inventory.
     * 
     * @return immutable copy of the inventory list
     * @throws IOException if an error occurs while reading from the repository
     */
    public List<Product> getReadOnlyInventory() throws IOException {
        return repository.findAll();
    }

    /**
     * Sells a specified quantity of a product identified by its ID, reducing its
     * stock.
     * 
     * <p>
     * Validates the amount, finds the product by ID, calls its decreaseStock
     * method, and persists the updated product. This operation is atomic and
     * will only complete if sufficient stock is available.
     *
     * @param id     product identifier (must exist)
     * @param amount quantity to sell (must be > 0)
     * @return a success message describing the completed transaction
     * @throws IllegalArgumentException if amount is invalid, product not found,
     *                                  or insufficient stock available
     * @see Product#decreaseStock(int)
     */
    public String sellProductByID(long id, int amount) {
        validateStockAdjustment(amount);

        Product product = findProductByID(id);
        product.decreaseStock(amount);
        repository.save(product);

        return String.format(
                "Successfully sold %d units of %s.",
                amount,
                product.getName());
    }

    /**
     * Restocks a product identified by its ID, adding the specified quantity to
     * inventory.
     * 
     * <p>
     * Validates the amount, finds the product by ID, calls its increaseStock
     * method, and persists the updated product.
     *
     * @param id     product identifier (must exist)
     * @param amount quantity to add (must be > 0)
     * @return a success message describing the completed transaction
     * @throws IllegalArgumentException if amount is invalid or product not found
     * @see Product#increaseStock(int)
     */
    public String restockProductByID(long id, int amount) {
        validateStockAdjustment(amount);

        Product product = findProductByID(id);
        product.increaseStock(amount);
        repository.save(product);

        return String.format(
                "Successfully restocked %d units of %s.",
                amount,
                product.getName());
    }

    /**
     * Validates that a stock adjustment amount is positive.
     * 
     * @param amount quantity to validate
     * @throws IllegalArgumentException if amount is not positive
     */
    private void validateStockAdjustment(int amount) {
        if (!(amount > 0)) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }
    }

    /**
     * Finds a product by its ID using the repository.
     * 
     * @param id product ID to search for
     * @return the Product with matching ID
     * @throws IllegalArgumentException if no product with given ID exists
     */
    private Product findProductByID(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
    }

    /**
     * Retrieves a product by its ID.
     * 
     * @param productId the product ID to look up
     * @return the Product with the given ID
     * @throws IllegalArgumentException if no product with that ID exists
     */
    public Product getProductByID(long productId) {
        return findProductByID(productId);
    }
}