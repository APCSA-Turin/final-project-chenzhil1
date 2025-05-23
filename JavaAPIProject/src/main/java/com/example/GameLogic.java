package com.example;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class GameLogic {
    private int age;
    private String date;
    private String time;
    private int hour;
    private int minute;
    private String role;
    private boolean debug = true;
    private int luck = (int) (Math.random() * 20 + 1);
    private int happiness = (int) (Math.random() * 30 + 50);
    private int schoolHour = 8;
    private int schoolMinute = 0;
    private int ready = 0;
    private boolean umbrellaPack = false;
    private boolean lunch = false;
    private ArrayList<String> morningThings = new ArrayList<String>(Arrays.asList("Brush your teeth", "Eat Breakfast", "Get dressed", "Check Weather", "Check transit time", "Pack your bag", "Play with phone", "Exit Home"));
    private boolean textbook = false;
    private boolean homework = false;
    public GameLogic() {
        this.age = 0;
        this.date = "20XX-" + (int) (Math.random() * 12 + 1) + "-" + (int) (Math.random() * 28 + 1); // Random date;
        this.time = "";
        this.hour = 6;
        this.minute = 0;
        this.role = "Student";
        this.luck = 0;
        this.happiness = 0;
        Map.getMap(); //Initialize random location
        Weather.findWeather(); // Initialize weather
        updateTime(0); // Update time to 6:30 AM for the morning
        // Initialize game state
        if(role.equalsIgnoreCase("Student")) {
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
            Map.findSchool();
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

    public void updateTime(int type) {
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

    public void addTime(int minute) {
        this.minute += minute;
        updateTime(0);
    }

    public String getWeather() {
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
    }

    public int getReady() {
        return ready;
    }

    public void packBag() {
        Scanner scanner = new Scanner(System.in);
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

        checkReady();

        String check = "";
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
        while (!(check.equalsIgnoreCase("yes") || check.equalsIgnoreCase("y") ||
                 check.equalsIgnoreCase("no")  || check.equalsIgnoreCase("n"))) {
            System.out.print("Do you want to check over your bag? (yes/no): ");
            check = scanner.next();
            if (check.equalsIgnoreCase("yes") || check.equalsIgnoreCase("y")) {
                System.out.println("You checked your bag.");
                if (ready == 0) {
                    System.out.println("You forgot your homework! Phew, good that you checked your bag.");
                    homework = true;
                    happiness += 5;
                } else if (ready == 1) {
                    System.out.println("You forgot your textbook! Phew, good that you checked your bag.");
                    textbook = true;
                    happiness += 5;
                } else {
                    System.out.println("You have everything you need.");
                    happiness += 10;
                }
            } else if (check.equalsIgnoreCase("no") || check.equalsIgnoreCase("n")) {
                System.out.println("You feel confident about yourself.");
            } else {
                System.out.println("Please enter yes, y, no, or n.");
            }
        }
        System.out.println("You packed your bag.");
    }

    public int getHour() {
        return hour;
    }
    public int getMinute() {
        return minute;
    }

    public void checkLateSchool() {
        if((hour > schoolHour) || (hour == schoolHour && minute > schoolMinute)) {
            System.out.println("You are late for school!");
            happiness -= 10;

        }
        else {
            System.out.println("You are on time for school.");
        }
    }

    public void weatherEvent() {
        if(Weather.getWeather(hour) >= 51 && Weather.getWeather(hour) <= 67) {
            if(umbrellaPack == false) {
                System.out.println("You got wet because you did not pack your umbrella.");
                happiness -= 10;
            }
            else {
                System.out.println("You are safe from the rain.");
            }
        }
        else if(Weather.getWeather(hour) >= 71 && Weather.getWeather(hour) <= 77) {
            if(umbrellaPack == false) {
                System.out.println("You got wet because you did not pack your umbrella.");
                happiness -= 5;
            }
            else {
                System.out.println("You are safe from the snow.");
            }
        }
        else if(Weather.getWeather(hour) >= 80 && Weather.getWeather(hour) <= 82) {
            if(umbrellaPack == false) {
                System.out.println("You got exctremely wet because you did not pack your umbrella.");
                happiness -= 15;
            }
            else {
                System.out.println("You are safe from the rain.");
            }
        }
        else if(Weather.getWeather(hour) >= 95 && Weather.getWeather(hour) <= 99) {
            if(umbrellaPack == false) {
                System.out.println("You got extremely wet because you did not pack your umbrella.");
                happiness -= 15;
            }
            else {
                System.out.println("You are safe from the rain.");
            }
        }
    }

    public void setLuck(int luck) {
        this.luck = luck;
    }
    public int getLuck() {
        return luck;
    }
    public void setHappiness(int happiness) {
        this.happiness = happiness;
    }
    public int getHappiness() {
        return happiness;
    }

    public String formatTime(int hour, int minute) {
        return String.format("%02d:%02d", hour, minute);
    }

    public void checkBagContents() {
        if(textbook == false) {
            System.out.println("You forgot your textbook.");
        }
        if(homework == false) {
            System.out.println("You forgot your homework.");
        }
    }


}
