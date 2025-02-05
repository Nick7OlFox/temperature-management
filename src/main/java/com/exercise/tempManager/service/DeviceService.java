package com.exercise.tempManager.service;

import com.exercise.tempManager.dto.Record;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;

@Service
@Slf4j
public class DeviceService {

    @Cacheable(value = "deviceAtCertainTime", key = "#device + '@' + #dateAndHour") // This is where we can cache our data
    public float deviceHourlyAverage(String device, Timestamp dateAndHour, ArrayList<Record> testList) {
        // Step 1: Get list of records made by device during date and hour passed
        ArrayList<Record> list = testList;
            // Throw exception if none found

        // Step 2: Return as result the value of the average
        return calculateAverage(list);
    }

    public float calculateAverage(ArrayList<Record> listOfRecordsForDevice) {
        float tempSum = 0;

        for(Record r: listOfRecordsForDevice)
            tempSum += r.getTemperature();

        float average = tempSum/listOfRecordsForDevice.size();
        return average;
    }

    // TODO When we create a new record we must remove delete cache for that device
}
