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

        // Main application loop - continues until user chooses "Exit"
        while (processRunning) {
            System.out.println("Welcome, would you like to process an order? Please choose: Sell, Restock, or Exit");
            String choice = keyboard.nextLine();

            // First-level validation: check if choice is valid
            if (isValidChoice(choice)) {
                // Exit branch: terminates the application loop
                if (choice.equalsIgnoreCase("Exit")) {
                    System.out.println("Goodbye!");
                    processRunning = false;
                }
                // Sell branch: process product sale
                else if (choice.equalsIgnoreCase("Sell")) {
                    int quantity;
                    System.out.println("Processing sell...");
                    System.out.println("How many would you like to sell?");

                    // Validate input is an integer before proceeding
                    if (keyboard.hasNextInt()) {
                        quantity = keyboard.nextInt();
                        keyboard.nextLine(); // Clear the newline character

                        // Second-level validation: check if quantity is positive
                        if (isValidStockAdjustment(quantity)) {
                            // Delegate to service layer - may throw exceptions for business rules
                            invServiceObj.sellProduct(validProduct, quantity);
                            System.out.println("Sold " + quantity + " of " + validProduct.getName() + ".");
                            System.out.println(validProduct.getQuantityInStock() + " remaining in stock.");
                        } else {
                            // Invalid quantity: not positive
                            System.out.println("Error: Quantity must be greater than 0.");
                        }
                    } else {
                        // Invalid input: not an integer
                        System.out.println("Please enter a valid number.");
                        keyboard.nextLine(); // Clear invalid input
                        continue; // Return to main menu
                    }
                }
                // Restock branch: process inventory restocking
                else if (choice.equalsIgnoreCase("Restock")) {
                    int quantity;
                    System.out.println("Processing restock...");
                    System.out.println("How many would you like to restock?");

                    // Validate input is an integer before proceeding
                    if (keyboard.hasNextInt()) {
                        quantity = keyboard.nextInt();
                        keyboard.nextLine(); // Clear the newline character

                        // Second-level validation: check if quantity is positive
                        if (isValidStockAdjustment(quantity)) {
                            // Delegate to service layer
                            invServiceObj.restockProduct(validProduct, quantity);
                            System.out.println("Restocked " + quantity + " of " + validProduct.getName() + ".");
                            System.out.println(validProduct.getQuantityInStock() + " remaining in stock.");
                        } else {
                            // Invalid quantity: not positive
                            System.out.println("Error: Quantity must be greater than 0.");
                        }
                    } else {
                        // Invalid input: not an integer
                        System.out.println("Please enter a valid number.");
                        keyboard.nextLine(); // Clear invalid input
                        continue; // Return to main menu
                    }
                }
            } else {
                // Invalid menu choice
                System.out.println("Error: Invalid selection. Please choose: Sell, Restock, or Exit");
            }
        }
    }

    /**
     * Validates that the user's input matches one of the allowed commands.
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
     * @param amount quantity to validate
     * @return true if amount is greater than 0, false otherwise
     */
    private static boolean isValidStockAdjustment(int amount) {
        return amount > 0;
    }
}