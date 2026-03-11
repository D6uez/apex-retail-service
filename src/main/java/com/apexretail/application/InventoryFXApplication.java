package com.apexretail.application;

import java.io.IOException;

import com.apexretail.diagnostics.SystemInfo;
import com.apexretail.domain.Product;
import com.apexretail.repository.InventoryFileRepository;
import com.apexretail.service.InventoryService;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * InventoryFXApplication is the first-phase JavaFX graphical interface for the
 * Apex Retail inventory system. It replaces the command‑line interaction with a
 * window containing a TableView to select a product, a quantity input field,
 * radio buttons to choose Sell or Restock, and a Process button. A transaction
 * history area displays the outcome of each operation, and the product list
 * automatically refreshes after each transaction.
 * <p>
 * This class illustrates:
 * <ul>
 * <li>Pure Java UI construction (no FXML)</li>
 * <li>Separation of UI component creation and layout building via helper
 * methods</li>
 * <li>Integration with the service and repository layers for inventory
 * operations</li>
 * <li>Input validation with user‑friendly error and success alerts</li>
 * <li>Transaction history displayed in a read‑only text area (with an initial
 * header)</li>
 * <li>Live inventory display using a TableView</li>
 * <li>Automatic inventory saving on application close (with error alert on
 * failure)</li>
 * <li>Menu bar with File (Save, Exit) and Help (System Information) menus</li>
 * <li>Conditional exit behavior – application exits only if save succeeds,
 * preventing data loss</li>
 * <li>Display of system diagnostic information via menu, using
 * {@link SystemInfo}</li>
 * <li>Use of constants for layout spacing, maximum width, and an enum for
 * action types</li>
 * </ul>
 * Future phases will expand the UI with more detailed summaries and additional
 * features.
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

    /** Maximum width (in pixels) for the transaction history text area. */
    private static final int MAX_WIDTH = 500;

    // -------------------------------------------------------------------------
    // Instance Fields (UI Controls)
    // -------------------------------------------------------------------------

    /** Service layer that handles inventory business logic. */
    private final InventoryService service = new InventoryService(new InventoryFileRepository());

    /** Read‑only text area to display a history of completed transactions. */
    private TextArea transactionHistory;

    /**
     * Table view showing current inventory with product name, quantity, and price.
     */
    private TableView<Product> currentInventoryTableView;

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
        BorderPane root = buildRoot();

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Apex Point of Sale");
        scene.getStylesheets().add(getClass().getResource("/inventory.css").toExternalForm());
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            attemptSaveInventory();
        });
    }

    // -------------------------------------------------------------------------
    // UI Building Helpers
    // -------------------------------------------------------------------------

    /**
     * Builds the root layout using a BorderPane.
     * The menu bar is placed at the top, and a VBox containing the title,
     * input form, and transaction history area is placed in the center.
     * The transaction history is displayed below the form.
     *
     * @return the configured BorderPane
     */
    private BorderPane buildRoot() {
        buildTransactionHistory();
        BorderPane borderPane = new BorderPane();
        HBox titleHBox = new HBox(buildTitle());
        titleHBox.setAlignment(Pos.CENTER);

        GridPane form = buildForm();
        VBox vBox = new VBox(SPACING, titleHBox, form, transactionHistory);
        vBox.setAlignment(Pos.CENTER);

        borderPane.setTop(buildMenuBar());
        borderPane.setCenter(vBox);

        return borderPane;
    }

    /**
     * Builds and returns the menu bar for the application.
     * Contains File menu (Save, Exit) and Help menu (System Information).
     * <p>
     * The Save menu item shows a success alert only if the save operation
     * completes successfully. The Exit menu item attempts to save and then
     * exits the application only if the save succeeded, preventing data loss.
     *
     * @return the configured MenuBar
     */
    private MenuBar buildMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem exitItem = new MenuItem("Exit");
        fileMenu.getItems().add(saveItem);
        fileMenu.getItems().add(exitItem);

        Menu helpMenu = new Menu("Help");
        MenuItem systemInfoItem = new MenuItem("System Information");
        helpMenu.getItems().add(systemInfoItem);

        saveItem.setOnAction(event -> {
            if (attemptSaveInventory()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("Notice");
                alert.setContentText("Inventory saved successfully.");
                alert.showAndWait();
            }
        });

        exitItem.setOnAction(event -> {
            if (attemptSaveInventory()) {
                Platform.exit();
            }
        });

        systemInfoItem.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("System Information");
            alert.setHeaderText("Notice");
            alert.setContentText(SystemInfo.getSystemInfo());
            alert.showAndWait();
        });

        menuBar.getMenus().add(fileMenu);
        menuBar.getMenus().add(helpMenu);

        return menuBar;
    }

    /**
     * Creates the title label for the application window.
     *
     * @return a Label with the application title
     */
    private Label buildTitle() {
        Label titleLabel = new Label("Apex Retail - Inventory CLI Replacement (Phase 1)");
        titleLabel.getStyleClass().add("title-label");
        return titleLabel;
    }

    /**
     * Builds the grid form containing the product selection table, quantity
     * field, action radio buttons, and the process button.
     * The product selection is required; an error is shown if none is selected.
     *
     * @return a configured GridPane with all input controls
     */
    private GridPane buildForm() {
        RadioButton sellBtn = new RadioButton("Sell");
        sellBtn.setSelected(true);
        RadioButton restockBtn = new RadioButton("Restock");
        Button processActionBtn = new Button("Process");

        ToggleGroup actionToggleGroup = new ToggleGroup();
        sellBtn.setToggleGroup(actionToggleGroup);
        restockBtn.setToggleGroup(actionToggleGroup);

        buildInventoryTableview(); // initializes the table

        TextField quantityTextField = new TextField();
        quantityTextField.setPromptText("Enter Quantity");
        quantityTextField.setPrefColumnCount(15);

        processActionBtn.setOnAction(e -> {
            Product selected = currentInventoryTableView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showErrorAlert(new IllegalArgumentException("Please select a product."));
                return;
            }
            long productId = selected.getId();
            String quantity = quantityTextField.getText();

            ActionType action = sellBtn.isSelected() ? ActionType.SELL : ActionType.RESTOCK;

            try {
                processAction(productId, quantity, action);
                currentInventoryTableView.refresh();
            } catch (IllegalArgumentException exc) {
                showErrorAlert(exc);
            }
        });

        Label productLabel = new Label("Product:");
        Label quantityLabel = new Label("Quantity:");

        HBox btnHBox = new HBox(SPACING, sellBtn, restockBtn);

        GridPane gridPane = new GridPane();
        gridPane.setMinSize(400, 200);

        gridPane.add(productLabel, 0, 1);
        gridPane.add(currentInventoryTableView, 1, 1);
        gridPane.add(quantityLabel, 0, 2);
        gridPane.add(quantityTextField, 1, 2);
        gridPane.add(btnHBox, 1, 3);
        gridPane.add(processActionBtn, 1, 4);

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(SPACING);
        gridPane.setVgap(SPACING);
        gridPane.setPadding(new Insets(SPACING * 2));

        return gridPane;
    }

    /**
     * Initializes the inventory table view with columns for product name,
     * quantity in stock, and price. Populates the table with data from the
     * service.
     */
    private void buildInventoryTableview() {
        currentInventoryTableView = new TableView<>();

        TableColumn<Product, String> nameCol = new TableColumn<>("Product");
        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        TableColumn<Product, String> qtyCol = new TableColumn<>("In Stock");
        qtyCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getQuantityInStock())));

        TableColumn<Product, String> priceCol = new TableColumn<>("Price Each");
        priceCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getPrice())));

        currentInventoryTableView.getColumns().add(nameCol);
        currentInventoryTableView.getColumns().add(qtyCol);
        currentInventoryTableView.getColumns().add(priceCol);
        currentInventoryTableView.setItems(FXCollections.observableArrayList(service.getReadOnlyInventory()));
    }

    /**
     * Initializes the transaction history text area with default properties.
     * The area is read‑only, has a maximum width set by {@link #MAX_WIDTH},
     * and starts with a header "Transaction History:\n".
     */
    private void buildTransactionHistory() {
        transactionHistory = new TextArea("Transaction History:\n");
        transactionHistory.setMaxWidth(MAX_WIDTH);
        transactionHistory.setEditable(false);
    }

    // -------------------------------------------------------------------------
    // Transaction Processing
    // -------------------------------------------------------------------------

    /**
     * Processes a sell or restock action based on the provided {@link ActionType}.
     * Validates the quantity, delegates to the service, shows a success alert with
     * the message returned from the service, and appends the message to the
     * transaction history.
     *
     * @param productID the ID of the product to act on (already obtained from
     *                  table)
     * @param quantity  the quantity entered by the user (as a string, will be
     *                  parsed)
     * @param at        the type of action to perform (SELL or RESTOCK)
     * @throws IllegalArgumentException if quantity validation fails or product is
     *                                  not found
     */
    private void processAction(long productID, String quantity, ActionType at) {
        int qty = readPositiveInt(quantity);

        switch (at) {
            case SELL -> {
                String message = service.sellProductByID(productID, qty);
                showSuccessAlert(message);
                appendTransaction(message);
            }
            case RESTOCK -> {
                String message = service.restockProductByID(productID, qty);
                showSuccessAlert(message);
                appendTransaction(message);
            }
        }
    }

    /**
     * Appends a message to the transaction history, followed by a newline.
     *
     * @param message the message to append (e.g., a transaction result)
     */
    private void appendTransaction(String message) {
        transactionHistory.appendText(message + "\n");
    }

    // -------------------------------------------------------------------------
    // Validation and Error Handling
    // -------------------------------------------------------------------------

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

    // -------------------------------------------------------------------------
    // Persistence
    // -------------------------------------------------------------------------

    /**
     * Attempts to save the current inventory to persistent storage.
     *
     * @return {@code true} if the save operation succeeded, {@code false} otherwise
     * @see #showErrorAlert(Exception)
     */
    private boolean attemptSaveInventory() {
        boolean isSaveSuccess = false;
        try {
            service.saveInventory();
            isSaveSuccess = true;
        } catch (IOException e) {
            showErrorAlert(new RuntimeException("Failed to save inventory: " + e.getMessage()));
        }
        return isSaveSuccess;
    }
}