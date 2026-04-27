package com.apexretail.repository;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.apexretail.domain.Product;

/**
 * Repository for inventory data persistence using CSV files.
 * Implements {@link InventoryRepository} to provide CRUD operations.
 * 
 * <p>
 * Handles reading and writing Product objects to CSV format.
 * Files are stored in the "data" directory with format:
 * id,name,price,quantity,category
 * 
 * <p>
 * The repository maintains an in‑memory list of products, loads from CSV
 * on construction, and persists changes immediately when {@link #save(Product)}
 * is called.
 */
public class FileInventoryRepository implements InventoryRepository {

    private final String FILE_NAME = "inventoryFile.csv";
    private List<Product> inventory = new ArrayList<Product>();

    /**
     * Constructs the repository, loading inventory from the default CSV file.
     * If the file does not exist or is empty, a default inventory is created.
     */
    public FileInventoryRepository() {
        try {
            inventory = loadInventory(FILE_NAME);
            if (inventory.isEmpty()) {
                createDefaultInventory();
            }
        } catch (IOException e) {
            System.out.println("Critical error loading inventory. Starting with empty inventory.");
        }
    }

    /**
     * Writes the given list of Product objects to a CSV file.
     * 
     * @param fileName file to write (without "data//" path)
     * @param inv      list of Product objects to persist
     * @throws IOException if file cannot be created or written
     */
    public void writeFile(String fileName, List<Product> inv) throws IOException {
        try (PrintWriter outputFile = new PrintWriter("data//" + fileName)) {
            for (Product p : inv) {
                outputFile.printf(
                        "%d,%s,%s,%d,%s%n",
                        p.getId(),
                        p.getName(),
                        p.getPrice().toPlainString(),
                        p.getQuantityInStock(),
                        p.getCategory());
            }
        }
    }

    /**
     * Loads inventory from CSV file and converts to Product objects.
     * 
     * <p>
     * Returns an empty list if the file does not exist.
     * 
     * @param fileName CSV file to load (without "data//" path)
     * @return List of Product objects from file data
     * @throws IOException if file exists but cannot be read
     */
    public List<Product> loadInventory(String fileName) throws IOException {
        File invFile = new File("data//" + fileName);
        if (!invFile.exists()) {
            System.out.println("File not found, creating blank inventory.");
            return new ArrayList<>();
        }
        System.out.println("File found, loading inventory...");

        List<String[]> parsedInventory = parseCSV(invFile);
        return mapRowToProduct(parsedInventory);
    }

    /**
     * Parses CSV file content into a list of string arrays.
     * 
     * @param fileName CSV file to parse
     * @return list where each array contains tokens from one CSV line
     * @throws IOException if file cannot be accessed or read
     */
    private List<String[]> parseCSV(File fileName) throws IOException {
        List<String[]> parsedInput = new ArrayList<>();
        try (Scanner inputFile = new Scanner(fileName)) {
            while (inputFile.hasNextLine()) {
                String line = inputFile.nextLine().trim();
                String[] tokens = line.split(",");
                parsedInput.add(tokens);
            }
        }
        return parsedInput;
    }

    /**
     * Maps parsed CSV rows to Product objects.
     * 
     * @param parsedInv list of string arrays from parsed CSV
     * @return List of mapped Product objects
     */
    private List<Product> mapRowToProduct(List<String[]> parsedInv) {
        List<Product> mappedInventory = new ArrayList<>();
        for (String[] prod : parsedInv) {
            if (prod.length != 5) {
                System.out.println("Skipping malformed row: " + Arrays.toString(prod));
                continue;
            }
            try {
                long id = Long.parseLong(prod[0]);
                String name = prod[1];
                BigDecimal price = new BigDecimal(prod[2]);
                int quantity = Integer.parseInt(prod[3]);
                String category = prod[4];

                mappedInventory.add(new Product(id, name, price, quantity, category));
            } catch (NumberFormatException | ArithmeticException e) {
                System.out.println("Row mapping failed, skipping current row " + Arrays.toString(prod));
            }
        }
        return mappedInventory;
    }

    /**
     * Creates a default inventory with sample products.
     * Called when no existing inventory file is found.
     */
    private void createDefaultInventory() {
        inventory.add(new Product(1, "Tomato", BigDecimal.valueOf(0.25), 30, "Produce"));
        inventory.add(new Product(2, "Onion", BigDecimal.valueOf(0.90), 20, "Produce"));
        inventory.add(new Product(3, "Milk", BigDecimal.valueOf(2.46), 15, "Dairy"));
        inventory.add(new Product(4, "Cheese", BigDecimal.valueOf(3.15), 10, "Dairy"));
    }

    // -------------------------------------------------------------------------
    // InventoryRepository implementation
    // -------------------------------------------------------------------------

    /**
     * Returns an unmodifiable view of all products currently in the repository.
     * 
     * @return immutable copy of the internal inventory list
     */
    @Override
    public List<Product> findAll() {
        return List.copyOf(inventory);
    }

    /**
     * Finds a product by its ID.
     * 
     * @param id the product ID to search for
     * @return an Optional containing the product if found, empty otherwise
     */
    @Override
    public Optional<Product> findById(long id) {
        for (Product p : inventory) {
            if (p.getId() == id) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    /**
     * Saves a product (adds if new, updates if existing) and immediately persists
     * the entire inventory to the CSV file.
     * 
     * @param product the product to save
     */
    @Override
    public void save(Product product) {
        Optional<Product> existing = findById(product.getId());

        if (existing.isEmpty()) {
            inventory.add(product);
        } else {
            // Update existing product (in-place modification of the list)
            int index = inventory.indexOf(existing.get());
            inventory.set(index, product);
        }

        try {
            writeFile(FILE_NAME, inventory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}