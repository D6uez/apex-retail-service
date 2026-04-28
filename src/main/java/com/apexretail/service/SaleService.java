package com.apexretail.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.apexretail.config.DatabaseConnection;
import com.apexretail.domain.Cart;
import com.apexretail.domain.CartItem;
import com.apexretail.domain.Product;
import com.apexretail.domain.Sale;
import com.apexretail.domain.SaleItem;
import com.apexretail.repository.InventoryRepository;
import com.apexretail.repository.SaleRepository;

/**
 * Service responsible for processing a customer's cart into a completed sale.
 * Handles validation, product lookup, stock checking, creation of Sale/SaleItem
 * objects,
 * and transactional persistence of the sale and stock updates.
 *
 * @author David
 * @version 1.0.0
 */
public class SaleService {

    private InventoryRepository inventoryRepository;
    private SaleRepository saleRepository;

    /**
     * Processes the given cart into a completed sale within a database transaction.
     * Steps include validation, product loading, stock verification, sale object
     * creation,
     * persistence of sale header and items, and stock reduction.
     *
     * @param cart the customer's cart containing items to purchase
     * @return the completed Sale object with generated ID and totals
     * @throws RuntimeException         if any step fails (transaction is rolled
     *                                  back)
     * @throws IllegalArgumentException if the cart is empty or quantities are
     *                                  invalid
     */
    public Sale processSale(Cart cart) {
        // 1. Validate the request
        validateCart(cart);
        // 2. Fetch products via inventory repo using the cart argument getItems
        List<Product> requestedProducts = loadProductsFromCart(cart);
        // 3. Validate Stock
        validateStockRequest(requestedProducts, cart);
        // 4. Create Sale Object
        Sale currentSale = new Sale(LocalDateTime.now());
        // 5. Create SaleItem Objects
        // 5A. Capture current item price at the time of sale in SaleItem
        loadProductsToSale(currentSale, requestedProducts, cart);
        // 6. Recalculate totals, done in Sale everytime a SaleItem is added
        // 7. Open DB Connection
        Connection conn = null;
        // 8. Begin Transaction
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            // 9. Use Sale Repo to persist in order
            // 9A. Insert Sale
            long saleId = saleRepository.insertSale(conn, currentSale);
            // 9B. Insert SaleItem, use Sale ID generated above as FK
            saleRepository.insertSaleItems(conn, saleId, currentSale.getItems());
            // 9C. Update Inventory
            saleRepository.updateStock(conn, currentSale.getItems());
            // 10. Commit if successful
            conn.commit();
        }
        // 11. Rollback if any failure
        catch (Exception e) {

            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    throw new RuntimeException("Transaction failed", e1);
                }
            }

            throw new RuntimeException("Transaction failed", e);

        }
        // 12. Close Connection
        finally {

            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException("Failed to close connection.", e);
                }
            }
        }
        return currentSale;
    }

    /**
     * Validates that the cart is not empty.
     *
     * @param cart the cart to validate
     * @throws IllegalArgumentException if the cart's item list is empty
     */
    private void validateCart(Cart cart) {
        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty.");
        }
    }

    /**
     * Loads the full Product objects for each item in the cart.
     * Performs a lookup in the inventory repository; throws if any product ID is
     * missing.
     *
     * @param cart the cart containing product IDs and quantities
     * @return a list of Product objects corresponding to the cart items
     * @throws IllegalArgumentException if any product ID is not found
     */
    private List<Product> loadProductsFromCart(Cart cart) {
        List<Product> mappedProducts = new ArrayList<>();
        // Loop through cart Items
        for (CartItem item : cart.getItems()) {
            // Find product by Id
            Product product = inventoryRepository
                    .findById(item.getProductID())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Product not found with id: " + item.getProductID()));

            mappedProducts.add(product);
        }
        // Return List of products
        return mappedProducts;
    }

    /**
     * Validates that requested quantities do not exceed available stock.
     *
     * @param requestedProducts the products loaded from the repository
     * @param cart              the original cart items
     * @throws IllegalArgumentException if any quantity is non-positive or exceeds
     *                                  stock
     */
    private void validateStockRequest(List<Product> requestedProducts, Cart cart) {
        for (CartItem item : cart.getItems()) {
            if (item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0");
            }

            Product matchedProduct = findMatchingProduct(requestedProducts, item.getProductID());

            if (item.getQuantity() > matchedProduct.getQuantityInStock()) {
                throw new IllegalArgumentException("Insufficient stock for product id: " + item.getProductID());
            }
        }
    }

    /**
     * Creates SaleItem objects from the cart and adds them to the Sale.
     * Captures the product's current price at the time of sale.
     *
     * @param currentSale       the sale to populate
     * @param requestedProducts the product objects corresponding to cart items
     * @param cart              the cart containing quantities
     */
    private void loadProductsToSale(Sale currentSale, List<Product> requestedProducts, Cart cart) {
        for (CartItem item : cart.getItems()) {

            Product matchedProduct = findMatchingProduct(requestedProducts, item.getProductID());

            SaleItem currentSaleItem = new SaleItem(matchedProduct, item.getQuantity(), matchedProduct.getPrice());

            currentSale.addItem(currentSaleItem);
        }
    }

    /**
     * Finds a product in the list by its ID.
     *
     * @param products  list of products
     * @param productID the ID to search for
     * @return the matching Product
     * @throws IllegalArgumentException if not found
     */
    private Product findMatchingProduct(List<Product> products, long productID) {
        for (Product product : products) {
            if (product.getId() == productID) {
                return product;
            }
        }
        throw new IllegalArgumentException("Product not found for id: " + productID);
    }
}