package com.stacs.bustravel.apitest;

import com.stacs.bustravel.api.dto.AddStop;
import com.stacs.bustravel.api.dto.CreateRoute;
import com.stacs.bustravel.api.RouteController;
import com.stacs.bustravel.model.*;
import com.stacs.bustravel.service.JsontoObjectConverter;
import com.stacs.bustravel.service.RouteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;

public class RouteControllerTest {

    WebTestClient client;

    @BeforeEach
    void setup() throws IOException {
        client = WebTestClient.bindToController(new RouteController()).build();
    }
    @Test
    void initialExpectedValues() {
        client.get().uri("/route/all").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody();

    }

    /**
     * This methods tests the add route endpoint
     */
    @Test
    void addRoute() {

        CreateRoute route = new CreateRoute();
        route.From = UUID.randomUUID().toString();
        route.To = "St-Andrews";
        route.RouteId = "91D";
        route.TimingsPerDay = new Timing[]{new Timing(DaysOfWeek.Sunday, new ArrayList<>(List.of("11:00")))};


        client.post().uri("/route/create").accept(MediaType.APPLICATION_JSON).bodyValue(route)
                .exchange()
                .expectStatus().isOk()
                .expectBody();
    }


    @Test
    void addRouteFailure() {

        CreateRoute route = new CreateRoute();
        //Failing from name
        route.From = "";
        route.To = "St-Andrews";
        route.RouteId = "91D";
        route.TimingsPerDay = new Timing[]{new Timing(DaysOfWeek.Sunday, new ArrayList<>(List.of("11:00")))};


        client.post().uri("/route/create").accept(MediaType.APPLICATION_JSON).bodyValue(route)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody();

        route.From = "St-andrews";
        route.To = "";
        client.post().uri("/route/create").accept(MediaType.APPLICATION_JSON).bodyValue(route)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody();


    }


    /**
     * This methods validates the input of Route
     */
    @Test
    void validRouteDataThrowsBadRequest() {
        CreateRoute route = new CreateRoute();
        AddStop addStop = new AddStop("","","",new StopInRoute());
        client.put().uri("/route/addstop").accept(MediaType.APPLICATION_JSON).bodyValue(addStop)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody();
    }


    @Test
    void testGetRouteUsingStop_NotFound() {
        client.get().uri("/route/getroutebystop/DavidRusselApartment").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody();
    }

    @Test
    void testGetRouteUsingStop_NoContent() throws IOException {
        JsontoObjectConverter mockedJsonToObjectConverter = mock(JsontoObjectConverter.class);
        RouteService service = new RouteService(mockedJsonToObjectConverter);
        WebTestClient client = WebTestClient.bindToController(new RouteController(service)).build();
        client.get().uri("/route/getroutebystop/1234qwer").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody();
    }

}
