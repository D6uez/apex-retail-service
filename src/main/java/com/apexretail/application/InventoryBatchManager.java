package com.apexretail.application;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    private final static int UI_OFFSET = 1;

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

        Category produceCategory = new Category(1, "Produce", "This category labels produce products.");
        Category dairyCategory = new Category(2, "Dairy", "This category labels dairy products.");

        ArrayList<Product> currentInventory = new ArrayList<Product>();
        currentInventory.add(new Product(1, "Tomato", BigDecimal.valueOf(0.25), 30, produceCategory));
        currentInventory.add(new Product(2, "Onion", BigDecimal.valueOf(0.90), 20, produceCategory));
        currentInventory.add(new Product(3, "Milk", BigDecimal.valueOf(2.46), 15, dairyCategory));
        currentInventory.add(new Product(4, "Cheese", BigDecimal.valueOf(3.15), 10, dairyCategory));

        InventoryService invServiceObj = new InventoryService();
        boolean processRunning = true;

        int sellCount = 0, restockCount = 0, unitsSold = 0, unitsRestocked = 0;

        // Main application loop - continues until user chooses "Exit"
        while (processRunning) {
            System.out.println("Welcome, would you like to process an order? Please choose: Sell, Restock, or Exit");
            String choice = normalizeCommand(keyboard.nextLine());

            // First-level validation: check if choice is valid
            if (isValidChoice(choice)) {
                // Exit branch: terminates the application loop
                if (choice.equals("exit")) {
                    processRunning = false;
                }
                // Sell branch: process product sale
                else if (choice.equals("sell")) {
                    Integer quantity;
                    System.out.println("Processing sell...");
                    System.out.println("Which product would you like to sell? Enter the Product number:");
                    Product validProduct = readProductSelection(keyboard, currentInventory);
                    if (validProduct == null) {
                        System.out.println("Invalid product selection.");
                    } else {
                        System.out.println("How many would you like to sell?");

                        // Validate input is an integer before proceeding
                        quantity = readPositiveInt(keyboard);
                        if (quantity == null) {
                            // Invalid input: not an integer
                            System.out.println("Please enter a valid quantity.");
                            continue; // Return to main menu
                        }
                        // Delegate to service layer - may throw exceptions for business rules
                        invServiceObj.sellProduct(validProduct, quantity);
                        System.out.println("Sold " + quantity + " of " + validProduct.getName() + ".");
                        System.out.println(validProduct.getQuantityInStock() + " remaining in stock.");
                        sellCount++;
                        unitsSold += quantity;

                    }

                }
                // Restock branch: process inventory restocking
                else if (choice.equals("restock")) {
                    Integer quantity;
                    System.out.println("Processing restock...");
                    System.out.println("Which product would you like to restock? Enter the Product number:");
                    Product validProduct = readProductSelection(keyboard, currentInventory);
                    if (validProduct == null) {
                        System.out.println("Invalid product selection.");
                    } else {
                        System.out.println("How many would you like to restock?");

                        // Validate input is an integer before proceeding
                        quantity = readPositiveInt(keyboard);
                        if (quantity == null) {
                            // Invalid input: not an integer
                            System.out.println("Please enter a valid quantity.");
                            continue; // Return to main menu
                        }
                        // Delegate to service layer
                        invServiceObj.restockProduct(validProduct, quantity);
                        System.out.println("Restocked " + quantity + " of " + validProduct.getName() + ".");
                        System.out.println(validProduct.getQuantityInStock() + " remaining in stock.");
                        restockCount++;
                        unitsRestocked += quantity;
                    }
                }

            } else {
                // Invalid menu choice
                System.out.println("Error: Invalid selection. Please choose: Sell, Restock, or Exit");
            }
        }
        keyboard.close();
        System.out.printf(
                "Thank you for using Apex service: Here is a summary of your usage today%nNumber of sell operations: %d"
                        + "%nTotal number of units sold: %d%nNumber of restock operations: %d%nTotal number of units restocked: %d%nHave a nice day! :)",
                sellCount, unitsSold, restockCount, unitsRestocked);

    }

    /**
     * Reads and validates a product selection from the user.
     * 
     * <p>
     * Displays the current inventory list, reads the user's selection,
     * validates that it corresponds to a valid product index, and returns
     * the selected Product object.
     *
     * @param scanner   Scanner object for reading user input
     * @param inventory List of available products
     * @return Selected Product object, or null if selection is invalid
     */
    private static Product readProductSelection(Scanner scanner, ArrayList<Product> inventory) {
        displayInventory(inventory);
        Integer productChoice = readPositiveInt(scanner);
        if (productChoice == null || productChoice < UI_OFFSET || productChoice > inventory.size()) {
            return null;
        }
        return inventory.get(productChoice - UI_OFFSET);
    }

    /**
     * Reads and validates a positive integer from user input.
     * 
     * <p>
     * Reads a line of input and validates that it contains only digits
     * and represents a positive integer value. Returns null if the input
     * is blank, contains non-digit characters, or is not a positive integer.
     *
     * @param scanner Scanner object for reading user input
     * @return Validated positive integer, or null if input is invalid
     */
    private static Integer readPositiveInt(Scanner scanner) {
        String trimmedRawInput = scanner.nextLine().trim();
        if (trimmedRawInput.isBlank()) {
            return null;
        }
        for (int i = 0; i < trimmedRawInput.length(); i++) {
            char nextChar = trimmedRawInput.charAt(i);
            if (!(Character.isDigit(nextChar))) {
                return null;
            }
        }
        Integer posInt = Integer.parseInt(trimmedRawInput);
        if (posInt <= 0) {
            return null;
        }
        return posInt;
    }

    /**
     * Displays the current inventory in a formatted list.
     * 
     * @param currentInventory List of products to display
     */
    private static void displayInventory(ArrayList<Product> currentInventory) {
        for (int i = 0; i < currentInventory.size(); i++) {
            System.out.printf("No: %d\tProduct: %s\tStock: %d%n", i + UI_OFFSET, currentInventory.get(i).getName(),
                    currentInventory.get(i).getQuantityInStock());
        }
    }

    /**
     * Normalizes user input for consistent processing.
     * 
     * <p>
     * Trims whitespace and converts input to lowercase for case-insensitive
     * command matching.
     *
     * @param input Raw user input string
     * @return Normalized command string
     */
    private static String normalizeCommand(String input) {
        if (input == null || input.isBlank()) {
            return "";
        }
        return input.trim().toLowerCase();
    }

    /**
     * Validates that the user's input matches one of the allowed commands.
     * 
     * @param choice user input string to validate
     * @return true if the input matches a valid command, false otherwise
     */
    private static boolean isValidChoice(String choice) {
        return choice.equals("sell") ||
                choice.equals("restock") ||
                choice.equals("exit");
    }
}