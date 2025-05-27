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

\-List of getter methods
\-Return needed information for main

    void setTime(int hour, int minute)
    void setSchoolHour(int hour, int minute)
    void setLuck(int luck)
    void setHappiness(int happiness)
\-List of setter methods
\-Used to set specific data

