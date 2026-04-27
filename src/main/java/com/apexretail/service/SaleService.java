package com.apexretail.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.apexretail.domain.Cart;
import com.apexretail.domain.CartItem;
import com.apexretail.domain.Product;
import com.apexretail.domain.Sale;
import com.apexretail.repository.InventoryRepository;
import com.apexretail.repository.SaleRepository;

public class SaleService {

    private InventoryRepository inventoryRepository;
    private SaleRepository saleRepository;

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

        // 6. Recalculate totals
        // 7. Open DB Connection
        // 8. Begin Transaction
        // 9. Use Sale Repo to persist in order
        // 9A. Insert Sale
        // 9B. Insert SaleItem, use Sale ID generated above as FK
        // 9C. Update Inventory
        // 10. Commit if successful
        // 11. Rollback if any failure
        // 12. Close Connection

        return currentSale;

    }

    private void validateCart(Cart cart) {
        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty.");
        }
    }

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

    private void validateStockRequest(List<Product> requestedProducts, Cart cart) {
        for (CartItem item : cart.getItems()) {
            if (item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0");
            }

            Product matchedProduct = null;

            for (Product product : requestedProducts) {
                if (item.getProductID() == product.getId()) {
                    matchedProduct = product;
                    break;
                }
            }

            if (matchedProduct == null) {
                throw new IllegalArgumentException("Product not found for id: " + item.getProductID());
            }

            if (item.getQuantity() > matchedProduct.getQuantityInStock()) {
                throw new IllegalArgumentException("Insufficient stock for product id: " + item.getProductID());
            }
        }
    }
}
