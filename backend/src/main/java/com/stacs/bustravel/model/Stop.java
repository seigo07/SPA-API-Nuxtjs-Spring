package com.stacs.bustravel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.UUID;

@JsonPropertyOrder({ "name", "coordinates" })
public class Stop {

    @JsonProperty("name")
    public String Name;
    @JsonProperty("coordinates")
    public Coordinate Coordinates;
    private final String StopId;
    public Stop() {
        this.StopId = UUID.randomUUID().toString();
    }
    public Stop(String name, Coordinate cordinates) {
        this.StopId = UUID.randomUUID().toString();
        this.Name = name;
        this.Coordinates = cordinates;
    }

    public String getName() {
        return this.Name;
    }

    public Coordinate getCoordinates() {
        return this.Coordinates;
    }
}
