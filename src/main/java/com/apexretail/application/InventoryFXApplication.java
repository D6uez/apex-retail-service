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
 * <li>Separation of UI component creation and layout building via helper
 * methods</li>
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

    /** Enum representing the type of inventory action. */
    private enum ActionType {
        SELL,
        RESTOCK
    }

    /** Spacing (in pixels) between controls inside an HBox or VBox. */
    private static final int SPACING = 10;

    // -------------------------------------------------------------------------
    // Instance Fields (UI Controls)
    // -------------------------------------------------------------------------

    /** Service layer that handles inventory business logic. */
    private final InventoryService service = new InventoryService(new InventoryFileRepository());

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
     * Delegates UI construction to helper methods, attaches the close‑request
     * handler for auto‑saving, and displays the window.
     *
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        VBox root = buildRoot();
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Apex Point of Sale");
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            try {
                service.saveInventory();
            } catch (IOException e) {
                showErrorAlert(new RuntimeException("Failed to save inventory: " + e.getMessage()));
            }
        });
    }

    /**
     * Builds the root VBox containing the title and the input form.
     *
     * @return a VBox with the title and form arranged vertically
     */
    private VBox buildRoot() {
        HBox titleHBox = new HBox(buildTitle());
        titleHBox.setAlignment(Pos.CENTER);
        GridPane form = buildForm();
        return new VBox(titleHBox, form);
    }

    /**
     * Builds the grid form containing labels, text fields, and action buttons.
     * This method creates all input controls and buttons, configures them,
     * and attaches the event handlers for sell and restock actions.
     *
     * @return a configured GridPane with all input controls
     */
    private GridPane buildForm() {
        Button sellBtn = new Button("Sell");
        Button restockBtn = new Button("Restock");

        TextField productIDTextField = new TextField();
        TextField quantityTextField = new TextField();

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

        Label productIDLabel = new Label("Product ID:");
        Label quantityLabel = new Label("Quantity:");

        productIDTextField.setPromptText("Enter Product ID");
        productIDTextField.setPrefColumnCount(15);

        quantityTextField.setPromptText("Enter Quantity");
        quantityTextField.setPrefColumnCount(15);

        GridPane gridPane = new GridPane();
        gridPane.setMinSize(400, 200);

        gridPane.add(productIDLabel, 0, 1);
        gridPane.add(productIDTextField, 1, 1);
        gridPane.add(quantityLabel, 0, 2);
        gridPane.add(quantityTextField, 1, 2);
        gridPane.add(sellBtn, 0, 3);
        gridPane.add(restockBtn, 1, 3);

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(SPACING);
        gridPane.setVgap(SPACING);
        gridPane.setPadding(new Insets(SPACING));

        return gridPane;
    }

    /**
     * Creates the title label for the application window.
     *
     * @return a Label with the application title
     */
    private Label buildTitle() {
        return new Label("Apex Retail - Inventory CLI Replacement (Phase 1)");
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