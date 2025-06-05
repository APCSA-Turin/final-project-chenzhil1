package com.example;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class GameLogic { //Main game logic class, accessed by the main class for both GUI and console mode
    //Non static variables to allow restarting the game
    private int age; //Player's age radomly generated between 10 and 18
    //Date and time variables
    //Date is randomly generated between 20XX-01-01 and 20XX-12-28
    private String date;
    private String time;
    //Time is initialized to 6:00 AM, and can be updated
    //Hour and minute are used to keep track of the time
    private int hour;
    private int minute;
    //Role of the player, can be Student, Teacher, or Parent, but currently only Student is implemented
    private String role;
    private boolean debug = false; //Debug mode used to print debug information

    private int happiness = (int) (Math.random() * 30 + 50); //Random happiness value between 50 and 80
    private int schoolHour = 8; // School hour is initialized to 8 AM, can be updated
    private int schoolMinute = 0;
    private int ready = -1; //Check bag item initialized as 0, which means no item is checked
    private boolean umbrellaPack = false;
    private boolean lunch = false;
    //List of available morning activities, used for GUI button list
    private ArrayList<String> morningThings = new ArrayList<String>(Arrays.asList("Brush your teeth", "Eat breakfast", "Get dressed", "Check weather", "Check transit time", "Pack your bag", "Play with phone", "Exit home"));
    private ArrayList<String> afternoonThings = new ArrayList<String>(Arrays.asList("Go to park", "Go home"));
    private ArrayList<String> parkThings = new ArrayList<String>(Arrays.asList("Play with friends", "Play basketball", "Run track", "Play volleyball", "Play soccer", "Play badminton", "Go home"));
    private ArrayList<String> eveningThings = new ArrayList<String>(Arrays.asList("Do homework", "Play on phone", "Take a nap"));
    private boolean textbook = false;
    private boolean homework = false;
    private boolean[] morningStatus = new boolean[3]; //Array to keep track of morning activities, 0: Brush teeth, 1: Eat breakfast, 2: Get dressed
    private String destination = ""; //Destination for the player to go to, used for GUI button
    private int homeSchoolTravelTime = 0; //Travel time from home to school, used for GUI button
    private int schoolParkTravelTime = 0; //Travel time from school to park, used for GUI button
    private int ParkHomeTravelTime = 0; //Travel time from park to home, used for GUI button
    private String previousDestination = "";
    private int homeworkTime = 0;
    private int homeworkDone = 0;
    private boolean dinner = false;
    private boolean shower = false;
    public GameLogic() {
        this.age = 0;
        this.date = "20XX-" + (int) (Math.random() * 12 + 1) + "-" + (int) (Math.random() * 28 + 1); // Random date;
        this.time = "";
        this.hour = 6;
        this.minute = 0;
        this.role = "Student";
        this.happiness = 0;
        Map.getMap(); //Initialize random location
        Weather.findWeather(); // Initialize weather
        updateTime(0); // Update time to 6:30 AM for the morning
        // Initialize game state
        if(role.equalsIgnoreCase("Student")) { //Check type of school based on age
            age = (int) (Math.random() * 9 + 10); 
            if(age >= 10 && age <= 11) {
                Map.setSchoolType("Elementary");
            } else if(age >= 12 && age <= 14) {
                Map.setSchoolType("Middle");
            } else if(age >= 15 && age <= 18) {
                Map.setSchoolType("High");
            } else {
                Map.setSchoolType("College");
            }
            Map.findSchool(); //Find school based on type
        }
        Map.findPark();
        homeSchoolTravelTime = Map.getTransitTime(Map.getLatitude(), Map.getLongitude(), Map.getSchoolLatitude(), Map.getSchoolLongitude(), "drive");
        if(homeSchoolTravelTime > 120) {
            homeSchoolTravelTime = 120;
        }
        schoolParkTravelTime = Map.getTransitTime(Map.getSchoolLatitude(), Map.getSchoolLongitude(), Map.getParkLatitude(), Map.getParkLongitude(), "drive");
        if(homeSchoolTravelTime > 120) {
            homeSchoolTravelTime = 120;
        }
        ParkHomeTravelTime = Map.getTransitTime(Map.getParkLatitude(), Map.getParkLongitude(), Map.getLatitude(), Map.getLongitude(), "drive");
        if(ParkHomeTravelTime > 120) {
            ParkHomeTravelTime = 120;
        }
        if(schoolParkTravelTime == -1) {
            schoolParkTravelTime = 10;
            ParkHomeTravelTime = 10;
        }
        if(debug) {
            System.out.println(homeSchoolTravelTime);
            System.out.println(schoolParkTravelTime);
            System.out.println(ParkHomeTravelTime);

        }
    }



    public int getAge() {
        return age;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void updateTime(int type) { //Format the time correctly based on minutes and hours
        if(type == 0) {
            if(minute >= 60) {
                hour += minute / 60;
                minute = minute % 60;
            }
            if(hour >= 24) {
                hour = hour % 24;
                date = date.substring(0, 8) + (Integer.parseInt(date.substring(8)) + 1);
            }
            time = formatTime(hour, minute);
        }
        else if(type == 1) {
            if(minute >= 60) {
                schoolHour += minute / 60;
                schoolMinute = minute % 60;
            }
        }
    }
    public void setTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
        updateTime(0);
    }
    public void setSchoolHour(int hour, int minute) {
        this.schoolHour = hour;
        this.schoolMinute = minute;
    }

    public int getSchoolHour() {
        return schoolHour;
    }
    public int getSchoolMinute() {
        return schoolMinute;
    }

    public ArrayList<String> getMorningThings() {
        return morningThings;
    }

    public void addTime(int minute) { //Add time to the current time, used for morning activities
        this.minute += minute;
        updateTime(0);
    }

    public String getWeather() { //Get the current weather information based on the hour
        String info = "";
        info += "The weather now is " + Weather.defineWeather(Weather.getWeather(hour)) + "\n";
        for(int i = hour; i < 24; i++) {
            if(((Weather.getWeather(i) >= 51 && Weather.getWeather(i) <= 67)) || (Weather.getWeather(i) >= 80 && Weather.getWeather(i) <= 82) || (Weather.getWeather(i) >= 95 && Weather.getWeather(i) <= 99)) {
                info += "It will rain today.\n";
                break;
            }
        }
        for(int i = hour; i < 24; i++) {
            if(((Weather.getWeather(i) >= 71 && Weather.getWeather(i) <= 77)) || (Weather.getWeather(i) >85 && Weather.getWeather(i) <= 86)) {
                info += "It will snow today.\n";
                break;
            }
        }
        info += "The temperature now is " + Weather.getTemperature(hour) + "°C\n";
        double high = Integer.MIN_VALUE;
        double low = Integer.MAX_VALUE;
        for(int i = hour; i < 24; i++) {
            if(Weather.getTemperature(i) > high) {
                high = Weather.getTemperature(i);
            }
            if(Weather.getTemperature(i) < low) {
                low = Weather.getTemperature(i);
            }
        }
        info += "The temperature today is " + low + "°C to " + high + "°C";
        return info;
    }

    public void checkReady() {
        ready = (int) (Math.random() * 3);
        if(ready == 0) {
            homework = true;
        }
        else if(ready == 1) {
            textbook = true;
        }
        else {
            homework = true;
            textbook = true;
        }
    }

    public int getReady() {
        return ready;
    }

    public void packBag() { //Method to pack the bag, used for GUI button
        Scanner scanner = new Scanner(System.in);

        checkReady(); // Check if the player is ready for school, randomly generated between 0 and 2

        String check = "";
        String lunch = "";
        // Only accept "yes", "y", "no", or "n"
        while (!(lunch.equalsIgnoreCase("yes") || lunch.equalsIgnoreCase("y") ||
                lunch.equalsIgnoreCase("no")  || lunch.equalsIgnoreCase("n"))) {
            System.out.print("Do you want to bring your lunch? (yes/no): ");
            lunch = scanner.next();
            if (lunch.equalsIgnoreCase("yes") || lunch.equalsIgnoreCase("y")) {
                System.out.println("You packed your lunch.");
            } else if (lunch.equalsIgnoreCase("no") || lunch.equalsIgnoreCase("n")) {
                System.out.println("You did not pack your lunch.");
            } else {
                System.out.println("Please enter yes, y, no, or n.");

            }
        }
        String umbrella = "";
        while (!(umbrella.equalsIgnoreCase("yes") || umbrella.equalsIgnoreCase("y") ||
                umbrella.equalsIgnoreCase("no")  || umbrella.equalsIgnoreCase("n"))) {
            System.out.print("Do you want to bring an umbrella? (yes/no): ");
            umbrella = scanner.next();
            if (umbrella.equalsIgnoreCase("yes") || umbrella.equalsIgnoreCase("y")) {
                umbrellaPack = true;
                System.out.println("You packed your umbrella.");
            } else if (umbrella.equalsIgnoreCase("no") || umbrella.equalsIgnoreCase("n")) {
                System.out.println("You did not pack your umbrella.");
            } else {
                System.out.println("Please enter yes, y, no, or n.");
            }
        }
        while (!(check.equalsIgnoreCase("yes") || check.equalsIgnoreCase("y") ||
                check.equalsIgnoreCase("no")  || check.equalsIgnoreCase("n"))) {
            System.out.print("Do you want to check over your bag? (yes/no): ");
            check = scanner.next();
            if (check.equalsIgnoreCase("yes") || check.equalsIgnoreCase("y")) {
                System.out.println("You checked your bag.");
                if (ready == 0) {
                    System.out.println("You forgot your homework! Phew, good that you checked your bag.");
                    homework = true;
                    setHappiness(getHappiness() + 5);
                } else if (ready == 1) {
                    System.out.println("You forgot your textbook! Phew, good that you checked your bag.");
                    textbook = true;
                    setHappiness(getHappiness() + 5);
                } else {
                    System.out.println("You have everything you need.");
                    setHappiness(getHappiness() + 10);
                }
            } else if (check.equalsIgnoreCase("no") || check.equalsIgnoreCase("n")) {
                System.out.println("You feel confident about yourself.");
            } else {
                System.out.println("Please enter yes, y, no, or n.");
            }
        }
        System.out.println("You packed your bag.");
        scanner.close();
    }

    public int getHour() {
        return hour;
    }
    public int getMinute() {
        return minute;
    }

    public String checkLateSchool() { //Check if the player is late for school, based on the current time and the school hour
        String result = "";
        if((hour > schoolHour) || (hour == schoolHour && minute > schoolMinute)) {
            System.out.println("You are late for school!");
            result = "You are late for school!";
            setHappiness(getHappiness() - 10);

        }
        else {
            System.out.println("You are on time for school.");
            result = "You are on time for school.";
        }
        return result;
    }

    public String weatherEvent() { //Check the weather event based on the current hour and the umbrella pack status
        String weatherNow = "";
        if(Weather.getWeather(hour) >= 51 && Weather.getWeather(hour) <= 67) {

            if(umbrellaPack == false) {
                System.out.println("You got wet because you did not pack your umbrella.");
                weatherNow = "You got wet because you did not pack your umbrella.";
                setHappiness(getHappiness() - 10);
            }
            else {
                System.out.println("You are safe from the rain.");
                weatherNow = "You are safe from the rain.";
            }
        }
        else if(Weather.getWeather(hour) >= 71 && Weather.getWeather(hour) <= 77) {
            if(umbrellaPack == false) {
                System.out.println("You got wet because you did not pack your umbrella.");
                weatherNow = "You got wet because you did not pack your umbrella.";
                setHappiness(getHappiness() - 5);
            }
            else {
                System.out.println("You are safe from the snow.");
                weatherNow = "You are safe from the snow.";
            }
        }
        else if(Weather.getWeather(hour) >= 80 && Weather.getWeather(hour) <= 82) {
            if(umbrellaPack == false) {
                System.out.println("You got exctremely wet because you did not pack your umbrella.");
                weatherNow = "You got extremely wet because you did not pack your umbrella.";
                setHappiness(getHappiness() - 15);
            }
            else {
                System.out.println("You are safe from the rain.");
                weatherNow = "You are safe from the rain.";
            }
        }
        else if(Weather.getWeather(hour) >= 95 && Weather.getWeather(hour) <= 99) {
            if(umbrellaPack == false) {
                System.out.println("You got extremely wet because you did not pack your umbrella.");
                weatherNow = "You got extremely wet because you did not pack your umbrella.";
                setHappiness(getHappiness() - 15);
            }
            else {
                System.out.println("You are safe from the rain.");
                weatherNow = "You are safe from the rain.";
            }
        }
        return weatherNow;
    }

    public void setHappiness(int happiness) {
        if (happiness < 0) {
            this.happiness = 0;
        } else if (happiness > 200) {
            this.happiness = 200;
        } else {
            this.happiness = happiness;
        }
    }
    public int getHappiness() {
        return happiness;
    }

    public String formatTime(int hour, int minute) {
        return String.format("%02d:%02d", hour, minute);
    }

    public void checkBagContents() { //Check the bag contents
        if(textbook == false) {
            System.out.println("You forgot your textbook.");
        }
        if(homework == false) {
            System.out.println("You forgot your homework.");
        }
    }

    public boolean isUmbrellaPack() {
        return umbrellaPack;
    }
    public boolean isLunch() {
        return lunch;
    }
    public boolean isTextbook() {
        return textbook;
    }
    public boolean isHomework() {
        return homework;
    }
    public void setUmbrellaPack(boolean umbrellaPack) {
        this.umbrellaPack = umbrellaPack;
    }
    public void setLunch(boolean lunch) {
        this.lunch = lunch;
    }
    public void setTextbook(boolean textbook) {
        this.textbook = textbook;
    }
    public void setHomework(boolean homework) {
        this.homework = homework;
    }


    public boolean[] getMorningStatus() {
        return morningStatus;
    }
    public void setMorningStatus(int index, boolean status) {
        this.morningStatus[index] = status;
    }
    public ArrayList<String> getAfternoonThings() {
        return afternoonThings;
    }
    public ArrayList<String> getParkThings() {
        return parkThings;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
    public String getDestination() {
        return destination;
    }

    public int getHomeSchoolTravelTime() {
        return homeSchoolTravelTime;
    }
    public int getSchoolParkTravelTime() {
        return schoolParkTravelTime;
    }
    public int getParkHomeTravelTime() {
        return ParkHomeTravelTime;
    }
    public String getPreviousDestination() {
        return previousDestination;
    }

    public void setPreviousDestination(String previousDestination) {
        this.previousDestination = previousDestination;
    }

    public ArrayList<String> getEveningThings() {
        return eveningThings;
    }

    public int getHomeworkTime() {
        return homeworkTime;
    }

    public void setHomeworkTime(int homeworkTime) {
        this.homeworkTime = homeworkTime;
    }

    public int getHomeworkDone() {
        return homeworkDone;
    }

    public void setHomeworkDone(int homeworkDone) {
        this.homeworkDone = homeworkDone;
    }

    public boolean getDinner() {
        return dinner;
    }

    public void setDinner(boolean din) {
        dinner = din;
    }

    public void finishHomework() {
        eveningThings.remove(0);
    }
    public boolean getShower() {
        return shower;
    }

    public void setShower(boolean shower) {
        this.shower = shower;
    }
    
    



}
