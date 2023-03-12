package com.stacs.bustravel.service;

import com.stacs.bustravel.model.DaysOfWeek;
import com.stacs.bustravel.model.Timing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class Helper {
    public String calculateTimeToArrival(String startTime, int travelTime) throws ParseException {

        int hours = travelTime / 60;
        int minutes = travelTime % 60;
        Date startTimeToAddInto = new SimpleDateFormat("HH:mm").parse(startTime);

        Instant instant1 = startTimeToAddInto.toInstant();
        Instant result = instant1.plus(Duration.ofHours(hours)).plus(Duration.ofMinutes(minutes));
        Date resultDate = Date.from(result);
        return String.format("%d:%02d", resultDate.getHours(), resultDate.getMinutes());
    }



    public LinkedHashMap<DaysOfWeek, List<String>> getTimingEachDayForStop(List<Timing> timePerDay, int travelTime) throws ParseException {
        LinkedHashMap<DaysOfWeek, List<String>> timingPerDay = new LinkedHashMap<>();
        for (Timing timing : timePerDay) {
            List<String> timeEachDay = new ArrayList<>();
            for (String time : timing.Timing) {
                timeEachDay.add(calculateTimeToArrival(time, travelTime));
            }
            timingPerDay.put(timing.Day, timeEachDay);
        }
        return timingPerDay;
    }
}

