package com.example.scoremodule;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
            if(wasToday(n_timestamp)){
                if(n_type.startsWith(str)){
                    Integer element = typeValues.get(str) + 1;
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
            //  Toast.makeText(ViewEvents.this,"Same year",Toast.LENGTH_SHORT).show();

            if(date[1].contains(dateNow[1])){
                //  Toast.makeText(ViewEvents.this,"Same month",Toast.LENGTH_SHORT).show();
                if(Integer.parseInt(date[0]) == Integer.parseInt(dateNow[0] )){
                    //       Toast.makeText(ViewEvents.this,"Within same day or yesterday",Toast.LENGTH_SHORT).show();
                    today= true;
                }
            }
        }
        return today;
    }
    private String getTimestamp() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(new Date());
    }
}
