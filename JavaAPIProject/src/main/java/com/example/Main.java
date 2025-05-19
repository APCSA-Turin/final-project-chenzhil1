package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.*;

public class Main extends Application {
    private static boolean debug = true;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if(debug) {
            System.out.println("Text Debug mode is enabled");
            GameLogic.initialize("Student");
            System.out.print("Please enter your name: ");
            String name = scanner.nextLine();
            System.out.println("Good morning " + name + "!");
            System.out.println("It is currently " + GameLogic.getTime() + " on " + GameLogic.getDate());
            System.out.println("You are a " + Map.getSchoolType() + " school student at " + Map.getSchoolName() + " in " + Map.getCity() + ", " + Map.getState());
            

        }
        else {

        
        launch(args);
        }
        scanner.close();
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox vbox = new VBox(10);
        Scene scene = new Scene(vbox, 1200, 695);
        primaryStage.setScene(scene);
        primaryStage.setTitle("A Day in A City");
        primaryStage.show();           
     }
    
}
