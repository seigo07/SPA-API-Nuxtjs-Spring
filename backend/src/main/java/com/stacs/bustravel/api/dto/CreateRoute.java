package com.stacs.bustravel.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stacs.bustravel.model.StopInRoute;
import com.stacs.bustravel.model.Timing;

import javax.swing.plaf.ProgressBarUI;

public class CreateRoute {
    @JsonProperty("routeId")
    public String RouteId;
    @JsonProperty("from")
    public String From;
    @JsonProperty("to")
    public String To;
    @JsonProperty("timingsPerDay")
    public Timing[] TimingsPerDay;
    @JsonProperty("stops")
    public StopInRoute[] Stops;

    public CreateRoute(String routeId,String from,String to, Timing[] timings, StopInRoute[] stops){
        this.RouteId = routeId;
        this.From = from;
        this.To = to;
        this.TimingsPerDay = timings;
        this.Stops = stops;
    }
    public CreateRoute(){}
}
