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

public class Main extends Application { // Main class for the JavaFX application, game starts here
    private static boolean debug = false;
    private static final ArrayList<String> yesNoOption = new ArrayList<>(Arrays.asList("Yes", "No"));
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if(debug) { //Concole version, now stopped development
            System.out.println("Text Debug mode is enabled");
            GameLogic game = new GameLogic();
            //Initialize the game
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
            // Display the morning actions
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
                    System.out.println(game.getWeather());
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
                if(game.getSchoolMinute() >= 30) { //Force leave home 30 minutes before school starts
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
            if(transitTime >= 60) { //Check if the transit time is too long else drive
                transitTime = Map.getTransitTime(Map.getLatitude(), Map.getLongitude(), Map.getSchoolLatitude(), Map.getLongitude(), "drive");
                System.out.println("You chose to drive to school.");
            }
            else {
                //Check if you can walk to school
                if(Map.getTransitTime(Map.getLatitude(), Map.getLongitude(), Map.getSchoolLatitude(), Map.getSchoolLongitude(), "walk") > transitTime) {
                    System.out.println("You chose to take the public transit to school.");
                }
                else {
                    System.out.println("You chose to walk to school.");
                }
            }
            //Weather event when exit home
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

        
        launch(args); // Launch the JavaFX application for GUI version
        }
        scanner.close();
    }
    @Override

    public void start(Stage primaryStage) {
        GameLogic game = new GameLogic();
        //Alert user about the game GUI location
        System.out.println();
        System.out.println("Welcome to A Day in A City!");
        System.out.println("Game by Chen Zhi Lin");
        System.out.println("GUI is open on the next browser tab");
        System.out.println("If you want to play the console version with more features, please run the program with debug mode enabled");
        System.out.println("Currently working until first selection acton, check weather, check transit time, pack bag, play with phone are not ready"); 

        // Ask for user's name using a popup dialog
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("A Day in A City");
        dialog.setHeaderText("Please enter your name");
        dialog.setContentText("Name:");
        Optional<String> result = dialog.showAndWait();

        if (!result.isPresent()) return;
        String name = result.get();
        //After the user enters their name, initialize the game
        int transitTime = Map.getTransitTime(Map.getLatitude(), Map.getLongitude(), Map.getSchoolLatitude(), Map.getSchoolLongitude(), "transit");
        System.out.println(Map.getLatitude());
        System.out.println(Map.getLongitude());
        System.out.println(Map.getSchoolLatitude());
        System.out.println(Map.getSchoolLongitude());
        if(transitTime >= 60) { //Check if the transit time is too long
            game.setSchoolHour(8, 30);
        }
        else if (transitTime > 120) { 
            game.setSchoolHour(6,transitTime /10 * 10 + 10);
            game.updateTime(1);
        }
        showScene(primaryStage, name, game, transitTime, "scene1"); // Show the first scene
    }


    public void typeText(Label label, String fullText, Duration delay, Runnable onFinished) {
        //This method is used to display the text in a typewriter effect
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


    public void handleMorningChoice(String action, String name, GameLogic game, int transitTime, Stage stage) {
        //This method handles the user's choice in the morning scene
        switch(action) {
            case "Brush your teeth":
                System.out.println("You brush your teeth.");
                game.addTime(5);
                showScene(stage, action, game, transitTime, "scene3a");
                return; 
            case "Eat breakfast":
                System.out.println("You cooked and ate breakfast.");
                game.addTime(30);
                showScene(stage, action, game, transitTime, "scene3b");
                return;
            case "Get dressed":
                System.out.println("You got dressed.");
                game.addTime(10);
                showScene(stage, action, game, transitTime, "scene3c"); 
                return;
            case "Check weather":
                game.addTime(5);
                showScene(stage, action, game, transitTime, "scene3d");
                return;
            case "Check transit time":
                System.out.println("The transit time to school is " + transitTime + " minutes.");
                System.out.println("School starts at " + game.getSchoolHour() + ":" + game.getSchoolMinute());
                game.addTime(5);
                showScene(stage, action, game, transitTime, "scene3e");
                return;
            case "Pack your bag":
                game.checkReady();
                game.addTime(10);
                showScene(stage, name, game, transitTime, "scene3f", yesNoOption);
                return;
            case "Play with phone":
                int phoneTime = (int)(Math.random() * 6 + 1) * 5;
                game.addTime(phoneTime);
                System.out.println("You played with your phone.");
                showScene(stage, name, game, transitTime, "scene3g");
                return;
            case "Exit home":
                notReady(stage, name, game, transitTime);
                showScene(stage, name, game, transitTime, "scene4");
            return;
        }

    }

    public void notReady(Stage stage, String name, GameLogic game, int transitTime) {
        //This method is used to display a message when the feature is not ready yet
        Label label = new Label();
        label.setStyle("-fx-font-size: 24px; -fx-text-fill: black;");
        Label hintLabel = new Label("Press ENTER to continue...");
        hintLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #777777;");

        VBox vbox = new VBox(10, label, hintLabel);
        vbox.setAlignment(Pos.CENTER);
        Scene scene1 = new Scene(vbox, 1200,695);
        stage.setScene(scene1);
        stage.setTitle("A Day in A City");
        stage.show();
        String fullText = "This feature is not ready yet.";
        
        typeText(label, fullText, Duration.millis(50), () -> {
            // This code will run after the text is fully displayed
                scene1.setOnKeyPressed(event -> {
                if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                    showScene(stage, name, game, transitTime, "scene3", game.getMorningThings());
                }
            });
        });
    }

    public void showScene(Stage stage, String name, GameLogic game, int transitTime, String sceneID) {
        //This method is used to display the scene based on the sceneID
        Label label = new Label();
        label.setStyle("-fx-font-size: 24px; -fx-text-fill: black;");
        String fullText = "";
        Image image = null;

        switch(sceneID) {
            case "scene1":
                fullText = "Good morning " + name + "!\n" +
                "It is currently " + game.getTime() + " on " + game.getDate() + "\n" +
                "You are a student at " + Map.getSchoolName() + " in " + Map.getCity() + ", " + Map.getState();
                image = new Image("file:/workspaces/final-project-chenzhil1/JavaAPIProject/src/main/java/com/example/images/wakeup.png");
                break;
            case "scene2":
                fullText = "You just woke up, school starts at " + game.formatTime(game.getSchoolHour(), game.getSchoolMinute());
                image = new Image("file:/workspaces/final-project-chenzhil1/JavaAPIProject/src/main/java/com/example/images/wakeup.png");
                break;
            case "scene3a":
                fullText = "You brushed your teeth." + "\n" + "It is now " + game.getTime();
                image = new Image("file:/workspaces/final-project-chenzhil1/JavaAPIProject/src/main/java/com/example/images/brush.png");
                break;
            case "scene3b":
                fullText = "You ate breakfast" + "\n" + "It is now " + game.getTime();
                image = new Image("file:/workspaces/final-project-chenzhil1/JavaAPIProject/src/main/java/com/example/images/breakfast.png");
                break;
            case "scene3c":
                fullText = "You got dressed"  + "\n" + "It is now " + game.getTime();
                image = new Image("file:/workspaces/final-project-chenzhil1/JavaAPIProject/src/main/java/com/example/images/dressed.png");
                break;
            case "scene3d":
                fullText = game.getWeather()  + "\n" + "It is now " + game.getTime();
                image = new Image("file:/workspaces/final-project-chenzhil1/JavaAPIProject/src/main/java/com/example/images/weather.png");
                break;
            case "scene3e":
                fullText = "The transit time to school is " + transitTime + " minutes." + "\n" + "It is now " + game.getTime();
                image = new Image("file:/workspaces/final-project-chenzhil1/JavaAPIProject/src/main/java/com/example/images/transit.png");
                break;
            case "scene3f1":
                fullText = "You brought your lunch.";
                image = new Image("file:/workspaces/final-project-chenzhil1/JavaAPIProject/src/main/java/com/example/images/lunch.png");
                break;
            case "scene3f1a":
                fullText = "You did not bring your lunch.";
                image = new Image("file:/workspaces/final-project-chenzhil1/JavaAPIProject/src/main/java/com/example/images/table.png");
                break;
            case "scene3f2":
                fullText = "You brought an umbrella.";
                image = new Image("file:/workspaces/final-project-chenzhil1/JavaAPIProject/src/main/java/com/example/images/umbrella.png");
                break;
            case "scene3f2a":
                fullText = "You did not bring an umbrella.";
                image = new Image("file:/workspaces/final-project-chenzhil1/JavaAPIProject/src/main/java/com/example/images/table.png");
                break;
            case "scene3f3":
                if(game.getReady() == 0) {
                    fullText = "You forgot your homework! Phew, good that you checked your bag.";
                    image = new Image("file:/workspaces/final-project-chenzhil1/JavaAPIProject/src/main/java/com/example/images/homework.png");
                    
                }
                else if(game.getReady() == 1) {
                    fullText = "You forgot your textbook! Phew, good that you checked your bag.";
                    image = new Image("file:/workspaces/final-project-chenzhil1/JavaAPIProject/src/main/java/com/example/images/textbook.png");
                }
                else {
                    fullText = "You checked your bag and everything is ready to go!";
                    image = new Image("file:/workspaces/final-project-chenzhil1/JavaAPIProject/src/main/java/com/example/images/bag.png");
                }
                System.out.println(game.getReady());
                break;
            case "scene3f3a":
                fullText = "You feel confident about yourself.";
                image = new Image("file:/workspaces/final-project-chenzhil1/JavaAPIProject/src/main/java/com/example/images/bag.png");
                break;
            case "scene3f3b":
                fullText = "You checked your bag" + "\n" + "It is now " + game.getTime();
                image = new Image("file:/workspaces/final-project-chenzhil1/JavaAPIProject/src/main/java/com/example/images/bag.png");
                break;
            case "scene3g":
                fullText = "You played with your phone." + "\n" + "It is now " + game.getTime();
                image = new Image("file:/workspaces/final-project-chenzhil1/JavaAPIProject/src/main/java/com/example/images/phone.png");
                break;
            case "scene4":
                

            default:
                break;
        }
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(600);
            imageView.setFitHeight(400);
            imageView.setPreserveRatio(true);
            Label hintLabel = new Label("Press ENTER to continue...");
            hintLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #777777;");

        VBox vbox = new VBox(10, imageView, label, hintLabel); 
        vbox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox, 1200,695);
        stage.setScene(scene);
        stage.setTitle("A Day in A City");
        stage.show();
        typeText(label, fullText, Duration.millis(50), () -> {
            // This code will run after the text is fully displayed
                scene.setOnKeyPressed(event -> {
                if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                    switch (sceneID) {
                        case "scene1":
                            showScene(stage, name, game, transitTime, "scene2");
                            break;
                        case "scene2":
                            showScene(stage, name, game, transitTime, "scene3", game.getMorningThings());
                            break;
                        case "scene3a":
                            for(int i = 0; i < game.getMorningThings().size(); i++) {
                                if(game.getMorningThings().get(i).equalsIgnoreCase("Brush your teeth")) {
                                    game.getMorningThings().remove(i);
                                }
                            }
                            showScene(stage, name, game, transitTime, "scene3", game.getMorningThings());
                            break;
                        case "scene3b":
                            for(int i = 0; i < game.getMorningThings().size(); i++) {
                                if(game.getMorningThings().get(i).equalsIgnoreCase("Eat Breakfast")) {
                                    game.getMorningThings().remove(i);
                                }
                            }
                            showScene(stage, name, game, transitTime, "scene3", game.getMorningThings());
                            break;
                        case "scene3c":
                            for(int i = 0; i < game.getMorningThings().size(); i++) {
                                if(game.getMorningThings().get(i).equalsIgnoreCase("Get dressed")) {
                                    game.getMorningThings().remove(i);
                                }
                            }
                            showScene(stage, name, game, transitTime, "scene3", game.getMorningThings());
                            break;
                        case "scene3d":
                        case "scene3e":
                        case "scene3g":
                            showScene(stage, name, game, transitTime, "scene3", game.getMorningThings());
                            break;
                        case "scene3f1":
                        case "scene3f1a":
                            showScene(stage, name, game, transitTime, "scene3f2", yesNoOption);
                            break;
                        case "scene3f2":
                        case "scene3f2a":
                            showScene(stage, name, game, transitTime, "scene3f3", yesNoOption);
                            break;
                        case "scene3f3":
                        case "scene3f3a":
                            showScene(stage, name, game, transitTime, "scene3f3b");
                            break;
                        case "scene3f3b":
                            showScene(stage, name, game, transitTime, "scene3", game.getMorningThings());
                            break;
                        default:
                        
                            break;
                    }
                }
            });
        });

    }

    public void showScene(Stage stage, String name, GameLogic game, int transitTime, String sceneID, ArrayList<String> buttonsList) {
        //This method is used when a scene allows user dicision.
        Label label = new Label();
        label.setStyle("-fx-font-size: 24px; -fx-text-fill: black;");
        VBox buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.CENTER); // <-- Add this line
        ArrayList<Button> buttons = new ArrayList<Button>();
        if(sceneID.equals("scene3")) {
            label.setText("What do you want to do now?");
        }
        else if(sceneID.equals("scene3f")) {
            label.setText("Do you want to bring your lunch?");
        }
        else if (sceneID.equals("scene3f2")) {
            label.setText("Do you want to bring an umbrella?");
        }
        else if(sceneID.equals("scene3f3")) {
            label.setText("Do you want to check over your bag?");
        }
            


        for(String action : buttonsList) {
            Button button = new Button(action);
            button.setStyle("-fx-font-size: 18px; -fx-padding: 10px 20px;");
            if(sceneID.equals("scene3")) {
                button.setOnAction(e -> handleMorningChoice(action, name, game, transitTime, stage));
            }
            else if(sceneID.equals("scene3f")) {
                button.setOnAction(e -> yesNoOptions(stage, name, game, transitTime, sceneID, action));
            }
            else if(sceneID.equals("scene3f2")) {
                button.setOnAction(e -> yesNoOptions(stage, name, game, transitTime, sceneID, action));
            }
            else if(sceneID.equals("scene3f3")) {
                button.setOnAction(e -> yesNoOptions(stage, name, game, transitTime, sceneID, action));
            }
            else {
                button.setOnAction(e -> showScene(stage, name, game, transitTime, action));
            }

            buttons.add(button);
        Timeline timeline = new Timeline();
        for(int i = 0; i < buttons.size(); i++) {
            final int index = i;
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.1 * i), event -> {
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
    }

    public void yesNoOptions(Stage stage, String name, GameLogic game, int transitTime, String sceneID, String action) {
        //This method is used to display the yes/no options
        if(sceneID.equals("scene3f")) {
            if(action.equals("Yes")) {
                showScene(stage, name, game, transitTime, "scene3f1");
            }
            else if(action.equals("No")) {
                showScene(stage, name, game, transitTime, "scene3f1a");
            }

        }
        else if(sceneID.equals("scene3f2")) {
            if(action.equals("Yes")) {
                showScene(stage, name, game, transitTime, "scene3f2");
            }
            else if(action.equals("No")) {
                showScene(stage, name, game, transitTime, "scene3f2a");
            }
        }
        else if(sceneID.equals("scene3f3")) {
            if(action.equals("Yes")) {
                showScene(stage, name, game, transitTime, "scene3f3");
            }
            else if(action.equals("No")) {
                showScene(stage, name, game, transitTime, "scene3f3a");
            }
        }
    }
    


}
