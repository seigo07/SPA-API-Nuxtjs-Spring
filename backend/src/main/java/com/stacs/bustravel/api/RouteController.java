package com.stacs.bustravel.api;

import com.stacs.bustravel.api.dto.AddStop;
import com.stacs.bustravel.api.dto.CreateRoute;
import com.stacs.bustravel.api.validator.RouteValidator;
import com.stacs.bustravel.api.validator.Validator;
import com.stacs.bustravel.model.Route;
import com.stacs.bustravel.model.Stop;
import com.stacs.bustravel.service.RouteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Controller for handling HTTP requests related to bus routes.
 */
@RestController
@RequestMapping("/route")
public class RouteController {
    RouteService service = new RouteService();

    /**
     * Constructor for creating a new instance of RouteController.
     * @throws IOException if an I/O error occurs while creating the instance.
     */
    public RouteController() throws IOException {
    }

    /**
     * Constructor for creating a new instance of RouteController using routeService parameter.
     * @param routeService - route service for instantiation
     * @throws IOException if an I/O error occurs while creating the instance.
     */
    public RouteController(RouteService routeService) throws IOException {
        this.service = routeService;
    }

    /**
     * Retrieves a list of all available bus routes.
     * @return the list of bus routes.
     */
    @GetMapping("/all")
    public ArrayList<Route> getRoute() {
        try {
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(service.getAllRoutes()).getBody();
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    /**
     * Adds a new bus route to the system.
     * @param addRoute the details of the new route.
     * @return the list of all bus routes, including the newly added route.
     */
    @PostMapping("/create")
    public ArrayList<Route> addRoute(@RequestBody CreateRoute addRoute) throws Exception {
        try {
            if (addRoute.RouteId.isEmpty() || addRoute.To.isEmpty() || addRoute.From.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid route data.");
            }
            Route route = new Route();
            route.RouteId = addRoute.RouteId;
            route.From = addRoute.From;
            route.To = addRoute.To;
            route.Stops = addRoute.Stops;
            route.TimingsPerDay = addRoute.TimingsPerDay;
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(service.addRoute(route)).getBody();
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a new stop to an existing bus route.
     * @param addStop the details of the stop to be added.
     * @return the updated bus route containing the new stop.
     */
    @PutMapping("/addstop")
    public ResponseEntity<Object> addRouteStop(@RequestBody AddStop addStop) {
        try {
            if (!new RouteValidator().addStopValidator(addStop)) {
                return new ResponseEntity<>("Invalid route data.", HttpStatus.BAD_REQUEST);
            }
            Route route = service.addStopToRoute(addStop.RouteId, addStop.From, addStop.To, addStop.Stop);
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(route);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a list of all bus routes that pass through a specified stop.
     * @param stopName the name of the stop.
     * @return the list of bus routes that pass through the stop.
     * @throws IOException if an I/O error occurs while retrieving the routes.
     */
    @GetMapping("/getroutebystop/{stopName}")
    public ResponseEntity<Object> getAllRoutesByStopName(@PathVariable String stopName) throws IOException {
        try {
            if (stopName.isEmpty()) {
                return new ResponseEntity<>("Please enter a valid stop name.", HttpStatus.BAD_REQUEST);
            }
            List<Route> routes = service.getAllRoutesByStop(stopName);
            return ResponseEntity.status(HttpStatusCode.valueOf(routes.size() >0 ? 200 : 204)).body(routes);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**

     This method retrieves all the routes that go through a particular stop on a given day.
     @param stopName the name of the stop
     @param day the day for which the routes are requested
     @return a ResponseEntity containing a List of Routes
     @throws ResponseStatusException if the stopName or day is empty or if an exception occurs
     */
    @GetMapping("/getroute/{stopName}/{day}")
    public ResponseEntity<List<Route>> getAllRoutesByStopNameAndDay(@PathVariable String stopName, @PathVariable String day) {
        try {
            if (stopName.isEmpty() || day.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid stop name or day.");
            }
            List<Route> routes = service.getAllRoutesByStopAndDay(stopName, day);
            if (routes.size() == 0) {
                return ResponseEntity.status(HttpStatusCode.valueOf(204)).body(routes);
            }
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(routes);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }


    /**

     Retrieves a list of all routes based on the given parameters(stop,day,time).
     @param stopName The name of the stop to retrieve routes for.
     @param day The day of the week to retrieve routes for. (Optional)
     @param time The time of day to retrieve routes for. (Optional)
     @return ResponseEntity with a list of routes that match the given parameters.
     @throws ResponseStatusException if an error occurs while retrieving the routes.
     */
    @GetMapping("/getroute")
    public ResponseEntity<Object> getAllRoutesByParameters(@RequestParam(name = "stop") String stopName, @RequestParam(name = "day",defaultValue = "") String day, @RequestParam(name = "time",defaultValue = "") String time) {
        try {
            if (!day.isEmpty() && !new Validator().dayValidator(day)) {
                return new ResponseEntity<>("Invalid day. Enter one of the following values " + new Validator().allDaysOfWeek(), HttpStatus.BAD_REQUEST);
            }
            if (!time.isEmpty() && !new Validator().timeValidator(time)) {
                return new ResponseEntity<>("Invalid Time format in the Url - Please enter of the type HH:MM", HttpStatus.BAD_REQUEST);
            }
            List<Route> routes = service.getAllRoutesByParameters(stopName, day, time);
            if (routes.size() == 0) {
                return ResponseEntity.status(HttpStatusCode.valueOf(204)).body(routes);
            }
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(routes);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }


    /**

     Retrieves a list of all route IDs.
     @return ResponseEntity with a list of all route IDs.
     @throws ResponseStatusException if an error occurs while retrieving the route IDs.
     */
    @GetMapping("/allrouteid")
    public ResponseEntity<List<String>> getAllRouteId() {
        try {
            return new ResponseEntity<>(service.getAllRoutesName(), HttpStatus.OK);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

