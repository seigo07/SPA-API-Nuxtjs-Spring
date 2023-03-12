package com.stacs.bustravel.servicetest;

import com.stacs.bustravel.model.Coordinate;
import com.stacs.bustravel.model.Stop;
import com.stacs.bustravel.service.JsontoObjectConverter;
import com.stacs.bustravel.service.StopService;
import com.sun.source.tree.AssertTree;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class StopServiceTest {

    public StopService service;
    public ArrayList<Stop> newStopsList;

    @BeforeEach
    void setup() {
        newStopsList = new ArrayList<>();
        service = new StopService(newStopsList);
    }

    @AfterEach
    void cleanup() throws IOException {
        JsontoObjectConverter objectConverter = new JsontoObjectConverter();
        objectConverter.revertStopJson();
    }


    //Add a new stop to the existing list of stops
    @Test
    void addStopsToList() throws IOException {
        assertEquals(0, newStopsList.size());
        newStopsList = service.addStop(new Stop(UUID.randomUUID().toString(),new Coordinate()));
        assertTrue(newStopsList.size()>0);

    }

    //Remove an existing stop from list of stops
    @Test
    void removeStopsFromList() throws IllegalArgumentException, IOException {
        List<Stop> existingStops = service.getAllStops();
        Stop stopToAdd = new Stop(UUID.randomUUID().toString(),new Coordinate(1,1));
        newStopsList = service.addStop(stopToAdd);
        assertEquals(existingStops.size()+1, newStopsList.size());
        newStopsList = service.removeStop(stopToAdd);
        assertEquals(existingStops.size(), newStopsList.size());
    }

    //Remove an existing stop from list of stops,
    // should throw exception if the stop doesn't exist
    @Test
    void removeStopsFromListThrowsException() throws IllegalArgumentException {
        Stop stop = new Stop("DRA",new Coordinate(1,1));
        assertEquals(0, newStopsList.size());
        Exception exception = assertThrows(RuntimeException.class, () -> service.removeStop(stop));

        String actualMessage = exception.getMessage();
        String expectedMessage = "Stop with name DRA not found in the list";

        assertEquals(actualMessage, expectedMessage);
    }


    //Remove an existing stop from list of stops using name,
    @Test
    void removeStopFromListUsingName() throws NoSuchElementException, IOException {

        int initialStopSize = service.getAllStops().size();
        assertEquals(0, newStopsList.size());
        Stop stop = new Stop(UUID.randomUUID().toString(),new Coordinate());
        newStopsList = service.addStop(stop);
        assertTrue(newStopsList.size()>0 && newStopsList.size()==initialStopSize+1);

        ArrayList<Stop> newStops = service.removeStopByName(stop.Name);
        assertEquals(initialStopSize, newStops.size());
    }


    //Remove an existing stop from list of stops using name,
    // throws exception if the stop name doesn't exist
    @Test
    void removeStopFromListUsingNameThrowsException() throws NoSuchElementException, IOException {
            Exception exception = assertThrows(NoSuchElementException.class, () -> service.removeStopByName(UUID.randomUUID().toString()));
    }

    /**
     * This method can be used to check if the initial stop values are loaded from the json file correctly
     */
    @Test
    void fetchStopsFromJson() throws IOException {
        ArrayList<Stop> stops = service.loadStopValue();
        assertNotNull(stops);
        assertFalse(stops.isEmpty());
        assertTrue(stops.size() > 0);
    }



        @Test
        void getAllStationNotNulll() throws IOException {
            ArrayList<String> stations = service.getAllStopName();
            assertNotNull(stations);
        }

        @Test
        void addNewStationReturnsListAfterAddition() throws IOException {
        int initialStopSize = service.getAllStops().size();
            Stop stopToAdd = new Stop(UUID.randomUUID().toString(),new Coordinate());
            ArrayList<Stop> stops = service.addStop(stopToAdd);
            assertTrue(stops.contains(stopToAdd));
            assertEquals(stops.size(), initialStopSize + 1);
        }

        @Test
        void addNewStationReturnsThrowsExceptionIfItExists() throws IOException {
            int initialStopSize = service.getAllStops().size();
            Stop stopToAdd = new Stop(UUID.randomUUID().toString(),new Coordinate());
            service.addStop(stopToAdd);
            Exception exception = assertThrows(IllegalArgumentException.class, () -> service.addStop(stopToAdd));

            String actualMessage = exception.getMessage();
            String expectedMessage = "Stop already exists in the list";
            assertTrue(actualMessage.contains(expectedMessage));
        }


    @Test
    void testAddStop() throws IOException {
        List<Stop> existingStops = service.getAllStops();
        // Create a mock Stop object
        Stop stop = new Stop(UUID.randomUUID().toString(), new Coordinate(40.7128, -74.0060));

        // Add the stop to the list
        ArrayList<Stop> stops = service.addStop(stop);

        // Check that the stop was added successfully
        assertEquals(existingStops.size()+1, stops.size());
        assertEquals(stop.getName(), stops.get(stops.size() -1).getName());
        assertEquals(stop.getCoordinates(), stops.get(stops.size() -1).getCoordinates());

        // Try adding the same stop again, which should throw an exception
        assertThrows(IllegalArgumentException.class, () -> service.addStop(stop));

        // Add a different stop to the list
        Stop stop2 = new Stop(UUID.randomUUID().toString(), new Coordinate(41.8781, -87.6298));
        ArrayList<Stop> stops2 = service.addStop(stop2);

        // Check that both stops are in the list
        assertEquals(existingStops.size() + 2, stops2.size());
        assertTrue(stops2.get(stops2.size() - 1).Name.equals(stop2.Name));
        assertTrue(stops2.get(stops2.size() - 1).Coordinates.equals(stop2.getCoordinates()));

        assertTrue(stops2.get(stops2.size() - 2).Name.equals(stop.Name));

    }


    @Test
    void testRemoveStopByName() throws NoSuchElementException {
        // Create some test stops
        Stop stop1 = new Stop("Stop 1", new Coordinate(1.0, 2.0));
        Stop stop2 = new Stop("Stop 2", new Coordinate(3.0, 4.0));
        Stop stop3 = new Stop("Stop 3", new Coordinate(5.0, 6.0));

        // Add the test stops to the stop service
        ArrayList<Stop> stops = new ArrayList<>();
        stops.add(stop1);
        stops.add(stop2);
        stops.add(stop3);
        StopService stopService = new StopService(stops);

        // Remove a stop by name
        String stopNameToRemove = "Stop 2";
        ArrayList<Stop> updatedStops = stopService.removeStopByName(stopNameToRemove);

        // Check that the stop was removed
        assertEquals(2, updatedStops.size());
        assertFalse(updatedStops.contains(stop2));

        // Try to remove a stop that doesn't exist
        String nonexistentStopName = "Nonexistent Stop";
        assertThrows(NoSuchElementException.class, () -> stopService.removeStopByName(nonexistentStopName));
    }

    @Test
    public void testRemoveStopByName_nonexistentStop() throws IOException {
        StopService stopService = new StopService();

        // Try to remove a stop that doesn't exist
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> stopService.removeStopByName("Nonexistent Stop"));

        assertEquals("Stop with name Nonexistent Stop not found in the list", exception.getMessage());
    }

    @Test
    public void testRemoveStopByName_existingStop() throws IOException {
        StopService stopService = new StopService();
        List<Stop> existingStops = service.getAllStops();

        // Add a new stop to the service
        Stop stopToAdd = new Stop(UUID.randomUUID().toString(), new Coordinate(1.23, 4.56));
        List<Stop> stopsAfterAddition = stopService.addStop(stopToAdd);
        assertEquals(existingStops.size() + 1, stopsAfterAddition.size());

        // Remove the stop by name
        ArrayList<Stop> stopsAfterRmoval = stopService.removeStopByName(stopToAdd.Name);

        // Verify that the stop was removed
        assertFalse(stopsAfterRmoval.contains(stopToAdd));
        assertTrue(stopsAfterRmoval.size() == stopsAfterAddition.size());
    }

    @Test
    void testGetAllStopName() throws IOException {
        // create a StopService object
        StopService stopService = new StopService();

        // create some sample stops to add to the service
        Stop stop1 = new Stop(UUID.randomUUID().toString(), new Coordinate(1.0, 2.0));
        Stop stop2 = new Stop(UUID.randomUUID().toString(), new Coordinate(3.0, 4.0));
        Stop stop3 = new Stop(UUID.randomUUID().toString(), new Coordinate(5.0, 6.0));

        // add the stops to the service
        ArrayList<Stop> stops = new ArrayList<>();
        stopService.addStop(stop1);
        stopService.addStop(stop2);
        stopService.addStop(stop3);

        // call the getAllStopName method and store the result
        ArrayList<String> stopNames = stopService.getAllStopName();

        // create an ArrayList of expected stop names
        ArrayList<String> expectedStopNames = new ArrayList<>();
        expectedStopNames.add(stop1.getName());
        expectedStopNames.add(stop2.getName());
        expectedStopNames.add(stop3.getName());

        // compare the actual and expected results
        assertTrue(stopNames.containsAll(expectedStopNames));
    }


}
