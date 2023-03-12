
package com.stacs.bustravel.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.UUID;

public class Route {
    public Route(){
        this.TripCode = UUID.randomUUID().toString();
    }
    @JsonProperty("tripCode")
    public String TripCode;
    @JsonProperty("routeId")
    public String RouteId;
    @JsonProperty("from")
    public String From;
    @JsonProperty("to")
    public String To;
    @JsonProperty("totalDurationInMinutes")
    public int TotalDurationInMinutes;
    @JsonProperty("daysOfOperation")

    public DaysOfWeek[] DaysOfOperation;
    @JsonProperty("timingsPerDay")

    public Timing[] TimingsPerDay ;
    @JsonProperty("stops")

    public StopInRoute[] Stops;

    public void setTripCode(String tripCode) {
        TripCode = tripCode;
    }

    public Route(String routeId,String from,String to, Timing[] timings,StopInRoute[] stops){
        this.TripCode = UUID.randomUUID().toString();
        this.Stops = stops;
        this.RouteId = routeId;
        this.From = from;
        this.To = to;
        this.TimingsPerDay = timings;
    }

    public Route(String routeId,String from,String to, Timing[] timings,StopInRoute[] stops,DaysOfWeek[] daysOfOperation){
        this.TripCode = UUID.randomUUID().toString();
        this.Stops = stops;
        this.RouteId = routeId;
        this.From = from;
        this.To = to;
        this.TimingsPerDay = timings;
        this.DaysOfOperation = daysOfOperation;
    }
}
