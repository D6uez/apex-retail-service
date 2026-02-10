package com.apexretail.repository;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.apexretail.domain.Product;

/**
 * Repository for inventory data persistence using CSV files.
 * 
 * <p>
 * Handles reading and writing Product objects to CSV format.
 * Files are stored in the "data" directory with format:
 * id,name,price,quantity,category
 */
public class InventoryFileRepository {

    /**
     * Writes Product objects to a CSV file.
     * 
     * @param fileName file to write (without "data//" path)
     * @param inv      list of Product objects to persist
     * @throws IOException if file cannot be created or written
     */
    public void writeFile(String fileName, List<Product> inv) throws IOException {
        try (PrintWriter outputFile = new PrintWriter("data//" + fileName);) {
            for (Product p : inv) {
                outputFile.printf(
                        "%d,%s,%s,%d,%s%n",
                        p.getId(),
                        p.getName(),
                        p.getPrice().toPlainString(),
                        p.getQuantityInStock(),
                        p.getCategory() // String for now
                );
            }
        }
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
            return parsedInput;
        }
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
                Long id = Long.parseLong(prod[0]);
                String name = prod[1];
                BigDecimal price = new BigDecimal(prod[2]);
                int quantity = Integer.parseInt(prod[3]);
                String category = prod[4];

                mappedInventory.add(new Product(id, name, price, quantity, category));
            } catch (NumberFormatException | ArithmeticException e) {
                System.out.println("Row mapping failed, skipping current row " + Arrays.toString(prod));
                continue;
            }

        }
        return mappedInventory;
    }

    /**
     * Loads inventory from CSV file and converts to Product objects.
     * 
     * <p>
     * Returns empty list if the file does not exist.
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
        List<Product> mappedInventory = mapRowToProduct(parsedInventory);
        return mappedInventory;
    }
}