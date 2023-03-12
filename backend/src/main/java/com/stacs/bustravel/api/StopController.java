package com.stacs.bustravel.api;

import com.stacs.bustravel.model.Stop;
import com.stacs.bustravel.service.StopService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Rest Controller for handling HTTP requests related to stops.
 */
@RestController
@RequestMapping("/stop")
public class StopController {
    private final StopService service;

    /**
     * Constructor that initializes the StopService object.
     *
     * @throws IOException if an I/O error occurs while initializing the StopService.
     */
    public StopController() throws IOException {
        this.service = new StopService();
    }

    public StopController(StopService service) throws IOException {
        this.service = service;
    }

    /**
     * Retrieves a list of all stops.
     *
     * @return List of Stop objects.
     * @throws IOException if an I/O error occurs while retrieving the stops.
     */
    @GetMapping("/all")
    public List<Stop> getAllStop() throws IOException {
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(service.getAllStops()).getBody();
    }

    /**
     * Retrieves a list of all stop names.
     *
     * @return ArrayList of stop names.
     * @throws IOException if an I/O error occurs while retrieving the stop names.
     */
    @GetMapping("/all/names")
    public ArrayList<String> getAllStopName() throws IOException {
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(service.getAllStopName()).getBody();
    }

    /**
     * Adds a new stop.
     *
     * @param stop The Stop object to add.
     * @return ResponseEntity with a message indicating whether the stop was added successfully.
     * @throws RuntimeException if an error occurs while saving the stop to JSON.
     */
    @PostMapping("/add")
    public ResponseEntity<String> addStop(@RequestBody Stop stop) {
        try {
            service.addStop(stop);
            return new ResponseEntity<>("Added stop", HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save stops to JSON");
        }
    }

    /**
     * Removes a stop.
     *
     * @param stop The Stop object to remove.
     * @return ResponseEntity with a message indicating whether the stop was removed successfully.
     * @throws Exception if an error occurs while removing the stop.
     */
    @PostMapping("/remove")
    public ResponseEntity<String> removeStop(@RequestBody Stop stop) {
        try {
            service.removeStop(stop);
            return new ResponseEntity<>("Removed Stop", HttpStatus.OK);

            //Remove stops from routes

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}
