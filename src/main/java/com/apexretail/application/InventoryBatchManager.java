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
     * Initializes a sample inventory and runs an interactive loop that
     * prompts users for inventory operations. Validates all user inputs
     * before performing operations and provides appropriate feedback.
     * Uses consolidated counters array to track transaction metrics.
     *
     * <p>
     * Counters array structure:
     * <ul>
     * <li>index 0: sell operation count</li>
     * <li>index 1: total units sold</li>
     * <li>index 2: restock operation count</li>
     * <li>index 3: total units restocked</li>
     * </ul>
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

        // Consolidated transaction counters array [sellCount, unitsSold, restockCount,
        // unitsRestocked]
        int[] counters = new int[4];

        // Main application loop - continues until user chooses "Exit"
        while (processRunning) {
            System.out.println("Welcome, would you like to process an order? Please choose: Sell, Restock, or Exit");
            String choice = normalizeCommand(keyboard.nextLine());

            // First-level validation: check if choice is valid
            if (!isValidChoice(choice)) {
                System.out.println("Error: Invalid selection. Please choose: Sell, Restock, or Exit");
                continue; // Return to start of loop for new input
            }

            // Exit branch: terminates the application loop
            if (choice.equals("exit")) {
                processRunning = false;
            }
            // Sell branch: process product sale
            else if (choice.equals("sell")) {
                processInventoryAction(keyboard, currentInventory, invServiceObj, choice, counters);
            }
            // Restock branch: process inventory restocking
            else if (choice.equals("restock")) {
                processInventoryAction(keyboard, currentInventory, invServiceObj, choice, counters);
            }
        }
        keyboard.close();
        System.out.printf(
                "Thank you for using Apex service: Here is a summary of your usage today%nNumber of sell operations: %d"
                        + "%nTotal number of units sold: %d%nNumber of restock operations: %d%nTotal number of units restocked: %d%nHave a nice day! :)",
                counters[0], counters[1], counters[2], counters[3]);

    }

    /**
     * Processes an inventory transaction (sell or restock) based on user input.
     * 
     * <p>
     * This method consolidates the common workflow for both sell and restock
     * operations,
     * including product selection, quantity validation, service layer delegation,
     * and transaction tracking. The action parameter determines which service
     * method
     * to call and which counters to update.
     *
     * <p>
     * The counters array is updated as follows based on the action:
     * <ul>
     * <li>For "sell": counters[0] (sell count) and counters[1] (units sold) are
     * updated</li>
     * <li>For "restock": counters[2] (restock count) and counters[3] (units
     * restocked) are updated</li>
     * </ul>
     *
     * @param keyboard  Scanner for reading user input
     * @param inventory List of available products
     * @param service   InventoryService instance for business logic operations
     * @param action    The transaction type ("sell" or "restock")
     * @param counters  Array containing transaction counters [sellCount, unitsSold,
     *                  restockCount, unitsRestocked]
     */
    private static void processInventoryAction(Scanner keyboard, ArrayList<Product> inventory, InventoryService service,
            String action, int[] counters) {
        Product validProduct = readProductSelection(keyboard, inventory);
        if (validProduct == null) {
            System.out.println("Invalid product selection.");
            return; // Return to main menu for new selection
        }
        System.out.printf("How many would you like to %s?%n", action);
        Integer quantity = readPositiveInt(keyboard);
        if (quantity == null) {
            System.out.println("Please enter a valid quantity.");
            return;
        }
        if ("sell".equals(action)) {
            service.sellProduct(validProduct, quantity);
            counters[0]++;
            counters[1] += quantity;
        } else {
            service.restockProduct(validProduct, quantity);
            counters[2]++;
            counters[3] += quantity;
        }
        System.out.printf("%s %d of %s.%n%d remaining in stock.%n",
                action.substring(0, 1).toUpperCase() + action.substring(1),
                quantity, validProduct.getName(), validProduct.getQuantityInStock());

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