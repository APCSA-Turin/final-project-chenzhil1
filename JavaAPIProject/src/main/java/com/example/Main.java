
package com.example;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
            int transitTime = game.getHomeSchoolTravelTime();
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
                    System.out.println("The drive time to school is " + transitTime + " minutes.");
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
        System.out.println("GUI is open on the next browser tab or as a pop up window");
        System.out.println("If you want to play the console version with more features, please run the program with debug mode enabled");
        System.out.println("Currently working fixing bugs"); 
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(695);

        // Ask for user's name using a popup dialog
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("A Day in A City");
        dialog.setHeaderText("Please enter your name");
        dialog.setContentText("Name:");
        Optional<String> result = dialog.showAndWait();

        if (!result.isPresent()) return;
        String name = result.get();
        //After the user enters their name, initialize the game
        if(game.getHomeSchoolTravelTime() >= 60) {
            game.setSchoolHour(8, 30);
        }
        else if (game.getHomeSchoolTravelTime() > 120) {
            game.setSchoolHour(6,game.getHomeSchoolTravelTime() /10 * 10 + 10);
            game.updateTime(1);
        }
        showScene(primaryStage, name, game, "scene1"); // Show the first scene
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


    public void handleMorningChoice(String action, String name, GameLogic game, Stage stage) {
        //This method handles the user's choice in the morning scene
        if((game.getHour() >= game.getSchoolHour() && game.getMinute() >= game.getSchoolMinute() - 15) ||
            (game.getHour() > game.getSchoolHour() || (game.getHour() == game.getSchoolHour() - 1 && game.getMinute() >= (game.getSchoolMinute() + 45)))) {
            showScene(stage, name, game, "sceneForce");
        }
        else {
            switch(action) {
                case "Brush your teeth":
                    System.out.println("You brush your teeth.");
                    game.addTime(5);
                    game.setMorningStatus(0, true);
                    showScene(stage, action, game, "scene3a");
                    return; 
                case "Eat breakfast":
                    System.out.println("You cooked and ate breakfast.");
                    game.setMorningStatus(1, true);
                    game.addTime(30);
                    showScene(stage, action, game, "scene3b");
                    return;
                case "Get dressed":
                    System.out.println("You got dressed.");
                    game.addTime(10);
                    game.setMorningStatus(2, true);
                    showScene(stage, action, game, "scene3c");
                    return;
                case "Check weather":
                    game.addTime(5);
                    showScene(stage, action, game, "scene3d");
                    return;
                case "Check transit time":
                    System.out.println("The transit time to school is " + game.getHomeSchoolTravelTime() + " minutes.");
                    System.out.println("School starts at " + game.getSchoolHour() + ":" + game.getSchoolMinute());
                    game.addTime(5);
                    showScene(stage, action, game, "scene3e");
                    return;
                case "Pack your bag":
                    game.checkReady();
                    game.addTime(10);
                    showScene(stage, name, game, "scene3f", yesNoOption);
                    return;
                case "Play with phone":
                    int phoneTime = (int)(Math.random() * 6 + 1) * 5;
                    game.addTime(phoneTime);
                    System.out.println("You played with your phone.");
                    showScene(stage, name, game, "scene3g");
                    return;
                case "Exit home":
                    showScene(stage, name, game, "scene4");
                return;
            }
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
                    showScene(stage, name, game, "scene3", game.getMorningThings());
                }
            });
        });
    }

    public void showScene(Stage stage, String name, GameLogic game, String sceneID) {
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
                image = new Image(getClass().getResource("/com/example/images/wakeup.png").toExternalForm());
                break;
            case "scene2":
                fullText = "You just woke up, school starts at " + game.formatTime(game.getSchoolHour(), game.getSchoolMinute());
                image = new Image(getClass().getResource("/com/example/images/wakeup.png").toExternalForm());
                break;
            case "scene3a":
                fullText = "You brushed your teeth." + "\n" + "It is now " + game.getTime();
                image = new Image(getClass().getResource("/com/example/images/brush.png").toExternalForm());
                break;
            case "scene3b":
                fullText = "You ate breakfast" + "\n" + "It is now " + game.getTime();
                image = new Image(getClass().getResource("/com/example/images/breakfast.png").toExternalForm());
                break;
            case "scene3c":
                fullText = "You got dressed"  + "\n" + "It is now " + game.getTime();
                image = new Image(getClass().getResource("/com/example/images/dressed.png").toExternalForm());
                break;
            case "scene3d":
                fullText = game.getWeather()  + "\n" + "It is now " + game.getTime();
                image = new Image(getClass().getResource("/com/example/images/weather.png").toExternalForm());
                break;
            case "scene3e":
                fullText = "The transit time to school is " + game.getHomeSchoolTravelTime() + " minutes." + "\n" + "It is now " + game.getTime();
                image = new Image(getClass().getResource("/com/example/images/transit.png").toExternalForm());
                break;
            case "scene3f1":
                game.setLunch(true);
                fullText = "You brought your lunch.";
                image = new Image(getClass().getResource("/com/example/images/lunch.png").toExternalForm());
                break;
            case "scene3f1a":
                fullText = "You did not bring your lunch.";
                image = new Image(getClass().getResource("/com/example/images/table.png").toExternalForm());
                break;
            case "scene3f2":
                game.setUmbrellaPack(true);
                fullText = "You brought an umbrella.";
                image = new Image(getClass().getResource("/com/example/images/umbrella.png").toExternalForm());
                break;
            case "scene3f2a":
                fullText = "You did not bring an umbrella.";
                image = new Image(getClass().getResource("/com/example/images/table.png").toExternalForm());
                break;
            case "scene3f3":
                game.addTime(5);
                game.updateTime(0);
                if(game.getReady() == 0) {
                    game.setHomework(true);
                    fullText = "You forgot your homework! Phew, good that you checked your bag.";
                    image = new Image(getClass().getResource("/com/example/images/homework.png").toExternalForm());
                    
                }
                else if(game.getReady() == 1) {
                    game.setTextbook(true);
                    fullText = "You forgot your textbook! Phew, good that you checked your bag.";
                    image = new Image(getClass().getResource("/com/example/images/textbook.png").toExternalForm());
                }
                else {
                    fullText = "You checked your bag and everything is ready to go!";
                    image = new Image(getClass().getResource("/com/example/images/bag.png").toExternalForm());
                }
                System.out.println(game.getReady());
                break;
            case "scene3f3a":
                fullText = "You feel confident about yourself.";
                image = new Image(getClass().getResource("/com/example/images/bag.png").toExternalForm());
                break;
            case "scene3f3b":
                fullText = "You checked your bag" + "\n" + "It is now " + game.getTime();
                image = new Image(getClass().getResource("/com/example/images/bag.png").toExternalForm());
                break;
            case "scene3g":
                fullText = "You played with your phone." + "\n" + "It is now " + game.getTime();
                image = new Image(getClass().getResource("/com/example/images/phone.png").toExternalForm());
                break;
            case "sceneForce":
                fullText = "Your mom is calling you to leave home.\n" + "You are late for school!\n" + "You have to leave home now!";
                image = new Image(getClass().getResource("/com/example/images/mom.png").toExternalForm());
                break;
            case "scene4":
                fullText = "You left home." + "\n" + "It is now " + game.getTime();
                image = new Image(getClass().getResource("/com/example/images/leavehome.png").toExternalForm());
                break;
            case "scene5":
                fullText = game.weatherEvent();
                if((Weather.getWeather(game.getHour()) >= 51 && Weather.getWeather(game.getHour()) <= 67) || 
                (Weather.getWeather(game.getHour()) >= 80 && Weather.getWeather(game.getHour()) <= 82) ) {
                    if(game.isUmbrellaPack()) {
                        fullText = "You are glad that you brought an umbrella." + "\n" + fullText;
                        image = new Image(getClass().getResource("/com/example/images/rainUmbrella.png").toExternalForm());
                    }
                    else {
                        fullText = "You regret not bringing an umbrella." + "\n" + fullText;
                        image = new Image(getClass().getResource("/com/example/images/rain.png").toExternalForm());
                    }
                }
                else if((Weather.getWeather(game.getHour()) >= 71 && Weather.getWeather(game.getHour()) <= 77)) {
                    if(game.isUmbrellaPack()) {
                        fullText = "You are glad that you brought an umbrella." + "\n" + fullText;
                        image = new Image(getClass().getResource("/com/example/images/snowUmbrella.png").toExternalForm());
                    }
                    else {
                        fullText = "You regret not bringing an umbrella." + "\n" + fullText;
                        image = new Image(getClass().getResource("/com/example/images/snow.png").toExternalForm());
                    }
                }
                else if(Weather.getWeather(game.getHour()) >= 95 && Weather.getWeather(game.getHour()) <= 99) {
                    if(game.isUmbrellaPack()) {
                        fullText = "You are glad that you brought an umbrella." + "\n" + fullText;
                        image = new Image(getClass().getResource("/com/example/images/stormUmbrella.png").toExternalForm());
                    }
                    else {
                        fullText = "You regret not bringing an umbrella." + "\n" + fullText;
                        image = new Image(getClass().getResource("/com/example/images/storm.png").toExternalForm());
                    }
                }
                else {
                    image = new Image(getClass().getResource("/com/example/images/sunny.png").toExternalForm());
                }
                fullText = "The weather now is " + Weather.defineWeather(Weather.getWeather(game.getHour())) + "\n" + fullText;
                break;
            case "scene6":
                game.addTime(game.getHomeSchoolTravelTime());
                game.updateTime(0);
                fullText = "You arrived at school at " + game.getTime() + "\n" + game.checkLateSchool();
                if(game.checkLateSchool().equals("You are late for school!")) {
                    image = new Image(getClass().getResource("/com/example/images/late.png").toExternalForm());
                }
                else {
                    image = new Image(getClass().getResource("/com/example/images/school.png").toExternalForm());
                }
                break;
            case "scene7":
                if(game.getHour() < game.getSchoolHour() || (game.getHour() == game.getSchoolHour() && game.getMinute() < game.getSchoolMinute())) {
                    fullText = "You arrived at school early, you can relax for a while, so you decided to play on your phone.";
                    image = new Image(getClass().getResource("/com/example/images/phoneSchool.png").toExternalForm());
                    int timePassed = game.getSchoolHour() * 60 + game.getSchoolMinute() - (game.getHour() * 60 + game.getMinute());
                    game.setHappiness((int)(game.getHappiness() + 0.5 * timePassed));
                    game.addTime(timePassed);
                    game.updateTime(0);
                }
                else {
                    fullText = "You arrived at school on time.";
                    image = new Image(getClass().getResource("/com/example/images/schoolTable.png").toExternalForm());
                }
                break;
            case "scene8":
                fullText = "Class begins, you are ready to learn!";
                int timePassed = 12 * 60 - (game.getHour() * 60 + game.getMinute());
                game.addTime(timePassed);
                game.updateTime(0);
                image = new Image(getClass().getResource("/com/example/images/class.png").toExternalForm());
                break;
            case "scene8a":
                fullText = "";
                image = new Image(getClass().getResource("/com/example/images/schoolFrustrated.png").toExternalForm());
                if(!game.getMorningStatus()[0]) {
                    fullText += "You did not brush your teeth, your breath stinks.\n";
                    game.setHappiness(game.getHappiness() - 10);
                }
                if(!game.getMorningStatus()[1]) {
                    fullText += "You did not eat breakfast, you feel hungry.\n";
                    game.setHappiness(game.getHappiness() - 10);
                }
                if(!game.getMorningStatus()[2]) {
                    fullText += "You did not get dressed, you feel felt unfit.";
                    game.setHappiness(game.getHappiness() - 10);
                }
                else {
                    fullText = "You are ready for class!";
                }
                break;
            case "scene8b":
                image = new Image(getClass().getResource("/com/example/images/schoolFrustratedBag.png").toExternalForm());
                if(!game.isHomework()) {
                    fullText = "You did not bring your homework, so you got a zero for the day.";
                    game.setHappiness(game.getHappiness() - 10);
                }
                else if(!game.isTextbook()) {
                    fullText = "You did not bring your textbook, so you need to borrow one from a friend.";
                    game.setHappiness(game.getHappiness() - 10);
                }
                else {
                    fullText = "You are ready for class!";
                }
                break;
            case "scene9":
                int goodLunch = (int)(Math.random() * 2);
                if(game.isLunch()) {
                    fullText = "It is now lunch time at " + game.getTime() + "\nYou brought your own lunch, you feel full and happy.";
                    game.setHappiness(game.getHappiness() + 10);
                    image = new Image(getClass().getResource("/com/example/images/lunch.png").toExternalForm());
                }
                else {
                    if(goodLunch == 0) {
                        fullText = "It is now lunch time at " + game.getTime() + "\nYou bought a lunch from the cafeteria, it is not very good, but you are full.";
                        game.setHappiness(game.getHappiness() - 5);
                        image = new Image(getClass().getResource("/com/example/images/lunchBad.png").toExternalForm());
                    }
                    else {
                        fullText = "It is now lunch time at " + game.getTime() + "\nYou bought a lunch from the cafeteria, it is delicious, you feel happy.";
                        game.setHappiness(game.getHappiness() + 10);
                        image = new Image(getClass().getResource("/com/example/images/lunchGood.png").toExternalForm());
                    }
                }
                game.addTime(60);
                game.updateTime(0);
                break;
            case "scene10": 
                fullText = "It's now " + game.getTime() + "\nIt's afternoon class time, you are ready to learn!";
                image = new Image(getClass().getResource("/com/example/images/class.png").toExternalForm());
            break;

            case "scene11":
                int sleepy = (int)(Math.random() * 10);
                if((game.getHappiness() < 100 && game.getHappiness() > 50 && sleepy <= 3) ||
                (game.getHappiness() <= 50 && game.getHappiness() > 20 && sleepy <= 5) || sleepy == 0) {
                    fullText = "You fell asleep in class, you feel tired and frustrated.";
                    game.setHappiness(game.getHappiness() - 10);
                    image = new Image(getClass().getResource("/com/example/images/sleepy.png").toExternalForm());
                }
                else if((game.getHappiness() <= 20 && game.getHappiness() > 0 && sleepy <= 7) || sleepy == 1) {
                    fullText = "You are a bit sleepy in class, you feel tired.";
                    game.setHappiness(game.getHappiness() - 5);
                    image = new Image(getClass().getResource("/com/example/images/tired.png").toExternalForm());
                }
                else {
                    fullText += "You are focused in class, you feel happy.";
                    game.setHappiness(game.getHappiness() + 10);
                    image = new Image(getClass().getResource("/com/example/images/focused.png").toExternalForm());
                }
                game.addTime(3 * 60);
                game.updateTime(0); 
                break;
            case "scene12":
                fullText = "It is now " + game.getTime() + "\nSchool is over!";
                if((Weather.getWeather(game.getHour()) >= 51 && Weather.getWeather(game.getHour()) <= 67) || 
                (Weather.getWeather(game.getHour()) >= 80 && Weather.getWeather(game.getHour()) <= 82) ) {
                    image = new Image(getClass().getResource("/com/example/images/leaveSchoolRain.png").toExternalForm());
                }
                else if((Weather.getWeather(game.getHour()) >= 71 && Weather.getWeather(game.getHour()) <= 77)) {
                    image = new Image(getClass().getResource("/com/example/images/leaveSchoolSnow.png").toExternalForm());
                }
                else if(Weather.getWeather(game.getHour()) >= 95 && Weather.getWeather(game.getHour()) <= 99) {
                    image = new Image(getClass().getResource("/com/example/images/leaveSchoolStorm.png").toExternalForm());
                }
                else {
                    image = new Image(getClass().getResource("/com/example/images/leaveSchool.png").toExternalForm());
                }
                break;
            case "scene14":
                if(game.getDestination().equals("Park")) {
                    if((Weather.getWeather(game.getHour()) >= 51 && Weather.getWeather(game.getHour()) <= 67) ||
                    (Weather.getWeather(game.getHour()) >= 80 && Weather.getWeather(game.getHour()) <= 82) ||
                    (Weather.getWeather(game.getHour()) >= 71 && Weather.getWeather(game.getHour()) <= 77) ||
                    (Weather.getWeather(game.getHour()) >= 95 && Weather.getWeather(game.getHour()) <= 99)) {
                        fullText = "The weather is not suitable for going to the park, you decided to go home instead.";
                        image = new Image(getClass().getResource("/com/example/images/parkBadWeather.png").toExternalForm());
                    }
                    else {
                        fullText = "You are on your way to the park, you feel excited!";
                        image = new Image(getClass().getResource("/com/example/images/sunny.png").toExternalForm());
                    }
                }
                else {
                    fullText = game.weatherEvent();
                    if((Weather.getWeather(game.getHour()) >= 51 && Weather.getWeather(game.getHour()) <= 67) || 
                    (Weather.getWeather(game.getHour()) >= 80 && Weather.getWeather(game.getHour()) <= 82) ) {
                        if(game.isUmbrellaPack()) {
                            fullText = "You are glad that you brought an umbrella." + "\n" + fullText;
                            image = new Image(getClass().getResource("/com/example/images/rainUmbrella.png").toExternalForm());
                        }
                        else {
                            fullText = "You regret not bringing an umbrella." + "\n" + fullText;
                            image = new Image(getClass().getResource("/com/example/images/rain.png").toExternalForm());
                        }
                    }
                    else if((Weather.getWeather(game.getHour()) >= 71 && Weather.getWeather(game.getHour()) <= 77)) {
                        if(game.isUmbrellaPack()) {
                            fullText = "You are glad that you brought an umbrella." + "\n" + fullText;
                            image = new Image(getClass().getResource("/com/example/images/snowUmbrella.png").toExternalForm());
                        }
                        else {
                            fullText = "You regret not bringing an umbrella." + "\n" + fullText;
                            image = new Image(getClass().getResource("/com/example/images/snow.png").toExternalForm());
                        }
                    }
                    else if(Weather.getWeather(game.getHour()) >= 95 && Weather.getWeather(game.getHour()) <= 99) {
                        if(game.isUmbrellaPack()) {
                            fullText = "You are glad that you brought an umbrella." + "\n" + fullText;
                            image = new Image(getClass().getResource("/com/example/images/stormUmbrella.png").toExternalForm());
                        }
                        else {
                            fullText = "You regret not bringing an umbrella." + "\n" + fullText;
                            image = new Image(getClass().getResource("/com/example/images/storm.png").toExternalForm());
                        }
                    }
                    else {
                        image = new Image(getClass().getResource("/com/example/images/sunny.png").toExternalForm());
                    }
                    fullText = "You are now heading home.\nThe weather now is " + Weather.defineWeather(Weather.getWeather(game.getHour())) + "\n" + fullText;
                }
                break;
            case "scene15":
                game.addTime(game.getSchoolParkTravelTime());
                game.updateTime(0);
                fullText = "You arrived at " + Map.getParkName();
                image = new Image(getClass().getResource("/com/example/images/park.png").toExternalForm());
                break;
            case "scene16a":
                fullText = "You played with your friends in the park";
                image = new Image(getClass().getResource("/com/example/images/friends.png").toExternalForm());
                break;
            case "scene16b":
                fullText = "You played basketball in the park";
                image = new Image(getClass().getResource("/com/example/images/basketball.png").toExternalForm());
                break;
            case "scene16c": 
                fullText = "You ran track in the park";
                image = new Image(getClass().getResource("/com/example/images/track.png").toExternalForm());
                break;
            case "scene16d":
                fullText = "You played volleyball in the park";
                image = new Image(getClass().getResource("/com/example/images/volleyball.png").toExternalForm());
                break;
            case "scene16e":
                fullText = "You played soccer in the park";
                image = new Image(getClass().getResource("/com/example/images/soccer.png").toExternalForm());
                break;
            case "scene16f":
                fullText = "You played badminton in the park";
                image = new Image(getClass().getResource("/com/example/images/badminton.png").toExternalForm());
                break;
            case "sceneForce2":
                fullText = "Your mom called you to come home for dinner";
                image = new Image(getClass().getResource("/com/example/images/momCall.png").toExternalForm());
                break;
            case "scene17":
                if(game.getPreviousDestination().equals("Park")) {
                    game.addTime(game.getParkHomeTravelTime());
                }
                else {
                    game.addTime(game.getHomeSchoolTravelTime());
                }
                game.addTime(-1 * (game.getMinute() % 5));
                game.updateTime(0);
                game.setHomeworkTime((int)(Math.random() * 13 + 12) * 5);
                fullText = "You arrived home, it is now " + game.getTime();
                image = new Image(getClass().getResource("/com/example/images/home.png").toExternalForm());
                break;
            case "scene18a":
                int timeHomework = (int) (Math.random() * 6 + 1) * 5;
                if(timeHomework > game.getHomeworkTime() - game.getHomeworkDone()) {
                    timeHomework = game.getHomeworkTime() - game.getHomeworkDone();
                }
                game.setHomeworkDone(timeHomework);
                game.addTime(timeHomework);
                game.updateTime(0);
                fullText = "Ypu have a total of " + game.getHomeworkTime() + " minutes of homework" + "\n"
                + "You have done " + game.getHomeworkDone() + " minutes of work";
                if(game.getHomeworkDone() == game.getHomeworkTime()) {
                    game.finishHomework();
                    fullText += "\nYou have finished doing homework";
                }
                fullText += "\nIt is now " + game.getTime();
                image = new Image(getClass().getResource("/com/example/images/doHomework.png").toExternalForm());
                break;
            case "scene18b":
                int timePhone = (int) (Math.random() * 6 + 1) * 5;
                game.addTime(timePhone);
                game.updateTime(0);
                fullText = "You decided to play on your phone \n" + 
                "It is now " + game.getTime();
                image = new Image(getClass().getResource("/com/example/images/phone.png").toExternalForm());
                break;
            case "scene18c":
                int napTime = (int) (Math.random() * 6 + 1) * 5;
                game.addTime(napTime);
                game.updateTime(0);
                if(napTime > 15) {
                    game.setHomeworkTime(game.getHomeworkTime() - napTime + 15);
                }
                else {
                    game.setHomeworkTime(game.getHomeworkTime() - 5);
                }
                fullText = "You decided to take a nap \n" +
                "it is now " + game.getTime() + "\n You feel more productive.";
                image = new Image(getClass().getResource("/com/example/images/nap.png").toExternalForm());
            case "sceneForce3":
                fullText = "It is now " + game.getTime() + "\nYour mom came in to call you for dinner";
                if(game.getHomeworkDone() > (double)(game.getHomeworkTime()) / 8.0) {
                    image = new Image(getClass().getResource("/com/example/images/dinner.png").toExternalForm());
                }
                else {
                    fullText += "\nYou have not done much of your homework";
                    image = new Image(getClass().getResource("/com/example/images/dinnerAngry.png").toExternalForm());
                }
                game.addTime(60);
                game.updateTime(0);
                break;
            case "scene19":
                fullText = "You finished your dinner, it is now " + game.getTime();
                image = new Image(getClass().getResource("/com/example/images/dinnerFinish.png").toExternalForm());
                break;
            case "sceneForce4":
                fullText = "It is " + game.getTime() + "\nYou have to take a shower now.";
                image = new Image(getClass().getResource("/com/example/images/shower.png").toExternalForm()); 
                break;
            case "scene20":
                fullText = "You came back from the shower, " + "\nIt is now " + game.getTime();
                image = new Image(getClass().getResource("/com/example/images/showerFinish.png").toExternalForm());
                break;
            case "scene21":
                fullText = "It is now " + game.getTime() + "\nYou are going to brush your teeth";
                image = new Image(getClass().getResource("/com/example/images/brush.png").toExternalForm());
                game.addTime(10);
                game.updateTime(0);
                break;
            case "scene22":
                fullText = "It is now " + game.getTime() + "\nYou are ready to sleep" + 
                "\nGood night!";
                image = new Image(getClass().getResource("/com/example/images/sleep.png").toExternalForm());
                break;

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
        Scene scene = new Scene(vbox);
        // Save current window size and position
        double x = stage.getX();
        double y = stage.getY();
        double width = stage.getWidth();
        double height = stage.getHeight();
        boolean wasMaximized = stage.isMaximized();

        // Set the new scene
        stage.setScene(scene);
        stage.setTitle("A Day in A City");
        stage.show();

        // Restore window size and position if not maximized
        if (wasMaximized) {
            stage.setMaximized(true);
        } else {
            stage.setX(x);
            stage.setY(y);
            stage.setWidth(width);
            stage.setHeight(height);
        }
        typeText(label, fullText, Duration.millis(50), () -> {
            // This code will run after the text is fully displayed
                scene.setOnKeyPressed(event -> {
                if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                    switch (sceneID) {
                        case "scene1":
                            showScene(stage, name, game, "scene2");
                            break;
                        case "scene2":
                            showScene(stage, name, game, "scene3", game.getMorningThings());
                            break;
                        case "scene3a":
                            for(int i = 0; i < game.getMorningThings().size(); i++) {
                                if(game.getMorningThings().get(i).equalsIgnoreCase("Brush your teeth")) {
                                    game.getMorningThings().remove(i);
                                }
                            }
                            showScene(stage, name, game, "scene3", game.getMorningThings());
                            break;
                        case "scene3b":
                            for(int i = 0; i < game.getMorningThings().size(); i++) {
                                if(game.getMorningThings().get(i).equalsIgnoreCase("Eat Breakfast")) {
                                    game.getMorningThings().remove(i);
                                }
                            }
                            showScene(stage, name, game, "scene3", game.getMorningThings());
                            break;
                        case "scene3c":
                            for(int i = 0; i < game.getMorningThings().size(); i++) {
                                if(game.getMorningThings().get(i).equalsIgnoreCase("Get dressed")) {
                                    game.getMorningThings().remove(i);
                                }
                            }
                            showScene(stage, name, game, "scene3", game.getMorningThings());
                            break;
                        case "scene3d":
                        case "scene3e":
                        case "scene3g":
                            showScene(stage, name, game, "scene3", game.getMorningThings());
                            break;
                        case "scene3f1":
                        case "scene3f1a":
                            showScene(stage, name, game, "scene3f2", yesNoOption);
                            break;
                        case "scene3f2":
                        case "scene3f2a":
                            showScene(stage, name, game, "scene3f3", yesNoOption);
                            break;
                        case "scene3f3":
                        case "scene3f3a":
                            showScene(stage, name, game, "scene3f3b");
                            break;
                        case "scene3f3b":
                            showScene(stage, name, game, "scene3", game.getMorningThings());
                            break;
                        case "sceneForce":
                            showScene(stage, name, game, "scene4");
                            break;
                        case "scene4":
                            showScene(stage, name, game, "scene5");
                            break;
                        case "scene5":
                            showScene(stage, name, game, "scene6");
                            break;
                        case "scene6":
                            if(game.checkLateSchool().equals("You are late for school!")) {
                                showScene(stage, name, game, "scene8");
                            }
                            else {
                                showScene(stage, name, game, "scene7");
                            }
                            break;
                        case "scene7":
                            if(!game.getMorningStatus()[0] || !game.getMorningStatus()[1] || !game.getMorningStatus()[2]) {
                                showScene(stage, name, game, "scene8a");
                            }
                            else {
                                if(!game.isHomework() || !game.isTextbook()) {
                                    showScene(stage, name, game, "scene8b");
                                }
                                else {
                                    showScene(stage, name, game,"scene8");
                                }
                            }
                            break;
                        case "scene8a":
                            if(!game.isHomework() || !game.isTextbook()) {
                                showScene(stage, name, game, "scene8b");
                            }
                            else {
                                showScene(stage, name, game, "scene8");
                            }
                            break;
                        case "scene8b":
                            showScene(stage, name, game, "scene8");
                            break;
                        case "scene8" :
                            showScene(stage, name, game, "scene9");
                            break;
                        case "scene9":
                            showScene(stage, name, game, "scene10");
                            break;
                        case "scene10":
                            showScene(stage, name, game, "scene11");
                            break;
                        case "scene11":
                            showScene(stage, name, game, "scene12");
                            break;
                        case "scene12":
                            showScene(stage, name, game, "scene13", game.getAfternoonThings());
                            break;
                        case "scene14" :
                            if(game.getDestination().equals("Park")) {
                                if((Weather.getWeather(game.getHour()) >= 51 && Weather.getWeather(game.getHour()) <= 67) ||
                                (Weather.getWeather(game.getHour()) >= 80 && Weather.getWeather(game.getHour()) <= 82) ||
                                (Weather.getWeather(game.getHour()) >= 71 && Weather.getWeather(game.getHour()) <= 77) ||
                                (Weather.getWeather(game.getHour()) >= 95 && Weather.getWeather(game.getHour()) <= 99)) {
                                    game.setDestination("Home");
                                    showScene(stage, name, game, "scene14");
                                }
                                else {
                                    showScene(stage, name, game, "scene15");
                                }
                            }
                            else {
                                showScene(stage, name, game, "scene17");
                            }
                            break;
                        case "scene15":
                            showScene(stage, name, game, "scene16", game.getParkThings());
                            break;
                        case "scene16a":
                        case "scene16b":
                        case "scene16c":
                        case "scene16d":
                        case "scene16e":
                        case "scene16f":
                            showScene(stage, name, game, "scene16", game.getParkThings());
                            break;
                        case "sceneForce2":
                            game.setDestination("Home");
                            showScene(stage, name, game, "scene14");
                            break;
                        case "scene17":
                            showScene(stage, name, game, "scene18", game.getEveningThings());
                            break;
                        case "scene18a":
                        case "scene18b":
                        case "scene18c":
                        case "scene19":
                        case "scene20":
                            showScene(stage, name, game,"scene18", game.getEveningThings());
                            break;
                        case "sceneForce3":
                            showScene(stage, name, game, "scene19");
                            break;
                        case "sceneForce4":
                            showScene(stage, name, game, "scene20");
                            break;
                        case "scene21":
                            showScene(stage, name, game, "scene22");
                            break;
                        case "scene22":
                            ArrayList<String> endList = new ArrayList<String>(Arrays.asList("Restart game", "Exit Game"));
                            showScene(stage, name, game, "end", endList);
                        default:

                            break;
                    }
                }
            });
        });

    }

    public void showScene(Stage stage, String name, GameLogic game, String sceneID, ArrayList<String> buttonsList) {
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
        else if(sceneID.equals("scene13")) {
            label.setText("Where do you want to go now?");
        }
        else if(sceneID.equals("scene16")) {
            label.setText("It is now " + game.getTime() + "\n" + "What do you want to do now?"); 
        }
        else if(sceneID.equals("scene18")) {
            label.setText("What do you want to do now?");
        }
        else if(sceneID.equals("end")) {
            label.setText("This is the end of the game\n Thank you for playing!" + 
            "Now you may:");
        }
            


        for(String action : buttonsList) {
            Button button = new Button(action);
            button.setStyle("-fx-font-size: 18px; -fx-padding: 10px 20px;");
            if(sceneID.equals("scene3")) {
                button.setOnAction(e -> handleMorningChoice(action, name, game, stage));
            }
            else if(sceneID.equals("scene3f")) {
                button.setOnAction(e -> yesNoOptions(stage, name, game, sceneID, action));
            }
            else if(sceneID.equals("scene3f2")) {
                button.setOnAction(e -> yesNoOptions(stage, name, game, sceneID, action));
            }
            else if(sceneID.equals("scene3f3")) {
                button.setOnAction(e -> yesNoOptions(stage, name, game, sceneID, action));
            }
            else if(sceneID.equals("scene13")) {
                button.setOnAction(e -> yesNoOptions(stage, name, game, sceneID, action));
            }
            else if(sceneID.equals("scene16")) {
                button.setOnAction(e -> handleAfternoonChoice(action, name, game, stage));
            }
            else if(sceneID.equals("scene18")) {
                button.setOnAction(e -> handleEveningChoice(action, name, game, stage));
            }
            else if(sceneID.equals("end")) {
                button.setOnAction(e -> yesNoOptions(stage, name, game, sceneID, action));
            }
            else {
                button.setOnAction(e -> showScene(stage, name, game, action));
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
        Scene scene3 = new Scene(vbox);
        stage.setScene(scene3);
        stage.setTitle("A Day in A City");
        stage.show();

        }
    }

    public void yesNoOptions(Stage stage, String name, GameLogic game, String sceneID, String action) {
        //This method is used to display the yes/no options
        if(sceneID.equals("scene3f")) {
            if(action.equals("Yes")) {
                showScene(stage, name, game, "scene3f1");
            }
            else if(action.equals("No")) {
                showScene(stage, name, game, "scene3f1a");
            }

        }
        else if(sceneID.equals("scene3f2")) {
            if(action.equals("Yes")) {
                showScene(stage, name, game, "scene3f2");
            }
            else if(action.equals("No")) {
                showScene(stage, name, game, "scene3f2a");
            }
        }
        else if(sceneID.equals("scene3f3")) {
            if(action.equals("Yes")) {
                showScene(stage, name, game, "scene3f3");
            }
            else if(action.equals("No")) {
                showScene(stage, name, game, "scene3f3a");
            }
        }
        else if(sceneID.equals("scene13")) {
            if(action.equals("Go to park")) {
                game.setPreviousDestination("School");
                game.setDestination("Park");
            }
            else if(action.equals("Go home")) {
                game.setPreviousDestination("School");
                game.setDestination("Home");
            }
            showScene(stage, name, game, "scene14");
        }
        else if(sceneID.equals("end")) {
            GameLogic newGame = new GameLogic();
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("A Day in A City");
            dialog.setHeaderText("Please enter your name");
            dialog.setContentText("Name:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String newName = result.get();
                if(newGame.getHomeSchoolTravelTime() >= 60) {
                    newGame.setSchoolHour(8, 30);
                }
                else if (newGame.getHomeSchoolTravelTime() > 120) {
                    newGame.setSchoolHour(6, newGame.getHomeSchoolTravelTime() / 10 * 10 + 10);
                    newGame.updateTime(1);
                }
                showScene(stage, newName, newGame, "scene1");
            }
            else if(action.equals("Exit game")) {
                stage.close();
            }
        }
    }

    public void handleAfternoonChoice(String action, String name, GameLogic game, Stage stage) {

        if ((game.getHour() > 17) || (game.getHour() == 17 && game.getMinute() >= 15)) {
            game.setPreviousDestination("Park");
            showScene(stage, name, game, "sceneForce2");
        } 
        else {
            switch(action) {
                case "Play with friends":
                    game.setHappiness(game.getHappiness() + 10);
                    game.addTime(((int) (Math.random() * 6 + 1)) * 5);
                    game.updateTime(0);
                    showScene(stage, name, game, "scene16a");
                    break;
                case "Play basketball":
                    game.setHappiness(game.getHappiness() + 10);
                    game.addTime(((int) (Math.random() * 6 + 1)) * 5);
                    game.updateTime(0);
                    showScene(stage, name, game, "scene16b");
                    break;
                case "Run track":
                    game.setHappiness(game.getHappiness() + 10);
                    game.addTime(((int) (Math.random() * 6 + 1)) * 5);
                    game.updateTime(0);
                    showScene(stage, name, game, "scene16c");
                    break;
                case "Play volleyball":
                    game.setHappiness(game.getHappiness() + 10);
                    game.addTime(((int) (Math.random() * 6 + 1)) * 5);
                    game.updateTime(0);
                    showScene(stage, name, game, "scene16d");
                    break;
                case "Play soccer":
                    game.setHappiness(game.getHappiness() + 10);
                    game.addTime(((int) (Math.random() * 6 + 1)) * 5);
                    game.updateTime(0);
                    showScene(stage, name, game, "scene16e");
                    break;
                case "Play badminton":
                    game.setHappiness(game.getHappiness() + 7);
                    game.addTime(((int) (Math.random() * 6 + 1)) * 5);
                    game.updateTime(0);
                    showScene(stage, name, game, "scene16f");
                    break;
                case "Go home":
                    game.setPreviousDestination("Park");
                    game.setDestination("Home");
                    showScene(stage, name, game, "scene14");
                    break;
                default:
                    break;
            }
        }
    }

    public void handleEveningChoice(String action, String name, GameLogic game, Stage stage) {
        if ((game.getHour() >= 18) && !game.getDinner()) {
            game.setDinner(true);
            showScene(stage, name, game, "sceneForce3");
        }
        else if ((game.getHour() >= 20) && !game.getShower()) {
            game.setShower(true);
            showScene(stage, name, game, "sceneForce4");
        }
        else if(game.getHour() >= 23) {
            showScene(stage, name, game, "scene21");
        }
        else {
            switch(action) {
                case "Do homework":
                    showScene(stage, name, game, "scene18a");
                    break;
                case "Play on phone":
                    showScene(stage, name, game, "scene18b");
                    break;
                case "Take a nap":
                    showScene(stage, name, game, "scene18c");
                    break;
            }
        }

    }
}

