package com.apexretail.application;

import java.math.BigDecimal;
import java.util.Scanner;

import com.apexretail.domain.Category;
import com.apexretail.domain.Product;
import com.apexretail.service.InventoryService;

/**
 * Interactive command-line inventory management application.
 * 
 * <p>
 * This application provides a user interface for processing inventory
 * transactions through a simple menu-driven interface. Users can sell
 * or restock products, with all operations validated and managed through
 * the service layer.
 *
 * <p>
 * Example interaction:
 * 
 * <pre>{@code
 * Welcome, would you like to process an order? Please choose: Sell, Restock, or Exit
 * Sell
 * Processing sell...
 * How many would you like to sell?
 * 5
 * (Product sold successfully)
 * }</pre>
 *
 * @author David
 * @version 1.0.0
 */
public class InventoryBatchManager {

    /**
     * Main entry point for the inventory batch management application.
     * 
     * <p>
     * Initializes a sample product and runs an interactive loop that
     * prompts users for inventory operations. Validates all user inputs
     * before performing operations and provides appropriate feedback.
     *
     * @param args command-line arguments (not used in this application)
     */
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        Category validCategory = new Category(1, "Produce", "Valid Produce");
        Product validProduct = new Product(1, "TestItem", BigDecimal.valueOf(5.00), 20, validCategory);
        InventoryService invServiceObj = new InventoryService();
        boolean processRunning = true;

        while (processRunning) {
            System.out.println("Welcome, would you like to process an order? Please choose: Sell, Restock, or Exit");
            String choice = keyboard.nextLine();

            if (isValidChoice(choice)) {
                if (choice.equalsIgnoreCase("Exit")) {
                    System.out.println("Goodbye!");
                    processRunning = false;
                } else if (choice.equalsIgnoreCase("Sell")) {
                    System.out.println("Processing sell...");
                    System.out.println("How many would you like to sell?");
                    int quantity = keyboard.nextInt();
                    if (isValidStockAdjustment(quantity)) {
                        invServiceObj.sellProduct(validProduct, quantity);
                    } else {
                        System.out.println("Error: Quantity must be greater than 0.");
                    }
                } else if (choice.equalsIgnoreCase("Restock")) {
                    System.out.println("Processing restock...");
                    // TODO: Implement restock functionality
                }
            } else {
                System.out.println("Error: Invalid selection. Please choose: Sell, Restock, or Exit");
            }
        }
    }

    /**
     * Validates that the user's input matches one of the allowed commands.
     * 
     * <p>
     * Comparison is case-insensitive. Valid commands are:
     * <ul>
     * <li>"Sell" - Process a product sale</li>
     * <li>"Restock" - Restock a product</li>
     * <li>"Exit" - Terminate the application</li>
     * </ul>
     *
     * @param choice user input string to validate
     * @return true if the input matches a valid command, false otherwise
     */
    private static boolean isValidChoice(String choice) {
        return choice.equalsIgnoreCase("Sell") ||
                choice.equalsIgnoreCase("Restock") ||
                choice.equalsIgnoreCase("Exit");
    }

    /**
     * Validates that a stock adjustment quantity is positive.
     * 
     * <p>
     * This validation is performed at the application layer before
     * passing the quantity to the service layer. Note that additional
     * validation (e.g., checking stock availability) may still occur
     * in the service layer.
     *
     * @param amount quantity to validate
     * @return true if amount is greater than 0, false otherwise
     */
    private static boolean isValidStockAdjustment(int amount) {
        return amount > 0;
    }
}