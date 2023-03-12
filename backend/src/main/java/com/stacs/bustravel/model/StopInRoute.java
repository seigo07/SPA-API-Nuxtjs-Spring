package com.stacs.bustravel.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class StopInRoute {
    public StopInRoute(){
        this.StopId = UUID.randomUUID().toString();
        this.Coordinates = new Coordinate();
    }
    public StopInRoute(String name,Coordinate coordinate, int travelTimeInMinutes){
        this.TravelTimeInMinutes= travelTimeInMinutes;
        this.Name = name;
        this.StopId = UUID.randomUUID().toString();
        this.Coordinates = coordinate;
    }

    @JsonProperty("stopId")
    public String StopId;
    @JsonProperty("name")
    public String Name;
    @JsonProperty("coordinates")
    public Coordinate Coordinates;
    @JsonProperty("travelTimeInMinutes")
    public int TravelTimeInMinutes;
}

