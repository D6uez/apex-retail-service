package com.apexretail.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.apexretail.domain.Product;
import com.apexretail.repository.InventoryFileRepository;

/**
 * Service for managing product inventory operations.
 * 
 * <p>
 * This service provides business operations for inventory management,
 * including selling products and restocking inventory. All operations
 * validate their inputs before execution. Inventory data is loaded from
 * and saved to persistent storage via a repository.
 *
 * <p>
 * Example:
 * 
 * <pre>{@code
 * InventoryFileRepository repo = new InventoryFileRepository();
 * InventoryService inventory = new InventoryService(repo);
 * inventory.restockProductByID(101L, 5); // Add 5 units to product with ID 101
 * inventory.sellProductByID(101L, 2); // Sell 2 units
 * inventory.saveInventory(); // Persist changes
 * }</pre>
 *
 * @author David
 * @version 1.0.0
 */
public class InventoryService {
    private InventoryFileRepository repository;
    private final String FILE_NAME = "inventoryFile.csv";
    private List<Product> inventory = new ArrayList<Product>();

    /**
     * Constructs an InventoryService with the given repository.
     * Loads inventory from file; creates default inventory if file is empty.
     * 
     * @param repo the repository for file operations
     */
    public InventoryService(InventoryFileRepository repo) {
        this.repository = repo;
        try {
            inventory = repository.loadInventory(FILE_NAME);
            if (inventory.isEmpty()) {
                createDefaultInventory();
            }
        } catch (IOException e) {
            System.out.println("Critical error loading inventory. Starting with empty inventory.");
        }
    }

    /**
     * Returns an unmodifiable view of the current inventory.
     * 
     * @return immutable copy of the inventory list
     */
    public List<Product> getReadOnlyInventory() {
        return List.copyOf(inventory);
    }

    /**
     * Sells a specified quantity of a product identified by its ID, reducing its
     * stock.
     * 
     * <p>
     * Validates the ID and amount, finds the product, and calls its decreaseStock
     * method.
     * This operation is atomic and will only complete if sufficient stock is
     * available.
     *
     * @param id     product identifier (must not be null and must exist)
     * @param amount quantity to sell (must be > 0)
     * @throws IllegalArgumentException if id is null or not found, or amount is
     *                                  invalid
     * @throws IllegalArgumentException if insufficient stock is available
     * @see Product#decreaseStock(int)
     */
    public void sellProductByID(long id, int amount) {
        validateStockAdjustment(amount);

        Product product = findProductByID(id);
        product.decreaseStock(amount);
    }

    /**
     * Restocks a product identified by its ID, adding the specified quantity to
     * inventory.
     * 
     * <p>
     * Validates the ID and amount, finds the product, and calls its increaseStock
     * method.
     *
     * @param id     product identifier (must not be null and must exist)
     * @param amount quantity to add (must be > 0)
     * @throws IllegalArgumentException if id is null or not found, or amount is
     *                                  invalid
     * @see Product#increaseStock(int)
     */
    public void restockProductByID(long id, int amount) {
        validateStockAdjustment(amount);

        Product product = findProductByID(id);
        product.increaseStock(amount);
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
     * Finds a product by its ID.
     * 
     * @param id product ID to search for
     * @return the Product with matching ID
     * @throws IllegalArgumentException if no product with given ID exists
     */
    private Product findProductByID(long id) {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getId() == id) {
                return inventory.get(i);
            }
        }
        throw new IllegalArgumentException("Product not found");
    }

    /**
     * Saves the current inventory to persistent storage using the repository.
     */
    public void saveInventory() {
        try {
            repository.writeFile(FILE_NAME, inventory);
            System.out.println("Inventory saved successfully.");
        } catch (Exception e) {
            System.out.println("Saving inventory failed: " + e.getMessage());
        }
    }

    /**
     * Creates a default inventory with sample products.
     */
    private void createDefaultInventory() {
        inventory.add(new Product(1, "Tomato", BigDecimal.valueOf(0.25), 30, "Produce"));
        inventory.add(new Product(2, "Onion", BigDecimal.valueOf(0.90), 20, "Produce"));
        inventory.add(new Product(3, "Milk", BigDecimal.valueOf(2.46), 15, "Dairy"));
        inventory.add(new Product(4, "Cheese", BigDecimal.valueOf(3.15), 10, "Dairy"));
    }

    public Product getProductByID(long productChoice) {
        return findProductByID(productChoice);
    }
}