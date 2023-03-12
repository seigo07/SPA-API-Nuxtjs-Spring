package com.stacs.bustravel.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stacs.bustravel.model.StopInRoute;

public class AddStop {
    @JsonProperty("routeId")
    public String RouteId;
    @JsonProperty("from")
    public String From;
    @JsonProperty("to")
    public String To;
    @JsonProperty("stop")
    public StopInRoute Stop;

    public AddStop(String routeId,String from,String to,StopInRoute stop){
        this.Stop = stop;
        this.From = from;
        this.To = to;
        this.RouteId = routeId;
    }
    public AddStop(){

    }

}
