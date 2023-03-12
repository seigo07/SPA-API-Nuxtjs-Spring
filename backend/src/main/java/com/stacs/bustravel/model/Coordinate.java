package com.stacs.bustravel.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Coordinate {

    @JsonProperty("latitude")
    public double Latitude;
    @JsonProperty("longitude")

    public double Longitude;

    public Coordinate(double latitude, double longitude){
        this.Latitude = latitude;
        this.Longitude = longitude;
    }

    public Coordinate() {
        this.Latitude = 0;
        this.Longitude = 0;

    }
}
