package com.stacs.bustravel.servicetest;

import com.stacs.bustravel.model.*;
import com.stacs.bustravel.service.JsontoObjectConverter;
import com.stacs.bustravel.service.RouteService;
import com.stacs.bustravel.service.StopService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.annotation.processing.RoundEnvironment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RouteServiceTest {
    RouteService service;
    JsontoObjectConverter mockedJsonToObjectConverter;

    public RouteServiceTest() throws IOException {
    }

    @BeforeEach
    void setup() throws IOException {
        mockedJsonToObjectConverter = mock(JsontoObjectConverter.class);
        service = mock(RouteService.class);
    }

   @AfterEach
   void cleanup() throws IOException {
       JsontoObjectConverter objectConverter = new JsontoObjectConverter();
       objectConverter.revertRoutesJson();
   }

    @Test
    void getAllRouteNotNulll() throws IOException {
        ArrayList<Route> routes = service.getAllRoutes();
        assertNotNull(routes);
    }

    @Test
    void addNewRouteReturnsListAfterAddition() throws Exception {
        ArrayList<Route> existingRoute = service.getAllRoutes();
        Route newRoute = new Route();
        newRoute.From = UUID.randomUUID().toString();
        newRoute.To = "St-Andrews";
        newRoute.RouteId = "92D";
        newRoute.TimingsPerDay = new Timing[]{new Timing(DaysOfWeek.Sunday, new ArrayList<>(List.of("11:00")))};
        when(service.addRoute(newRoute)).thenReturn(new ArrayList<>(Arrays.asList(newRoute)));
        ArrayList<Route> routes = service.addRoute(newRoute);
        assertFalse(routes.isEmpty());
        assertEquals(existingRoute.size() + 1, routes.size());
    }

    @Test
    void addNewRouteThrowsErrorIfItExists() throws Exception {
        Route newRoute = new Route();
        newRoute.From = UUID.randomUUID().toString();
        newRoute.To = "St-Andrews";
        newRoute.RouteId = "91D";
        newRoute.TimingsPerDay = new Timing[]{new Timing(DaysOfWeek.Sunday, new ArrayList<>(List.of("11:00")))};
        when(service.addRoute(newRoute)).thenReturn(new ArrayList<>(Arrays.asList(newRoute)));
        ArrayList<Route> routes = service.addRoute(newRoute);
        assertFalse(routes.isEmpty());

        ArrayList<Route> mockData = new ArrayList<>();
        mockData.add(newRoute);
        when(mockedJsonToObjectConverter.loadRouteValues()).thenReturn(mockData);
        RouteService routeService = new RouteService(mockedJsonToObjectConverter);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> routeService.addRoute(newRoute));
        String actualMessage = exception.getMessage();
        String expectedMessage = "Route already exists";
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void addNewRouteThrowsErrorForIncorrectData() throws Exception {
        Route newRoute = new Route();
        newRoute.From = UUID.randomUUID().toString();
        newRoute.To = "St-Andrews";
        newRoute.RouteId = "91D";
        //Incorrect data - timingPerDay
        newRoute.TimingsPerDay = null;
        ArrayList<Route> mockData = new ArrayList<>();
        mockData.add(newRoute);
        when(mockedJsonToObjectConverter.loadRouteValues()).thenReturn(new ArrayList<>());
        RouteService routeService = new RouteService(mockedJsonToObjectConverter);
        Exception exception = assertThrows(Exception.class, () -> routeService.addRoute(newRoute));
        String actualError = exception.getMessage();
        String expectedError = "Cannot read the array length because \"array\" is null";
        assertEquals(expectedError, (actualError));
    }

    @Test
    public void testGetAllRoutes() throws Exception {
        // Initialize a Route object
        Route newRoute = new Route();
        newRoute.From = UUID.randomUUID().toString();
        newRoute.To = "St-Andrews";
        newRoute.RouteId = "91D";
        newRoute.TimingsPerDay = new Timing[]{new Timing(DaysOfWeek.Sunday, new ArrayList<>(List.of("11:00")))};

        // Create an ArrayList of Route objects and add the initialized object to it
        ArrayList<Route> expectedRoutes = new ArrayList<>();
        expectedRoutes.add(newRoute);
        when(service.addRoute(newRoute)).thenReturn(new ArrayList<>(Arrays.asList(newRoute)));
        service.addRoute(newRoute);

        when(mockedJsonToObjectConverter.loadRouteValues()).thenReturn(expectedRoutes);

        // Call the getAllRoutes() method and verify that it returns the expected list of routes
        try {
            when(service.getAllRoutes()).thenReturn(new ArrayList<>(Arrays.asList(newRoute)));
            ArrayList<Route> actualRoutes = service.getAllRoutes();
            assertEquals(actualRoutes.get(actualRoutes.size() - 1).From, (expectedRoutes.get(0).From));
            assertEquals(actualRoutes.get(actualRoutes.size() - 1).To, (expectedRoutes.get(0).To));
            assertEquals(actualRoutes.get(actualRoutes.size() - 1).RouteId, (expectedRoutes.get(0).RouteId));
            assertEquals(actualRoutes.get(actualRoutes.size() - 1).Stops, (expectedRoutes.get(0).Stops));

        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetAllRoutesThrowsExceptionWHenMockingJsonInput() throws IOException {
        // Create a mock list of routes
        ArrayList<Route> mockRoutes = new ArrayList<>();
        Timing[] timings = new Timing[0];
        StopInRoute[] stops = new StopInRoute[0];
        mockRoutes.add(new Route("Route A", UUID.randomUUID().toString(), UUID.randomUUID().toString(), timings, stops));

        // Set up the MockedJsonToObjectConverter to return the mock list of routes

        when(mockedJsonToObjectConverter.loadRouteValues()).thenReturn(mockRoutes);

        // Set up the RouteService with the MockedJsonToObjectConverter
        RouteService routeService = new RouteService(mockedJsonToObjectConverter);

        // Call the getAllRoutes() method and verify that it returns the mock list of routes
        ArrayList<Route> actualRoutes = routeService.getAllRoutes();
        assertEquals(mockRoutes, actualRoutes);
    }

    @Test
    public void testGetAllRoutesExceptionWhenMockingJsonService() throws IOException {
        when(mockedJsonToObjectConverter.loadRouteValues()).thenThrow(new IOException());
        RouteService routeService = new RouteService(mockedJsonToObjectConverter);
        Exception exception = assertThrows(IOException.class, () -> routeService.getAllRoutes());
    }

    @Test
    public void testGetAllRoutesEmpty() throws IOException {
        when(mockedJsonToObjectConverter.loadRouteValues()).thenReturn(new ArrayList<>());
        RouteService routeService = new RouteService(mockedJsonToObjectConverter);
        ArrayList<Route> routes = routeService.getAllRoutes();
        assertEquals(0, routes.size());
    }


    @Test
    void testAddStopToRoute_success() throws Exception {
        // arrange
        String routeId = "R001";
        String from = "Bus Station A";
        String to = "Bus Station B";
        StopInRoute stop = new StopInRoute(UUID.randomUUID().toString(),new Coordinate(),10);

        Timing[] timings = new Timing[]{new Timing(DaysOfWeek.Sunday, new ArrayList<>(List.of("11:00")))};

        Route route = new Route(routeId, from, to, timings, new StopInRoute[]{});

        ArrayList<Route> allRoutes = new ArrayList<>();
        allRoutes.add(route);

        ArrayList<Route> routes = new ArrayList<>();
        routes.add(route);
        JsontoObjectConverter objectConverter = mock(JsontoObjectConverter.class);
        StopService stopService = mock(StopService.class);
        when(mockedJsonToObjectConverter.loadRouteValues()).thenReturn(allRoutes);
        RouteService service = new RouteService(routes);

        // act
        Route result =service.addStopToRoute(routeId, from, to, stop);

        // assert
        assertEquals(1, result.Stops.length);
        assertEquals(stop, result.Stops[0]);
    }


    @Test
    void testGetAllRouteByStop_success() throws Exception {
        // arrange
        String routeId = "R001";
        String from = "Bus Station A";
        String to = "Bus Station B";
        StopInRoute stop = new StopInRoute();
        Timing[] timings = new Timing[]{new Timing(DaysOfWeek.Sunday, new ArrayList<>(List.of("11:00")))};
        StopInRoute stopToAdd = new StopInRoute(from, new Coordinate(1, 1), 10);
        StopInRoute[] stops = new StopInRoute[]{stopToAdd};

        Route route = new Route(routeId, from, to, timings, stops);

        ArrayList<Route> allRoutes = new ArrayList<>();
        allRoutes.add(route);

        List<Route> routes = List.of(route);
        JsontoObjectConverter objectConverter = mock(JsontoObjectConverter.class);
        StopService stopService = mock(StopService.class);
        when(objectConverter.loadRouteValues()).thenReturn(allRoutes);

        // act
        List<Route> result = new RouteService(objectConverter, stopService).getAllRoutesByStop(from);
        // assert
        assertEquals(1, result.size());
        assertEquals(from, result.get(0).From);
        assertEquals(to, result.get(0).To);
        assertEquals(routeId, result.get(0).RouteId);
    }


    @Test
    void testGetAllRouteByStop_NotFound() throws Exception {
        // arrange
        String routeId = "R001";
        String from = "Bus Station A";
        String to = "Bus Station B";
        StopInRoute stop = new StopInRoute();
        Timing[] timings = new Timing[]{new Timing(DaysOfWeek.Sunday, new ArrayList<>(List.of("11:00")))};
        StopInRoute stopToAdd = new StopInRoute(from, new Coordinate(1, 1), 10);
        StopInRoute[] stops = new StopInRoute[]{stopToAdd};

        Route route = new Route(routeId, from, to, timings, stops);

        ArrayList<Route> allRoutes = new ArrayList<>();
        allRoutes.add(route);

        List<Route> routes = List.of(route);
        JsontoObjectConverter objectConverter = mock(JsontoObjectConverter.class);
        StopService stopService = mock(StopService.class);
        when(objectConverter.loadRouteValues()).thenReturn(allRoutes);

        // act
        List<Route> result = new RouteService(objectConverter, stopService).getAllRoutesByStop("stop1");
        // assert - stop not found
        assertEquals(0, result.size());
    }

    @Test
    void testGetAllRouteByStopAndDay_Success() throws Exception {
        // arrange
        String routeId = "R001";
        String from = "Bus Station A";
        String to = "Bus Station B";
        StopInRoute stop = new StopInRoute();
        Timing[] timings = new Timing[]{new Timing(DaysOfWeek.Sunday, new ArrayList<>(List.of("11:00")))};
        StopInRoute stopToAdd = new StopInRoute(from, new Coordinate(1, 1), 10);
        StopInRoute[] stops = new StopInRoute[]{stopToAdd};
        DaysOfWeek[] days = new DaysOfWeek[]{DaysOfWeek.Sunday};

        Route route = new Route(routeId, from, to, timings, stops, days);

        ArrayList<Route> allRoutes = new ArrayList<>();
        allRoutes.add(route);

        List<Route> routes = List.of(route);
        JsontoObjectConverter objectConverter = mock(JsontoObjectConverter.class);
        StopService stopService = mock(StopService.class);
        when(objectConverter.loadRouteValues()).thenReturn(allRoutes);

        // act
        List<Route> result = new RouteService(objectConverter, stopService).getAllRoutesByStopAndDay(from, "Sunday");
        // assert - stop not found
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllRouteByStopAndDay_NotFound() throws Exception {
        // arrange
        String routeId = "R001";
        String from = "Bus Station A";
        String to = "Bus Station B";
        StopInRoute stop = new StopInRoute();
        Timing[] timings = new Timing[]{new Timing(DaysOfWeek.Sunday, new ArrayList<>(List.of("11:00")))};
        StopInRoute stopToAdd = new StopInRoute(from, new Coordinate(1, 1), 10);
        StopInRoute[] stops = new StopInRoute[]{stopToAdd};
        DaysOfWeek[] days = new DaysOfWeek[]{DaysOfWeek.Sunday};

        Route route = new Route(routeId, from, to, timings, stops, days);

        ArrayList<Route> allRoutes = new ArrayList<>();
        allRoutes.add(route);

        List<Route> routes = List.of(route);
        JsontoObjectConverter objectConverter = mock(JsontoObjectConverter.class);
        StopService stopService = mock(StopService.class);
        when(objectConverter.loadRouteValues()).thenReturn(allRoutes);

        // act
        List<Route> result = new RouteService(objectConverter, stopService).getAllRoutesByStopAndDay(from, "Monday");
        // assert - Route not found
        assertEquals(0, result.size());
    }

    @Test
    void testGetAllRouteByStopDayTime_Success() throws Exception {
        // arrange
        String routeId = "R001";
        String from = "Bus Station A";
        String to = "Bus Station B";
        StopInRoute stop = new StopInRoute();
        Timing[] timings = new Timing[]{new Timing(DaysOfWeek.Sunday, new ArrayList<>(List.of("11:00")))};
        StopInRoute stopToAdd = new StopInRoute(from, new Coordinate(1, 1), 10);
        StopInRoute[] stops = new StopInRoute[]{stopToAdd};
        DaysOfWeek[] days = new DaysOfWeek[]{DaysOfWeek.Sunday};

        Route route = new Route(routeId, from, to, timings, stops, days);

        ArrayList<Route> allRoutes = new ArrayList<>();
        allRoutes.add(route);

        List<Route> routes = List.of(route);
        JsontoObjectConverter objectConverter = mock(JsontoObjectConverter.class);
        StopService stopService = mock(StopService.class);
        when(objectConverter.loadRouteValues()).thenReturn(allRoutes);

        // act
        List<Route> result = new RouteService(objectConverter, stopService).getAllRoutesByParameters(from, "Sunday", "11:10");
        // assert - Route not found
        assertEquals(1, result.size());

    }

    @Test
    void testGetAllRouteByStopDayTime_NotFound() throws Exception {
        // arrange
        String routeId = "R001";
        String from = "Bus Station A";
        String to = "Bus Station B";
        StopInRoute stop = new StopInRoute();
        Timing[] timings = new Timing[]{new Timing(DaysOfWeek.Sunday, new ArrayList<>(List.of("11:00")))};
        StopInRoute stopToAdd = new StopInRoute(from, new Coordinate(1, 1), 10);
        StopInRoute[] stops = new StopInRoute[]{stopToAdd};
        DaysOfWeek[] days = new DaysOfWeek[]{DaysOfWeek.Sunday};

        Route route = new Route(routeId, from, to, timings, stops, days);

        ArrayList<Route> allRoutes = new ArrayList<>();
        allRoutes.add(route);

        List<Route> routes = List.of(route);
        JsontoObjectConverter objectConverter = mock(JsontoObjectConverter.class);
        StopService stopService = mock(StopService.class);
        when(objectConverter.loadRouteValues()).thenReturn(allRoutes);

        // act
        List<Route> result = new RouteService(objectConverter, stopService).getAllRoutesByParameters(from, "Monday", "11:15");
        // assert - Route not found
        assertEquals(0, result.size());

        List<Route> result1 = new RouteService(objectConverter, stopService).getAllRoutesByParameters(from, "Monday", "12:15");
        // assert - Route not found
        assertEquals(0, result1.size());

        List<Route> result2 = new RouteService(objectConverter, stopService).getAllRoutesByParameters(from, "Sunday", "11:15");
        // assert - Route not found
        assertEquals(0, result2.size());
        List<Route> result3 = new RouteService(objectConverter, stopService).getAllRoutesByParameters(to, "Monday", "11:15");
        // assert - Route not found
        assertEquals(0, result3.size());

    }

    @Test
    void testGetAllRoutesName_multipleRoutes() throws IOException {
        // arrange
        ArrayList<Route> routes = new ArrayList<>();
        routes.add(new Route("R001", "Bus Station A", "Bus Station B", new Timing[]{new Timing(DaysOfWeek.Sunday, new ArrayList<>(List.of("11:00")))}, new StopInRoute[]{}));
        routes.add(new Route("R002", "Bus Station B", "Bus Station C", new Timing[]{new Timing(DaysOfWeek.Sunday, new ArrayList<>(List.of("10:50")))}, new StopInRoute[]{}));
        routes.add(new Route("R003", "Bus Station C", "Bus Station D", new Timing[]{new Timing(DaysOfWeek.Sunday, new ArrayList<>(List.of("12:00")))}, new StopInRoute[]{}));


        when(mockedJsonToObjectConverter.loadRouteValues()).thenReturn(routes);
        RouteService routeService = new RouteService(mockedJsonToObjectConverter, null);

        // act
        List<String> result = routeService.getAllRoutesName();

        // assert
        assertEquals(3, result.size());
        assertTrue(result.contains("R001"));
        assertTrue(result.contains("R002"));
        assertTrue(result.contains("R003"));
    }

}
