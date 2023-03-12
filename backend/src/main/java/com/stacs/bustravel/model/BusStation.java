package com.stacs.bustravel.model;

import java.util.UUID;

public class BusStation {

    public String Name;
    private String StationId;


    public BusStation() {
        this.StationId = UUID.randomUUID().toString();
    }

    public BusStation(String name) {
        this.StationId = UUID.randomUUID().toString();
        this.Name = name;
    }

    public void setStationId(String stationId) {
        StationId = UUID.randomUUID().toString();
    }

    public String getName() {
        return Name;
    }

}
