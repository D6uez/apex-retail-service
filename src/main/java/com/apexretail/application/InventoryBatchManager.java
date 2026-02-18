package com.apexretail.application;

import java.util.List;
import java.util.Scanner;

import com.apexretail.domain.Product;
import com.apexretail.repository.InventoryFileRepository;
import com.apexretail.service.InventoryService;

/**
 * Interactive command-line inventory management application.
 * 
 * <p>
 * Provides a menu-driven interface for sell and restock operations.
 * Inventory is loaded from persistent storage at startup and saved on exit,
 * with business logic delegated to the service layer.
 *
 * @author David
 * @version 1.0.0
 */
public class InventoryBatchManager {

    /**
     * Main entry point.
     * 
     * <p>
     * Initializes the service (which loads inventory from file or creates default),
     * then runs an interactive loop until the user chooses Exit.
     * Tracks transaction counts and displays a summary on exit.
     *
     * <p>
     * Counters array:
     * <ul>
     * <li>index 0 – sell operation count</li>
     * <li>index 1 – total units sold</li>
     * <li>index 2 – restock operation count</li>
     * <li>index 3 – total units restocked</li>
     * </ul>
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        boolean processRunning = true;
        InventoryService invServiceObj = new InventoryService(new InventoryFileRepository());

        // [sellCount, unitsSold, restockCount, unitsRestocked]
        int[] counters = new int[4];

        while (processRunning) {
            System.out.println("Welcome, would you like to process an order? Please choose: Sell, Restock, or Exit");
            String choice = normalizeCommand(keyboard.nextLine());

            if (!isValidChoice(choice)) {
                System.out.println("Error: Invalid selection. Please choose: Sell, Restock, or Exit");
                continue;
            }

            if (choice.equals("exit")) {
                invServiceObj.saveInventory();
                processRunning = false;
            } else if (choice.equals("sell")) {
                processInventoryAction(keyboard, invServiceObj, choice, counters);
            } else if (choice.equals("restock")) {
                processInventoryAction(keyboard, invServiceObj, choice, counters);
            }
        }
        keyboard.close();
        System.out.printf(
                "Thank you for using Apex service: Here is a summary of your usage today%n" +
                        "Number of sell operations: %d%nTotal number of units sold: %d%n" +
                        "Number of restock operations: %d%nTotal number of units restocked: %d%n" +
                        "Have a nice day! :)%n",
                counters[0], counters[1], counters[2], counters[3]);
    }

    /**
     * Processes a single inventory transaction (sell or restock).
     * 
     * <p>
     * Handles product selection, quantity input, validation, and delegates
     * to the appropriate service method. Updates the counters array accordingly.
     *
     * @param keyboard  scanner for user input
     * @param inventory read‑only snapshot of current inventory (for display)
     * @param service   service that performs the actual operation
     * @param action    "sell" or "restock"
     * @param counters  transaction counters array
     */
    private static void processInventoryAction(Scanner keyboard, InventoryService service,
            String action, int[] counters) {
        Product validProduct = readProductSelection(keyboard, service);
        if (validProduct == null) {
            System.out.println("Invalid product selection.");
            return;
        }
        System.out.printf("How many would you like to %s?%n", action);
        int quantity = readPositiveInt(keyboard);
        if ("sell".equals(action)) {
            service.sellProductByID(validProduct.getId(), quantity);
            counters[0]++;
            counters[1] += quantity;
        } else {
            service.restockProductByID(validProduct.getId(), quantity);
            counters[2]++;
            counters[3] += quantity;
        }
        System.out.printf("%s %d of %s.%n%d remaining in stock.%n",
                action.substring(0, 1).toUpperCase() + action.substring(1),
                quantity, validProduct.getName(), validProduct.getQuantityInStock());
    }

    /**
     * Reads a product selection from the user.
     * 
     * <p>
     * Displays the inventory list, reads a 1‑based index, validates it,
     * and returns the corresponding Product. Returns null if input is invalid.
     *
     * @param scanner   scanner for user input
     * @param inventory list of products to choose from
     * @return selected Product or null
     */
    private static Product readProductSelection(Scanner scanner, InventoryService service) {
        displayInventory(service);
        int productChoice = readPositiveInt(scanner);
        try {
            return service.getProductByID(productChoice);
        } catch (IllegalArgumentException e) {
            System.out.println("Product not found.");
            return null;
        }
    }

    /**
     * Reads and validates a positive integer.
     * 
     * <p>
     * Ensures input contains only digits and is > 0. Returns null for invalid
     * input.
     *
     * @param scanner scanner for user input
     * @return positive integer or null
     */
    private static int readPositiveInt(Scanner scanner) {
        while (true) {
            String trimmedRawInput = scanner.nextLine().trim();
            if (trimmedRawInput.isBlank()) {
                System.out.println("Input cannot be blank. Try again:");
                continue;
            }

            boolean isNumeric = true;
            for (int i = 0; i < trimmedRawInput.length(); i++) {
                if (!Character.isDigit(trimmedRawInput.charAt(i))) {
                    isNumeric = false;
                    break;
                }
            }
            if (!isNumeric) {
                System.out.println("Invalid number. Try again:");
                continue;
            }

            int posInt = Integer.parseInt(trimmedRawInput);
            if (posInt <= 0) {
                System.out.println("Number must be greater than 0. Try again:");
                continue;
            }

            return posInt;
        }
    }

    /**
     * Displays inventory with 1‑based numbering.
     * 
     * @param currentInventory list of products to display
     */
    private static void displayInventory(InventoryService service) {
        List<Product> currentInventory = service.getReadOnlyInventory();
        for (int i = 0; i < currentInventory.size(); i++) {
            System.out.printf("No: %d\tProduct: %s\tStock: %d%n",
                    currentInventory.get(i).getId(),
                    currentInventory.get(i).getName(),
                    currentInventory.get(i).getQuantityInStock());
        }
    }

    /**
     * Normalizes command input: trims and converts to lowercase.
     * 
     * @param input raw input string
     * @return normalized command, or empty string if null/blank
     */
    private static String normalizeCommand(String input) {
        if (input == null || input.isBlank()) {
            return "";
        }
        return input.trim().toLowerCase();
    }

    /**
     * Checks if the choice is one of the valid commands.
     * 
     * @param choice normalized command string
     * @return true if valid (sell, restock, exit)
     */
    private static boolean isValidChoice(String choice) {
        return choice.equals("sell") || choice.equals("restock") || choice.equals("exit");
    }
}