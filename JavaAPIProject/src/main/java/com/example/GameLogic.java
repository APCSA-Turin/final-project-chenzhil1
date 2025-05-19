package com.example;

public class GameLogic {
    private static int age;
    private static String date;
    private static String time;
    private static int hour;
    private static int minute;
    
    public static void initialize(String role) {
        Map.getMap(); //Initialize random lcoation
        date = "20XX-" + (int) (Math.random() * 12 + 1) + "-" + (int) (Math.random() * 28 + 1); // Random date
        hour = 6;
        minute = 30;
        updateTime(); // Update time to 6:30 AM for the morning
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



    public static int getAge() {
        return age;
    }

    public static String getDate() {
        return date;
    }

    public static String getTime() {
        return time;
    }

    public static void updateTime() {
        if(minute >= 60) {
            hour += minute / 60;
            minute = minute % 60;
        }
        if(hour >= 24) {
            hour = hour % 24;
            date = date.substring(0, 8) + (Integer.parseInt(date.substring(8)) + 1);
        }
        time = String.format("%02d:%02d", hour, minute);
    }

}
