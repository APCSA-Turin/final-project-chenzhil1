package com.example;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.lang.reflect.Array;
import java.util.*;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import javafx.scene.control.TextInputDialog;

public class Main extends Application {
    private static boolean debug = false;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if(debug) {
            System.out.println("Text Debug mode is enabled");
            GameLogic game = new GameLogic();
            System.out.print("Please enter your name: ");
            String name = scanner.nextLine();
            System.out.println("Good morning " + name + "!");
            System.out.println("It is currently " + game.getTime() + " on " + game.getDate());
            System.out.println("You are a student at " + Map.getSchoolName() + " in " + Map.getCity() + ", " + Map.getState());
            scanner.nextLine();
            int transitTime = Map.getTransitTime(Map.getLatitude(), Map.getLongitude(), Map.getSchoolLatitude(), Map.getSchoolLongitude(), "drive");
            if(transitTime >= 60) {
                game.setSchoolHour(8, 30);
            }
            else if (transitTime > 120) {
                game.setSchoolHour(6,transitTime /10 * 10 + 10);
                game.updateTime(1);
            }
            System.out.println("You just woke up, school starts at " + game.formatTime(game.getSchoolHour(), game.getSchoolMinute()));
            int action = -1;
            while(action != game.getMorningThings().size() - 1) {
                System.out.println("What do you want to do now?");
                for(int i = 0; i < game.getMorningThings().size(); i++) {
                    System.out.println((i + 1) + ". " + game.getMorningThings().get(i));
                }
                System.out.println("0. Exit home");
                System.out.print("Please enter the number of the action you want to take: ");
                ArrayList<String> morningActions = game.getMorningThings();
                while(action < 0 || action > morningActions.size()) {
                    action = scanner.nextInt();
                }

                if(action == 0) {
                    // Exit the loop if user chooses to exit home
                    break;
                }

                if(morningActions.get(action - 1).equalsIgnoreCase("Brush your teeth")) {
                    System.out.println("You brush your teeth.");
                    game.addTime(5);

                }
                else if(morningActions.get(action - 1).equalsIgnoreCase("Eat Breakfast")) {
                    System.out.println("You cooked and ate breakfast.");
                    game.addTime(30);
                }
                else if(morningActions.get(action - 1).equalsIgnoreCase("Get dressed")) {
                    System.out.println("You got dressed.");
                    game.addTime(10);

                }
                else if(game.getMorningThings().get(action - 1).equalsIgnoreCase("Check Weather")) {
                    game.getWeather();
                    game.addTime(5);
                }
                else if(game.getMorningThings().get(action - 1).equalsIgnoreCase("Check transit time")) {
                    System.out.println("The transit time to school is " + transitTime + " minutes.");
                    System.out.println("School starts at " + game.getSchoolHour() + ":" + game.getSchoolMinute());
                    game.addTime(5);
                }
                else if(game.getMorningThings().get(action - 1).equalsIgnoreCase("Pack your bag")) {
                    game.packBag();
                    game.addTime(10);
                }
                else if(game.getMorningThings().get(action - 1).equalsIgnoreCase("Play with phone")) {
                    int phoneTime = (int)(Math.random() * 6 + 1) * 5;
                    game.addTime(phoneTime);
                    System.out.println("You played with your phone.");
                }
                game.updateTime(0);
                System.out.println("It is now " + game.getTime());
                System.out.println("Remember that you have to arrive at school by " + game.formatTime(game.getSchoolHour(), game.getSchoolMinute()));
                action = -1;
                System.out.println();
                if(game.getSchoolMinute() >= 30) {
                    if(game.getHour() >= game.getSchoolHour() && game.getMinute() >= game.getSchoolMinute() - 15) {
                        System.out.println("Your mom is calling you to leave home.");
                        System.out.println("You are late for school!");
                        System.out.println("You have to leave home now!");
                        System.out.println();
                        break;
                    }
                    else if(game.getHour() > game.getSchoolHour() || (game.getHour() == game.getSchoolHour() - 1 && game.getMinute() >= (game.getSchoolMinute() + 45))) {
                        System.out.println("Your mom is calling you to leave home.");
                        System.out.println("You are late for school!");
                        System.out.println("You have to leave home now!");
                        System.out.println();
                        break;
                    }
                }
            }
            System.out.println("You left home.");
            if(transitTime >= 60) {
                transitTime = Map.getTransitTime(Map.getLatitude(), Map.getLongitude(), Map.getSchoolLatitude(), Map.getLongitude(), "drive");
                System.out.println("You chose to drive to school.");
            }
            else {
                if(Map.getTransitTime(Map.getLatitude(), Map.getLongitude(), Map.getSchoolLatitude(), Map.getSchoolLongitude(), "walk") > transitTime) {
                    System.out.println("You chose to take the public transit to school.");
                }
                else {
                    System.out.println("You chose to walk to school.");
                }
            }
            System.out.println("It is currently " + Weather.defineWeather(Weather.getWeather(game.getHour())));
            game.weatherEvent();
            game.addTime(transitTime);
            game.updateTime(0);
            scanner.nextLine();
            System.out.println("You arrived at school at " + game.getTime());
            game.checkLateSchool();
            scanner.nextLine();
            game.checkBagContents();
            
        }
        else {

        
        launch(args);
        }
        scanner.close();
    }
    @Override

    public void start(Stage primaryStage) {
        GameLogic game = new GameLogic();

        // Ask for user's name
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("A Day in A City");
        dialog.setHeaderText("Please enter your name");
        dialog.setContentText("Name:");
        Optional<String> result = dialog.showAndWait();

        if (!result.isPresent()) return;
        String name = result.get();

        showScene1(primaryStage, name, game);

    }


    public void typeText(Label label, String fullText, Duration delay, Runnable onFinished) {
        final StringBuilder displayedText = new StringBuilder();
        Timeline timeline = new Timeline();
        for (int i = 0; i < fullText.length(); i++) {
            final int index = i;
            KeyFrame keyFrame = new KeyFrame(delay.multiply(i), event -> {
                displayedText.append(fullText.charAt(index));
                label.setText(displayedText.toString());
            });
            timeline.getKeyFrames().add(keyFrame);
        }
        timeline.setOnFinished(event -> {
            if (onFinished != null) {
                onFinished.run();
            }
        });
        timeline.play();
    }


    public void showScene1(Stage stage, String name, GameLogic game) {
        Label label = new Label();
        label.setStyle("-fx-font-size: 24px; -fx-text-fill: black;");

        Image image = new Image("file:/workspaces/final-project-chenzhil1/JavaAPIProject/src/main/java/com/example/images/wakeup.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(600);
        imageView.setFitHeight(400);
        imageView.setPreserveRatio(true);
        Label hintLabel = new Label("Press ENTER to continue...");
        hintLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #777777;");

        VBox vbox = new VBox(10, imageView, label, hintLabel);
        vbox.setAlignment(Pos.CENTER);
        Scene scene1 = new Scene(vbox, 1200,695);
        stage.setScene(scene1);
        stage.setTitle("A Day in A City");
        stage.show();
        String fullText = "Good morning " + name + "!\n" +
        "It is currently " + game.getTime() + " on " + game.getDate() + "\n" +
        "You are a student at " + Map.getSchoolName() + " in " + Map.getCity() + ", " + Map.getState();
        typeText(label, fullText, Duration.millis(50), () -> {
            // This code will run after the text is fully displayed
                scene1.setOnKeyPressed(event -> {
                if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                    showScene2(stage, name, game);
                }
            });
        });
    }

    public void showScene2(Stage stage, String name, GameLogic game) {
        Label label = new Label();
        label.setStyle("-fx-font-size: 24px; -fx-text-fill: black;");

        Image image = new Image("file:/workspaces/final-project-chenzhil1/JavaAPIProject/src/main/java/com/example/images/wakeup.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(600);
        imageView.setFitHeight(400);
        imageView.setPreserveRatio(true);
        Label hintLabel = new Label("Press ENTER to continue...");
        hintLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #777777;");

        VBox vbox = new VBox(10, imageView, label, hintLabel);
        vbox.setAlignment(Pos.CENTER);
        Scene scene2 = new Scene(vbox, 1200,695);
        stage.setScene(scene2);
        stage.setTitle("A Day in A City");
        stage.show();

        int transitTime = Map.getTransitTime(Map.getLatitude(), Map.getLongitude(), Map.getSchoolLatitude(), Map.getSchoolLongitude(), "drive");
        if(transitTime >= 60) {
            game.setSchoolHour(8, 30);
        }
        else if (transitTime > 120) {
            game.setSchoolHour(6,transitTime /10 * 10 + 10);
            game.updateTime(1);
        }

        String fullText = "You just woke up, school starts at " + game.formatTime(game.getSchoolHour(), game.getSchoolMinute());
        typeText(label, fullText, Duration.millis(50), () -> {
            // This code will run after the text is fully displayed
                scene2.setOnKeyPressed(event -> {
                if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                    showScene3(stage, name, game, transitTime);
                }
            });
        });
    }

    public void showScene3(Stage stage, String name, GameLogic game, int transitTime) {
        Label label = new Label("What do you want to do now?");
        label.setStyle("-fx-font-size: 24px; -fx-text-fill: black;");
        VBox buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.CENTER); // <-- Add this line
        ArrayList<Button> buttons = new ArrayList<Button>();
        for(String action : game.getMorningThings()) {
            Button button = new Button(action);
            button.setStyle("-fx-font-size: 18px; -fx-padding: 10px 20px;");
            button.setOnAction(e -> handleMorningChoice(action, game, transitTime, stage)); // <-- set handler here
            buttons.add(button);

        }
        Timeline timeline = new Timeline();
        for(int i = 0; i < buttons.size(); i++) {
            final int index = i;
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3 * i), event -> {
                buttonBox.getChildren().add(buttons.get(index));

            });
            timeline.getKeyFrames().add(keyFrame);
        }
        timeline.play();

        VBox vbox = new VBox(10,label, buttonBox);
        vbox.setAlignment(Pos.CENTER);
        Scene scene3 = new Scene(vbox, 1200,695);
        stage.setScene(scene3);
        stage.setTitle("A Day in A City");
        stage.show();
    }

    public void handleMorningChoice(String action, GameLogic game, int transitTime, Stage stage) {
        switch(action) {
            case "Brush your teeth":
                System.out.println("You brush your teeth.");
                game.addTime(5);
                showScene3a(stage, game, transitTime);
                return; 
            case "Eat Breakfast":
                System.out.println("You cooked and ate breakfast.");
                game.addTime(30);
                showScene3b(stage, action, game, transitTime);
                return;
            case "Get dressed":
                System.out.println("You got dressed.");
                game.addTime(10);
                showScene3c(stage, action, game, transitTime);
                return;
            case "Check Weather":
                game.getWeather();
                game.addTime(5);
                break;
            case "Check transit time":
                System.out.println("The transit time to school is " + transitTime + " minutes.");
                System.out.println("School starts at " + game.getSchoolHour() + ":" + game.getSchoolMinute());
                game.addTime(5);
                break;
            case "Pack your bag":
                game.packBag();
                game.addTime(10);
                break;
            case "Play with phone":
                int phoneTime = (int)(Math.random() * 6 + 1) * 5;
                game.addTime(phoneTime);
                System.out.println("You played with your phone.");
                break;
            case "Exit home":

            break;
        }

    }
    public void showScene3a(Stage stage, GameLogic game, int transitTime) {
        Label label = new Label();
        label.setStyle("-fx-font-size: 24px; -fx-text-fill: black;");

        Image image = new Image("file:/workspaces/final-project-chenzhil1/JavaAPIProject/src/main/java/com/example/images/brush.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(600);
        imageView.setFitHeight(400);
        imageView.setPreserveRatio(true);
        Label hintLabel = new Label("Press ENTER to continue...");
        hintLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #777777;");

        VBox vbox = new VBox(10, imageView, label, hintLabel);
        vbox.setAlignment(Pos.CENTER);
        Scene scene1 = new Scene(vbox, 1200,695);
        stage.setScene(scene1);
        stage.setTitle("A Day in A City");
        stage.show();
        String fullText = "You brushed your teeth.";
        for(int i = 0; i < game.getMorningThings().size(); i++) {
            if(game.getMorningThings().get(i).equalsIgnoreCase("Brush your teeth")) {
                game.getMorningThings().remove(i);
            }
        }
        
        typeText(label, fullText, Duration.millis(50), () -> {
            // This code will run after the text is fully displayed
                scene1.setOnKeyPressed(event -> {
                if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                    showScene3(stage, fullText, game, 0);
                }
            });
        });
    }

    public void showScene3b(Stage stage, String name, GameLogic game, int transitTime) {
        Label label = new Label();
        label.setStyle("-fx-font-size: 24px; -fx-text-fill: black;");

        Image image = new Image("file:/workspaces/final-project-chenzhil1/JavaAPIProject/src/main/java/com/example/images/breakfast.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(600);
        imageView.setFitHeight(400);
        imageView.setPreserveRatio(true);
        Label hintLabel = new Label("Press ENTER to continue...");
        hintLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #777777;");

        VBox vbox = new VBox(10, imageView, label, hintLabel);
        vbox.setAlignment(Pos.CENTER);
        Scene scene1 = new Scene(vbox, 1200,695);
        stage.setScene(scene1);
        stage.setTitle("A Day in A City");
        stage.show();
        String fullText = "You ate breakfast";
        for(int i = 0; i < game.getMorningThings().size(); i++) {
            if(game.getMorningThings().get(i).equalsIgnoreCase("Eat Breakfast")) {
                game.getMorningThings().remove(i);
            }
        }
        
        typeText(label, fullText, Duration.millis(50), () -> {
            // This code will run after the text is fully displayed
                scene1.setOnKeyPressed(event -> {
                if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                    showScene3(stage, fullText, game, transitTime);
                }
            });
        });
    }

    public void showScene3c(Stage stage, String name, GameLogic game, int transitTime) {
        Label label = new Label();
        label.setStyle("-fx-font-size: 24px; -fx-text-fill: black;");

        Image image = new Image("file:/workspaces/final-project-chenzhil1/JavaAPIProject/src/main/java/com/example/images/dressed.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(600);
        imageView.setFitHeight(400);
        imageView.setPreserveRatio(true);
        Label hintLabel = new Label("Press ENTER to continue...");
        hintLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #777777;");

        VBox vbox = new VBox(10, imageView, label, hintLabel);
        vbox.setAlignment(Pos.CENTER);
        Scene scene1 = new Scene(vbox, 1200,695);
        stage.setScene(scene1);
        stage.setTitle("A Day in A City");
        stage.show();
        String fullText = "You got dressed";
        for(int i = 0; i < game.getMorningThings().size(); i++) {
            if(game.getMorningThings().get(i).equalsIgnoreCase("Get dressed")) {
                game.getMorningThings().remove(i);
            }
        }
        
        typeText(label, fullText, Duration.millis(50), () -> {
            // This code will run after the text is fully displayed
                scene1.setOnKeyPressed(event -> {
                if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                    showScene3(stage, fullText, game, transitTime);
                }
            });
        });
    }


    


}
