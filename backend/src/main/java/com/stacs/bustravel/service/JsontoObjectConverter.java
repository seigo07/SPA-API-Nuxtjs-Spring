package com.stacs.bustravel.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.stacs.bustravel.model.*;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class JsontoObjectConverter {

    ObjectMapper mapper = new ObjectMapper();
    private static FileWriter file;

    public ArrayList<Stop> loadStopValue() throws IOException {
        try {
            ArrayList<Stop> stopArrayList = new ArrayList<>();
            JsonNode stops = new ObjectMapper().readTree(new File("src/main/resources/stops.json"));
            for (JsonNode stop : stops) {
                String name = stop.get("name").textValue();
                Coordinate coordinates = new Coordinate();
                String coordinate = stop.get("coordinates").toString();
                coordinates = new ObjectMapper().readValue(coordinate, Coordinate.class);
                stopArrayList.add(new Stop(name, coordinates));
            }
            return stopArrayList;
        }
        catch(Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
    }


    public ArrayList<Stop> loadDefaultStopValue() throws IOException {
        try {
            ArrayList<Stop> stopArrayList = new ArrayList<>();
            JsonNode stops = new ObjectMapper().readTree(new File("src/main/resources/stopsBackup.json"));
            for (JsonNode stop : stops) {
                String name = stop.get("name").textValue();
                Coordinate coordinates = new Coordinate();
                String coordinate = stop.get("coordinates").toString();
                coordinates = new ObjectMapper().readValue(coordinate, Coordinate.class);
                stopArrayList.add(new Stop(name, coordinates));
            }
            return stopArrayList;
        }
        catch(Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
    }

    public ArrayList<Route> loadRouteValues() throws IOException{
        ArrayList<Route> routesArrayList = new ArrayList<>();
        try {
            JsonNode routes = new ObjectMapper().readTree(new File("src/main/resources/routes.json"));
            for (JsonNode route : routes) {
                Route newRoute = new Route();
                newRoute.setTripCode(route.get("tripCode").textValue());
                newRoute.RouteId = route.get("routeId").textValue();
                newRoute.From = route.get("from").textValue();
                newRoute.To = route.get("to").textValue();
                newRoute.TotalDurationInMinutes = Integer.parseInt(route.get("totalDurationInMinutes").toString());
                newRoute.DaysOfOperation = new ObjectMapper().readValue(route.get("daysOfOperation").toString(), DaysOfWeek[].class);
                newRoute.Stops = new ObjectMapper().readValue(route.get("stops").toString(), StopInRoute[].class);
                newRoute.TimingsPerDay =new ObjectMapper().readValue(route.get("timingsPerDay").toString(), Timing[].class);
                routesArrayList.add(newRoute);
            }
        }
        catch(Exception ex){
            String a = ex.getMessage();
        }
        return routesArrayList;
    }

    public boolean saveRouteToJSON(ArrayList<Route> route) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(route);
        mapper.writeValue(new File("src/main/resources/routes.json"), route);
        return true;
    }


    public boolean saveStopsToJSON(ArrayList<Stop> stops) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(stops);
        mapper.writeValue(new File("src/main/resources/stops.json"), stops);
        return true;
    }

    public boolean revertStopJson() throws IOException {
        List<Stop> stops = loadDefaultStopValue();
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(stops);
        mapper.writeValue(new File("src/main/resources/stops.json"), stops);
        return true;
    }

    public ArrayList<Route> loadDefaultRouteValues() throws IOException{
        ArrayList<Route> routesArrayList = new ArrayList<>();
        try {
            JsonNode routes = new ObjectMapper().readTree(new File("src/main/resources/routesBackup.json"));
            for (JsonNode route : routes) {
                Route newRoute = new Route();
                newRoute.setTripCode(route.get("tripCode").textValue());
                newRoute.RouteId = route.get("routeId").textValue();
                newRoute.From = route.get("from").textValue();
                newRoute.To = route.get("to").textValue();
                newRoute.TotalDurationInMinutes = Integer.parseInt(route.get("totalDurationInMinutes").toString());
                newRoute.DaysOfOperation = new ObjectMapper().readValue(route.get("daysOfOperation").toString(), DaysOfWeek[].class);
                newRoute.Stops = new ObjectMapper().readValue(route.get("stops").toString(), StopInRoute[].class);
                newRoute.TimingsPerDay =new ObjectMapper().readValue(route.get("timingsPerDay").toString(), Timing[].class);
                routesArrayList.add(newRoute);
            }
        }
        catch(Exception ex){
            String a = ex.getMessage();
        }
        return routesArrayList;
    }

    public boolean revertRoutesJson() throws IOException {
        List<Route> stops = loadDefaultRouteValues();
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(stops);
        mapper.writeValue(new File("src/main/resources/routes.json"), stops);
        return true;
    }



}

