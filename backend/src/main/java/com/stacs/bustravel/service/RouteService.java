package com.stacs.bustravel.service;

import com.stacs.bustravel.model.DaysOfWeek;
import com.stacs.bustravel.model.Route;
import com.stacs.bustravel.model.Stop;
import com.stacs.bustravel.model.StopInRoute;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * A service class that provides operations related to bus routes.
 */
public class RouteService {
    /**
     * The list of routes managed by this service.
     */
    public ArrayList<Route> routes = new ArrayList<>();
    /**
     * The JSON to object converter used by this service.
     */
    public JsontoObjectConverter objectConverter = new JsontoObjectConverter();
    /**
     * The stop service used by this service.
     */
    StopService stopService = new StopService();

    /**
     * Creates a new instance of the RouteService class.
     *
     * @throws IOException If an error occurs while loading the routes from JSON.
     */
    public RouteService() throws IOException {
        this.routes = loadRouteValues();
    }

    /**
     * Creates a new instance of the RouteService class.
     */
    public RouteService(ArrayList<Route> routes) throws IOException {
        this.routes = routes;
    }


    /**
     * Creates a new instance of the RouteService class with JSON object converter.
     *
     * @throws IOException If an error occurs while loading the routes from JSON.
     */
    public RouteService(JsontoObjectConverter jsontoObjectConverter) throws IOException {
        this.objectConverter = jsontoObjectConverter;
    }

    public RouteService(JsontoObjectConverter objectConverter, StopService stopService) throws IOException {
        this.objectConverter = objectConverter;
        this.stopService = stopService;
    }

    /**
     * Loads the routes from JSON.
     *
     * @return The list of routes loaded from JSON.
     * @throws IOException If an error occurs while loading the routes from JSON.
     */
    public ArrayList<Route> loadRouteValues() throws IOException {
        return objectConverter.loadRouteValues();
    }


    /**
     * Get all the routes available in the system
     *
     * @return list of all routes
     * @throws IOException if there is an error loading route values from JSON
     */
    public ArrayList<Route> getAllRoutes() throws IOException {
        try {
            return loadRouteValues();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new IOException();
        }
    }

    /**
     * Adds a new route to the list of all routes.
     *
     * @param route The route to add.
     * @return The updated list of all routes.
     * @throws IllegalArgumentException if the route already exists.
     */
    public ArrayList<Route> addRoute(Route route) throws Exception {
        ArrayList<Route> allRoutes = loadRouteValues() != null ? loadRouteValues() : new ArrayList<>();
        try {
            Route exisitingRoute = allRoutes.stream().filter(r -> r.RouteId.equals(route.RouteId) && r.To.equals(route.To) && r.From.equals(route.From)).findFirst().orElseThrow(NoSuchElementException::new);
            if (exisitingRoute != null) {
                throw new IllegalArgumentException("Route already exists");
            }
        } catch (NoSuchElementException ex) {
            try {
                //Derive days of operation from timing
                route.DaysOfOperation = Arrays.stream((route.TimingsPerDay)).map(x -> x.Day).toArray(DaysOfWeek[]::new);
                if(route.DaysOfOperation == null) { route.DaysOfOperation = new DaysOfWeek[]{}; }
                allRoutes.add(route);
                //Ignoring the Save to JSON failure
                objectConverter.saveRouteToJSON(allRoutes);
            } catch (IOException ioe) {
                System.out.println("Failed to save data to JSON");
            } catch (Exception excep) {
                throw new Exception(excep.getMessage());
            }
        }
        this.routes = allRoutes;
        return allRoutes;
    }

    /**
     * The following method adds a stop to a given route
     *
     * @param routeId - route to which the stop needs to be added
     * @param from    - starting bus station
     * @param to      - end bus station
     * @param stop    - stop that needs to be added
     * @return - the route information with added stop
     */
    public Route addStopToRoute(String routeId, String from, String to, StopInRoute stop) throws IOException {
        Route exisitingRoute;
        List<Route> existingRoutes = this.routes;
        try {
            exisitingRoute = existingRoutes.stream().filter(r -> r.RouteId.equals(routeId) && r.To.equals(to) && r.From.equals(from)).findFirst().orElseThrow(NoSuchElementException::new);
            if (exisitingRoute != null) {
                //Check if the stop is conflicting or already existing
                if (Arrays.stream(exisitingRoute.Stops).anyMatch(s -> s.Name.trim().equalsIgnoreCase(stop.Name.trim()))) {
                    throw new IllegalArgumentException("Stop already exists in route");
                } else {
                    //Adding stop to the list of existing stops
                    List<StopInRoute> stops = Arrays.asList(exisitingRoute.Stops);
                    List<StopInRoute> stopInRoutes = new ArrayList<>(stops);
                    stopInRoutes.add(stop);
                    exisitingRoute.Stops = stopInRoutes.toArray(new StopInRoute[0]);

                    //Save the stop to stops JSON
                    try {
                        stopService.addStop(new Stop(stop.Name, stop.Coordinates));

                    }
                    //This exception need not be propagated to the user end
                    //As the stop might already exist but the save to json might fail
                    catch (Exception ex) {
                        System.out.println("Failed to update stops JSON files");
                    }
                    try {
                        //Save Route to Route JSON
                        for (Route route : this.routes) {
                            if (route.RouteId == exisitingRoute.RouteId && route.From.equalsIgnoreCase(exisitingRoute.From) && route.To.equalsIgnoreCase(exisitingRoute.To)) {
                                route = exisitingRoute;
                            }
                        }
                        //Save Route to JSON
                        objectConverter.saveRouteToJSON(this.routes);
                    }
                    //This exception need not be propagated to the user end
                    //As the stop might already exist but the save to json might fail
                    catch (Exception ex) {
                        System.out.println("Failed to update Route JSON files");
                    }
                }
            }
        } catch (NoSuchElementException ex) {
            throw new IllegalArgumentException("Route does not exists");
        }
        return exisitingRoute;
    }


    /**
     * The following method is used to get all routes that services the given stop name
     *
     * @param stopName stop to be matched in the array of routes
     * @return all the routes that contains the given stop
     */
    public List<Route> getAllRoutesByStop(String stopName) throws IOException {
        List<Route> existingRoute = loadRouteValues();
        try {
            existingRoute = existingRoute.stream().filter(sublist -> Arrays.stream(sublist.Stops).anyMatch(p -> p.Name != null && p.Name.equalsIgnoreCase(stopName))).collect(Collectors.toList());
            if (existingRoute.size() > 0) {
                return existingRoute;
            }
        } catch (NoSuchElementException ex) {
            throw new IllegalArgumentException("Route does not exists");
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
        return existingRoute;
    }


    /**
     * The following method is used to get all routes that services the given stop name on given day
     *
     * @param stopName stop to be matched in the array of routes
     * @param day      day to be matched in the array of routes
     * @return all the routes that contains the given stop
     */
    public List<Route> getAllRoutesByStopAndDay(String stopName, String day) throws IOException {

        List<Route> existingRoute;
        ArrayList<Route> allRoutes = loadRouteValues();
        try {
            existingRoute = allRoutes.stream().filter(r -> Arrays.asList(r.DaysOfOperation).contains(DaysOfWeek.valueOf(day))).collect(Collectors.toList());

            existingRoute = existingRoute.stream().filter(sublist -> Arrays.stream(sublist.Stops).anyMatch(p -> p.Name.equals(stopName))).collect(Collectors.toList());
            if (existingRoute != null && existingRoute.size() > 0) {
                return existingRoute;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
        return existingRoute;
    }


    /**
     * The following method is used to get all routes that services the given stop name on given day
     *
     * @param stopName stop to be matched in the array of routes
     * @param day      day to be matched in the array of routes
     * @param time     time to be matched in the array of routes
     * @return all the routes that contains the given stop
     */
    public List<Route> getAllRoutesByParameters(String stopName, String day, String time) throws IOException {
        List<Route> existingRoute = new ArrayList<>();
        List<Route> allRoutes = loadRouteValues();
        try {
            if (!stopName.isEmpty()) {
                existingRoute = (allRoutes).stream().filter(sublist -> Arrays.stream(sublist.Stops).anyMatch(p -> p.Name.equals(stopName))).collect(Collectors.toList());
            }
            if (!day.isEmpty()) {
                existingRoute = existingRoute.stream().filter(r -> Arrays.asList(r.DaysOfOperation).contains(DaysOfWeek.valueOf(day))).collect(Collectors.toList());
            }

            if (!time.isEmpty()) {
                LinkedHashMap<DaysOfWeek, List<String>> timeRange;
                List<Route> routeByTiming = new ArrayList<>();
                for (Route route : existingRoute) {
                    int travelTimeToStop = Arrays.stream(route.Stops).filter(stop -> stop.Name.equalsIgnoreCase(stopName)).findFirst().get().TravelTimeInMinutes;
                    timeRange = new Helper().getTimingEachDayForStop(List.of(route.TimingsPerDay), travelTimeToStop);

                    //If day is provided then we match that particular day timing to match the given timing and return to the user
                    if (!day.isEmpty() && timeRange.containsKey(DaysOfWeek.valueOf(day)) && timeRange.get(DaysOfWeek.valueOf(day)).contains(time)) {
                        routeByTiming.add(route);
                    }

                    //If no day is provided then we match any days that match the given timing and return to the user
                    else if (day.isEmpty()) {
                        for (List<String> timeToCheck : timeRange.values()) {
                            if (timeToCheck.contains(time)) {
                                routeByTiming.add(route);
                            }
                        }
                    }
                }
                existingRoute = routeByTiming;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
        return existingRoute;
    }

    /**
     * This method is used to serve the UI with all the available routeId
     *
     * @return list of all route ids
     * @throws IOException when the routes cannot be loaded
     */
    public List<String> getAllRoutesName() throws IOException {
        List<Route> allRoutes = loadRouteValues();
        return allRoutes.stream().map(route -> (route.RouteId)).collect(Collectors.toList());
    }
}
