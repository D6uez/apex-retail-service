package com.apexretail.application;

import java.io.IOException;

import com.apexretail.repository.InventoryFileRepository;
import com.apexretail.service.InventoryService;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * InventoryFXApplication is the first-phase JavaFX graphical interface for the
 * Apex Retail inventory system. It replaces the command‑line interaction with a
 * simple window containing input fields for product ID and quantity, and two
 * action buttons (Sell and Restock).
 * <p>
 * This class illustrates:
 * <ul>
 * <li>Pure Java UI construction (no FXML)</li>
 * <li>Separation of UI component creation and layout building</li>
 * <li>Integration with the service and repository layers for inventory
 * operations</li>
 * <li>Input validation with user‑friendly error and success alerts</li>
 * <li>Automatic inventory saving on application close (with error alert on
 * failure)</li>
 * <li>Use of constants for layout spacing and an enum for action types</li>
 * </ul>
 * Future phases will expand the UI with inventory display, transaction
 * summaries, and more advanced features.
 *
 * @author David
 * @version 1.0.0
 */
public class InventoryFXApplication extends Application {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    private enum ActionType {
        SELL,
        RESTOCK
    }

    /** Spacing (in pixels) between controls inside an HBox or VBox. */
    private final int PIXELS_BETWEEN_CONTROLS = 10;

    // -------------------------------------------------------------------------
    // Instance Fields (UI Controls)
    // -------------------------------------------------------------------------

    /** Label displaying the application title. */
    private Label titleLabel;

    /** Label for the product ID field. */
    private Label productIDLabel;

    /** Label for the quantity field. */
    private Label quantityLabel;

    /** Text field where the user enters the product ID. */
    private TextField productIDTextField;

    /** Text field where the user enters the quantity. */
    private TextField quantityTextField;

    /** Button to trigger a sell operation. */
    private Button sellBtn;

    /** Button to trigger a restock operation. */
    private Button restockBtn;

    /** Repository for file‑based inventory persistence. */
    private final InventoryFileRepository repo = new InventoryFileRepository();

    /** Service layer that handles inventory business logic. */
    private final InventoryService service = new InventoryService(repo);

    // -------------------------------------------------------------------------
    // Entry Point
    // -------------------------------------------------------------------------

    /**
     * The main entry point for the JavaFX application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        launch(args);
    }

    // -------------------------------------------------------------------------
    // JavaFX Application Lifecycle
    // -------------------------------------------------------------------------

    /**
     * Initializes and configures the primary stage with the application's UI.
     * Creates all controls, sets up event handlers, builds the layout,
     * and displays the window.
     *
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        // ---------------------------------------------------------------------
        // 1. Create and configure controls
        // ---------------------------------------------------------------------
        titleLabel = new Label("Apex Retail - Inventory CLI Replacement (Phase 1)");
        productIDLabel = new Label("Product ID:");
        quantityLabel = new Label("Quantity:");

        productIDTextField = new TextField();
        productIDTextField.setPromptText("Enter Product ID");
        productIDTextField.setPrefColumnCount(15);

        quantityTextField = new TextField();
        quantityTextField.setPromptText("Enter Quantity");
        quantityTextField.setPrefColumnCount(15);

        sellBtn = new Button("Sell");
        restockBtn = new Button("Restock");

        // ---------------------------------------------------------------------
        // 2. Attach event handlers – validate input and call service methods
        // ---------------------------------------------------------------------
        sellBtn.setOnAction(e -> {
            try {
                processAction(productIDTextField.getText(), quantityTextField.getText(), ActionType.SELL);
            } catch (IllegalArgumentException exc) {
                showErrorAlert(exc);
            }
        });

        restockBtn.setOnAction(e -> {
            try {
                processAction(productIDTextField.getText(), quantityTextField.getText(), ActionType.RESTOCK);
            } catch (IllegalArgumentException exc) {
                showErrorAlert(exc);
            }
        });

        // ---------------------------------------------------------------------
        // 3. Build layout using a GridPane for the input fields and buttons
        // ---------------------------------------------------------------------
        GridPane gridPane = new GridPane();
        gridPane.setMinSize(400, 200);

        gridPane.add(productIDLabel, 0, 1);
        gridPane.add(productIDTextField, 1, 1);
        gridPane.add(quantityLabel, 0, 2);
        gridPane.add(quantityTextField, 1, 2);
        gridPane.add(sellBtn, 0, 3);
        gridPane.add(restockBtn, 1, 3);

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(PIXELS_BETWEEN_CONTROLS);
        gridPane.setVgap(PIXELS_BETWEEN_CONTROLS);
        gridPane.setPadding(new Insets(PIXELS_BETWEEN_CONTROLS));

        HBox titleHBox = new HBox(titleLabel);
        titleHBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(titleHBox, gridPane);
        root.setAlignment(Pos.CENTER);

        // ---------------------------------------------------------------------
        // 4. Create scene and configure stage
        // ---------------------------------------------------------------------
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Apex Point of Sale");
        primaryStage.show();

        // Auto‑save inventory when the window is closed; show error alert on failure.
        primaryStage.setOnCloseRequest(event -> {
            try {
                service.saveInventory();
            } catch (IOException e) {
                showErrorAlert(new RuntimeException("Failed to save inventory: " + e.getMessage()));
            }
        });
    }

    /**
     * Processes a sell or restock action based on the provided {@link ActionType}.
     * Validates inputs, delegates to the service, and shows a success alert with
     * the message returned from the service.
     *
     * @param productID the product ID entered by the user
     * @param quantity  the quantity entered by the user
     * @param at        the type of action to perform (SELL or RESTOCK)
     * @throws IllegalArgumentException if input validation fails or product is not
     *                                  found
     */
    private void processAction(String productID, String quantity, ActionType at) {
        int id = readPositiveInt(productID);
        int qty = readPositiveInt(quantity);

        switch (at) {
            case SELL -> {
                String message = service.sellProductByID(id, qty);
                showSuccessAlert(message);
            }
            case RESTOCK -> {
                String message = service.restockProductByID(id, qty);
                showSuccessAlert(message);
            }
        }
    }

    /**
     * Displays an error alert dialog with the message from the given exception.
     *
     * @param exc the exception containing the error details
     */
    private void showErrorAlert(Exception exc) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("An exception occurred!");
        alert.setContentText("Details: " + exc.getMessage());
        alert.showAndWait();
    }

    /**
     * Displays a success alert dialog after a completed transaction.
     *
     * @param message the success message to display (e.g., "Successfully sold 5
     *                units of Tomato.")
     */
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Transaction Completed");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Parses and validates a string as a positive integer.
     *
     * @param userInput the raw input string
     * @return the parsed positive integer
     * @throws IllegalArgumentException if input is blank, non‑numeric, or ≤ 0
     */
    private int readPositiveInt(String userInput) {
        String trimmed = userInput.trim();
        if (trimmed.isBlank()) {
            throw new IllegalArgumentException("Value cannot be blank.");
        }
        try {
            int value = Integer.parseInt(trimmed);
            if (value <= 0) {
                throw new IllegalArgumentException("Value must be positive.");
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Value entered is not a valid number.");
        }
    }
}