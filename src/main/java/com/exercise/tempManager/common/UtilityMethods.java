package com.exercise.tempManager.common;

import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class UtilityMethods {

    /**
     * This is a utlity method that will transform the date sent in the request into a valid timestamp
     * @param input A string with a date in the format {@value Constants#AVERAGE_REQUEST_DATE_FORMAT}
     * @return
     */
    public static Timestamp convertStringToTimeStamp(String input){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.AVERAGE_REQUEST_DATE_FORMAT);
            Date parsedDate = dateFormat.parse(input);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            return timestamp;
        } catch(Exception e) {
            log.warn("Error during data conversion: " + e.getMessage());
            throw new RuntimeException("There was an error when processing the data passed in the request");
        }
    }

    public static String convertTimeToString(Timestamp input){
        try {
            // Make sure we invalidate the cache for the hour we just registered
            SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.AVERAGE_REQUEST_DATE_FORMAT);
            String dateAndHour = dateFormat.format(new Date(input.getTime()));
            return dateAndHour;
        } catch(Exception e) {
            log.warn("Error during data conversion: " + e.getMessage());
            throw new RuntimeException("There was an error when processing the data passed in the request");
        }
    }
}
