# Project Walkthrough
## Project Overview - A Day in a City
This Java project simulate a normal day of a student (as of now) in a random location of the United States.
It is fun and engaging, and its main purpose is for entertainment. It uses real time data such as location from Geoapify and weather from Open Meteo for simulation. Then the program will display the different results from user's different choice of action. 

## Code Breakdown
### Doc.sh
A list of codes used for starting the virtual display and remote connection to codespace to enable visible GUI on any browser that this codespace can execute.

### GameLogic.java
Main game logic, accessible to the Main class, used to execute game related activities and store game info.
    
    public void updateTime(int type)
\- Format the hour and minute by hour 

    public void addTime(int minute)
\- Add time to the current after execute some user action \
\- Format time after added

    public String getWeather()
\- Get stored weather based on current hour \
\- Get stored temperature based on current hour \
\- Check if the weather and temperatue in the future 24 hours for rain and snow \
\- Output for the check weather action in main \

    public void packBag()
\-This method is used to 

    public void checkLateSchool()
\-

    public void WeatherEvent()
\-

    public void checkBagContents()
\-

    public int getAge()
    public String getDate()
    public String getTime()
    public int getSchoolHour()
    public int getSchoolMinute()
    public ArrayList<String> getMorningThings()
    public String getWeather()
    public int getReady()
    public int getHour()
    public int getMinute()
    public int getLuck()
    public int getHappiness()

\-List of getter methods \
\-Return needed information for main

    public void setTime(int hour, int minute)
    public void setSchoolHour(int hour, int minute)
    public void setLuck(int luck)
    public void setHappiness(int happiness)
\-List of setter methods \
\-Used to set specific data

### Main.java
This class initializes the JavaFX application, sets up the intro and game scenes, and starts the game when the user enters their name.

    public static void main(String[] args)
\-Main method \
\-Allow user to use debug to choose between console mode and GUI mode \
\-initialize the game components through API calls \
\-Runs the game

    public void start(Stage primaryStage)
\-Initialize the GUI stage \
\-Textbox that ask for user's name
\-Get transit time between home and school

    public void typeText(Label label, String fullText, Duration delay, Runnable onFinished)
\-Display text in typewriter form \
\-Used for visual appealing

    public void handleMorningChoice(String action, String name, GameLogic game, int transitTime, Stage stage)
\-Used to direct scene for morning actions \
\-Used switch to differetiate between choices

    public void notReady(Stage stage, String name, GameLogic game, int transitTime)
\-Display message for not-ready features

    public void showScene(Stage stage, String name, GameLogic game, int transitTime, String sceneID)
\-Display scene based on sceneID \
\-Gather information from gameLogic object \
\-Used images pregenerated that are stored in images folder \
\-Display text in typewriter format

    public void showScene(Stage stage, String name, GameLogic game, int transitTime, String sceneID, ArrayList<String> buttonsList)
\-Display choices \
\-Display buttons \
\-Show buttons with delay after one another

### Map.java
This class deals with any location related API calls and storing data used for GameLogic class

    public static void getMap()
\-Generate a random longtitude and latitude \
\-Check and restrict the location to United States \
\-Store country info

    public static String findPlace(double latitude, double longitude)
\-Get country name by using Geoapify API call

    public static void findSchool()
\-Check school type
\-Find matching school in near location




