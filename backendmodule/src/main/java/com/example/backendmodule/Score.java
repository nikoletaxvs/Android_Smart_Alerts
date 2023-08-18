package com.example.backendmodule;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

public class Score {
    boolean wasToday;
    Hashtable<String, Integer> typeValues ;
    String[] elements ={"Fire","Tsunami","Earthquake","Tornado","Flood"};
    public Score(){
         typeValues= new Hashtable<String, Integer>();

        typeValues.put("Fire",0);
        typeValues.put("Tsunami",0);
        typeValues.put("Earthquake",0);
        typeValues.put("Tornado",0);
        typeValues.put("Flood",0);
    }
    public Hashtable<String,Integer> calculateScores(String n_timestamp, String n_type){
        for(String str : elements){
            if(n_type.startsWith(str)){
              if(wasToday(n_timestamp)){
                    int time_difference= minutesBefore(n_timestamp);
                    Integer element = typeValues.get(str) + 24/(time_difference/60+1);
                    typeValues.put(str,element);
              }
            }
        }
        return typeValues;
    }
    /*Method Explanation:
     * Assume timestamp =  17-02-2023 15:44:42
     * date =  {17,02,2023}, datetimeNow is the same but for the current date
     * time = {15,44,42}, timeNow is the same but for the current time
     *
     * Checks preformed:
     *   1. Year check (Same year)
     *   2. Month check (Same month)
     *   3. Day check (Same day)
     * */
    public Boolean wasToday(String eventDate) {

        boolean today = false;

        String[] datetime = eventDate.split(" ");
        String[] date = datetime[0].split("-");
        String[] time = datetime[1].split(":");
        String[] datetimeNow = getTimestamp().split(" ");
        String[] dateNow= datetimeNow[0].split("-");
        String[] timeNow = datetimeNow[1].split(":");
        //check if year is the same
        if(date[2].contains(dateNow[2])){
            //check if month is the same
            if(date[1].contains(dateNow[1])){
                //check if day is the same
                if(Integer.parseInt(date[0]) == Integer.parseInt(dateNow[0] )){
                    today= true;
                }//check if it was yesterday night
                else if(Integer.parseInt(date[0])+1 == Integer.parseInt(dateNow[0] )){



                    if(Integer.parseInt(timeNow[1])<5){

                        today =true;

                    }

                }
            }
        }
        return today;
    }

    // Method that returns the time difference from now and a given timestamp (in minutes)
    public int minutesBefore(String eventDate){
        int minutes=0;
        String[] datetime = eventDate.split(" ");
        String[] time = datetime[1].split(":");
        String[] datetimeNow = getTimestamp().split(" ");
        String[] timeNow = datetimeNow[1].split(":");
        minutes = Integer.parseInt(timeNow[0])*60 + Integer.parseInt(timeNow[1])-(Integer.parseInt(time[0])*60 + Integer.parseInt(time[1]));
        return Math.abs(minutes);
    }
    // Method that determines if location1 and location2 are less than 20 km apart
    public boolean isNearby(String location1,String location2) {
        location1 = location1.replace("Lat:" ,"");
        location1 = location1.replace("Log:","");
        String[] locs = location1.split(" ");
        location2 = location2.replace("Lat:" ,"");
        location2 = location2.replace("Log:","");
        String[] locsEvent = location2.split(" ");

        if(locs[0] != null && locs[0] != null && locsEvent[0]!=null && locsEvent[1]!=null){
            Double lat = Double.parseDouble(locs[0]);
            Double log = Double.parseDouble(locs[1]);
            Double latEvent = Double.parseDouble(locsEvent[0]);
            Double logEvent = Double.parseDouble(locsEvent[1]);
            Double d = haversine_distance(lat,log,latEvent,logEvent);
            return d<20.0;
        }
        return false;
    }
    //The haversine formula determines the great-circle distance between two points
    // on a sphere given their longitudes and latitudes.
    // Important in navigation
    public double haversine_distance(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        double earthRadius = 6371.01; //Kilometers
        return earthRadius * Math.acos(Math.sin(lat1)*Math.sin(lat2) + Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon1 - lon2));
    }
    private String getTimestamp() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(new Date());
    }
}
