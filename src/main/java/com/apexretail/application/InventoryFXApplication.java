package com.apexretail.application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class InventoryFXApplication extends Application {

    @Override
    public void start(Stage primaryStage) {

        Label label = new Label("Apex Retail PoS");

        Scene scene = new Scene(label, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Apex Retail");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
