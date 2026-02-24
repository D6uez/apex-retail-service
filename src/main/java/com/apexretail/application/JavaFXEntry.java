package com.apexretail.application;

/**
 * Launcher class for the JavaFX application.
 * 
 * <p>
 * This class provides a separate entry point to launch the JavaFX
 * InventoryFXApplication. It is useful when the JavaFX runtime
 * needs to be initialized from a non‑JavaFX main class, or when working
 * with certain build tools and module paths.
 * </p>
 *
 * <p>
 * Simply delegates to {@link InventoryFXApplication#main(String[])}.
 * </p>
 *
 * @author David
 * @version 1.0.0
 */
public class JavaFXEntry {
    /**
     * Entry point that launches the JavaFX inventory application.
     *
     * @param args command line arguments (passed through to the JavaFX application)
     */
    public static void main(String[] args) {
        InventoryFXApplication.main(args);
    }
}