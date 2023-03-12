package com.stacs.bustravel.apitest;

import com.stacs.bustravel.api.StopController;
import com.stacs.bustravel.model.Coordinate;
import com.stacs.bustravel.model.Stop;
import com.stacs.bustravel.service.JsontoObjectConverter;
import com.stacs.bustravel.service.RouteService;
import com.stacs.bustravel.service.StopService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StopControllerTest {

    WebTestClient client;

    @BeforeEach
    void setup() throws IOException {
        JsontoObjectConverter mockedJsonToObjectConverter = mock(JsontoObjectConverter.class);
        StopService service = new StopService(mockedJsonToObjectConverter);
        client = WebTestClient.bindToController(new StopController()).build();
    }

    @Test
    void getAllStoredStops() {
        client.get().uri("/stop/all").accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectBody();
    }

    @Test
    void saveNewStopReturn_Success() {
        Stop stop = new Stop(UUID.randomUUID().toString(), new Coordinate(12, 12));
        client.post().uri("/stop/add").accept(MediaType.APPLICATION_JSON).bodyValue(stop).exchange().expectStatus().isCreated();
    }

    @Test
    void saveNewStopReturnSuccess() throws IOException {
        // arrange
        StopService stopService = mock(StopService.class);
        when(stopService.addStop(any(Stop.class))).thenReturn(new ArrayList<>());
        Stop stop = new Stop("Stop A", new Coordinate(10, 20));
        StopController stopController = new StopController(stopService);

        // act
        ResponseEntity<String> response = stopController.addStop(stop);

        // assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Added stop", response.getBody());
    }

    @Test
    void testAddStop_alreadyExists() throws IOException {
        // arrange
        StopService stopService = mock(StopService.class);
        when(stopService.addStop(any(Stop.class))).thenThrow(new IllegalArgumentException("Stop already exists"));
        Stop stop = new Stop(UUID.randomUUID().toString(), new Coordinate(10, 20));
        StopController stopController = new StopController(stopService);

        // act
        ResponseEntity<String> response = stopController.addStop(stop);

        // assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Stop already exists", response.getBody());
    }

    @Test
    void testAddStop_throwsConflict() {
        Stop stop = new Stop(UUID.randomUUID().toString(), new Coordinate(12, 12));
        client.post().uri("/stop/add").accept(MediaType.APPLICATION_JSON).bodyValue(stop).exchange().expectStatus().isCreated();
        client.post().uri("/stop/add").accept(MediaType.APPLICATION_JSON).bodyValue(stop).exchange().expectStatus().is4xxClientError();
    }


    @Test
    void removeStopFromListReturnsSuccess() throws IOException {
        Stop stop = new Stop("David Russel Apartment", new Coordinate(56.33624733610355, -2.8222816859969853));
        client.post().uri("/stop/add").accept(MediaType.APPLICATION_JSON).bodyValue(stop).exchange().expectStatus().isCreated();
        client.post().uri("/stop/remove").accept(MediaType.APPLICATION_JSON).bodyValue(stop).exchange().expectStatus().is2xxSuccessful();
    }
    @Test
    void removeStopFromListThrowsErrorIfNotFound() {
        Stop stop = new Stop("David Russel", new Coordinate(56.33624733610355, -2.8222816859969853));
        client.post().uri("/stop/remove").accept(MediaType.APPLICATION_JSON).bodyValue(stop).exchange().expectStatus().isBadRequest();
    }


    @Test
    void getAllStopNames() {
        client.get().uri("/stop/all/names").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody();
    }

    @Test
    void addBusStopReturnsListIfSuccessfull() {
        ArrayList<Stop> expectedOutput = new ArrayList<>();
        Stop newStop = new Stop("St-ANDREWS",new Coordinate());
        expectedOutput.add(newStop);

        client.post().uri("/stop/add").accept(MediaType.APPLICATION_JSON).bodyValue(new Stop(UUID.randomUUID().toString(),new Coordinate())).exchange().expectStatus().isCreated();

    }

}
