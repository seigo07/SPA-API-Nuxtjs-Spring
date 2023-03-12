package com.stacs.bustravel.api.validator;

import com.stacs.bustravel.model.DaysOfWeek;

import java.text.SimpleDateFormat;

public class Validator {

    public boolean timeValidator(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm"); //HH = 24h format
        dateFormat.setLenient(false); //for example - this will not enable 25:00
        try {
            dateFormat.parse(time);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean dayValidator(String day) {
        try {
            DaysOfWeek.valueOf(day);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public String allDaysOfWeek() {
        StringBuilder allDays = new StringBuilder();
        DaysOfWeek[] daysOfWeeks = DaysOfWeek.class.getEnumConstants();
        for (int index = 0; index < daysOfWeeks.length; index++) {
            allDays.append(daysOfWeeks[index].toString());
            if(index != daysOfWeeks.length -1 )  allDays.append(", ");
        }
        return allDays.toString();

    }
}
