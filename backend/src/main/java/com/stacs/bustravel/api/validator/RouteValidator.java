package com.stacs.bustravel.api.validator;

import com.stacs.bustravel.api.dto.AddStop;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RouteValidator {
    public boolean addStopValidator(AddStop addStop){
        //Check if to and from are not the same
        if(addStop.From.equalsIgnoreCase(addStop.To)){
            return false;
        }
        if(addStop.Stop.Name.isEmpty()){
            return false;
        }
        if(addStop.Stop.TravelTimeInMinutes == 0){
            return false;
        }
        if (addStop.RouteId.isEmpty() || addStop.To.isEmpty() || addStop.From.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid route data.");
        }
        return true;
    }
}
