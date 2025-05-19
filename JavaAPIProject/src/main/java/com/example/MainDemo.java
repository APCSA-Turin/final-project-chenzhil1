
package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainDemo extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Create a button with the text "Click Me"
        Button button = new Button("Click Me");  // :contentReference[oaicite:0]{index=0}

        // Set an action handler for button clicks
        button.setOnAction(e -> System.out.println("Button was clicked!"));  // :contentReference[oaicite:1]{index=1}

        // Use a simple StackPane to center the button
        StackPane root = new StackPane(button);  // :contentReference[oaicite:2]{index=2}

        // Create the scene with the root pane and set it on the stage
        Scene scene = new Scene(root, 1920, 1080);
        primaryStage.setTitle("Single Button Example");
        primaryStage.setScene(scene);
        primaryStage.show();  // :contentReference[oaicite:3]{index=3}
    }
}
