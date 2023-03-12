package com.stacs.bustravel.service;

import com.stacs.bustravel.model.Stop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class StopService {
    /**
     * The list of stops managed by this service.
     */
    public ArrayList<Stop> stops;

    /**
     * A converter for loading and saving stop data to and from JSON.
     */
    JsontoObjectConverter jsontoObjectConverter = new JsontoObjectConverter();

    /**
     * Constructs a new StopService with an empty list of stops.
     *
     * @param stops the list of stops to manage
     */
    public StopService(ArrayList<Stop> stops) {
        this.stops = stops.isEmpty() ? new ArrayList<>() : stops;
    }

    /**
     * Constructs a new StopService with an empty list of stops.
     *
     * @param objectConverter JSON object converter
     */
    public StopService(JsontoObjectConverter objectConverter) {
        this.jsontoObjectConverter = objectConverter;
    }

    /**
     * Constructs a new StopService and loads the list of stops from a JSON file.
     *
     * @throws IOException if there was an error reading the JSON file
     */
    public StopService() throws IOException {
        this.stops = loadStopValue();
    }

    /**
     * Loads the list of stops from a JSON file and returns it.
     *
     * @return the list of stops loaded from the JSON file
     * @throws IOException if there was an error reading the JSON file
     */
    public ArrayList<Stop> loadStopValue() throws IOException {
        return jsontoObjectConverter.loadStopValue();
    }

    /**
     * Adds a stop to the list of stops managed by this service.
     *
     * @param stop the stop to add
     * @return the updated list of stops
     * @throws IOException if there was an error saving the updated list of stops to the JSON file
     */
    public ArrayList<Stop> addStop(Stop stop) throws IOException {
        //Load the stop from JSON file everytime and then save it back with the new update
        ArrayList<Stop> allStops = null;
        try {
            allStops = loadStopValue();
            Stop stopToAdd = allStops.stream().filter(s -> s.getName().trim().equalsIgnoreCase(stop.Name.trim()) && s.getCoordinates().Latitude == (stop.Coordinates.Latitude) && s.getCoordinates().Longitude == (stop.Coordinates.Longitude)).findFirst().orElseThrow(NoSuchElementException::new);
            if (stopToAdd != null) {
                throw new IllegalArgumentException("Stop already exists in the list");
            }
        } catch (NoSuchElementException ex) {
            assert allStops != null;
            allStops.add(stop);
            this.stops = allStops;

            //Save to Stop Json
            jsontoObjectConverter.saveStopsToJSON(allStops);

        } catch (IOException io) {
            throw new RuntimeException("Failed to save stops to JSON");
        }
        return stops;
    }


    /**
     * Removes a stop from the list of stops managed by this service.
     *
     * @param stop the stop to remove
     * @return the updated list of stops
     * @throws IllegalArgumentException if the specified stop is not in the list of stops
     */
    public ArrayList<Stop> removeStop(Stop stop) throws IllegalArgumentException {
        try {
            ArrayList<Stop> allStops = loadStopValue();
            Stop stopToRemove = allStops.stream().filter(s -> s.getName().equals(stop.Name) && s.getCoordinates().Longitude == stop.Coordinates.Longitude && s.getCoordinates().Latitude == stop.Coordinates.Latitude).findFirst().orElseThrow(NoSuchElementException::new);
            allStops.remove(stopToRemove);

            //Updating the global value with new stops
            this.stops = allStops;

            //Save to Stop Json
            jsontoObjectConverter.saveStopsToJSON(stops);
            return stops;
        } catch (Exception ex) {
            throw new NoSuchElementException("Stop with name " + stop.Name + " not found in the list");
        }
    }

    /**
     * Removes a stop with the given name from the list of stops.
     * @param stopName the name of the stop to remove
     * @return the updated list of stops
     * @throws NoSuchElementException if a stop with the given name cannot be found in the list
     */
    public ArrayList<Stop> removeStopByName(String stopName) throws NoSuchElementException {
        try {
            Stop stopToRemove = stops.stream().filter(s -> s.getName().equals(stopName)).findFirst().orElseThrow(NoSuchElementException::new);
            stops.remove(stopToRemove);
            return stops;
        } catch (Exception ex) {
            throw new NoSuchElementException("Stop with name " + stopName + " not found in the list");
        }
    }

    public ArrayList<Stop> getAllStops() throws IOException {
        return jsontoObjectConverter.loadStopValue();
    }

    /**
     * This method is used to serve the ui to show all stop names in a dropdown
     *
     * @return List of all stop names
     * @throws IOException when the file cannot be read
     */
    public ArrayList<String> getAllStopName() throws IOException {
        ArrayList<Stop> stops = jsontoObjectConverter.loadStopValue();
        ArrayList<String> stopNames = new ArrayList<>();
        for (Stop stop : stops) {
            stopNames.add(stop.Name);
        }
        return stopNames;
    }
}
