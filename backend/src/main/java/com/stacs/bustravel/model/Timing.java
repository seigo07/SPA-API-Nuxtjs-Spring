package com.stacs.bustravel.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Timing {

    @JsonProperty("day")
    public DaysOfWeek Day;
    @JsonProperty("timing")
    public ArrayList<String> Timing;

    public Timing(DaysOfWeek day, ArrayList<String> timing){
        this.Timing = timing;
        this.Day = day;
    }

    public Timing(){

    }
}
