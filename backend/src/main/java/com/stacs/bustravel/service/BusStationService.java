package com.stacs.bustravel.service;

import com.stacs.bustravel.model.BusStation;
import com.stacs.bustravel.model.Stop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class BusStationService {
    public ArrayList<BusStation> busStations = new ArrayList<>();
    JsontoObjectConverter objectConverter = new JsontoObjectConverter();

    public BusStationService(ArrayList<BusStation> busStations){
        this.busStations = busStations;
    }
    public BusStationService(){

    }

    /**
     * This method is used to add a new station
     * @return List of all stations added to the system
     */
    public ArrayList<String> getAllStation() throws IOException {
        ArrayList<String> allStation = new ArrayList<>();
        ArrayList<Stop> stops = objectConverter.loadStopValue();
        for (Stop stop : stops) {
            allStation.add(stop.Name);
        }
        return allStation;
    }



    /**
     * Deprecated
     * This method add a new bus station to list of available station
     *
     * @param name - name of the new bus station
     * @return - lists all the available bus stations
     * @throws IllegalArgumentException if the give bus station already exists
     * @deprecated -The following method has been deprecated and not in use
     */
    public ArrayList<String> addStation(String name) throws IllegalArgumentException, IOException {
        try {
            BusStation stationToAdd = busStations.stream()
                    .filter(s -> s.getName().equals(name))
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new);
            if(stationToAdd != null){
                throw new IllegalArgumentException("Station already exists");
            }
        } catch (NoSuchElementException ex) {
            busStations.add(new BusStation(name));
        }

        //Returns all bus station after addition
        return getAllStation();
    }
}
