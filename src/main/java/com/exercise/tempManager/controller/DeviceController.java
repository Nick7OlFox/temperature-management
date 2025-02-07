package com.exercise.tempManager.controller;

import com.exercise.tempManager.common.Constants;
import com.exercise.tempManager.common.UtilityMethods;
import com.exercise.tempManager.response.SuccessMessage;
import com.exercise.tempManager.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    DeviceService deviceService;

    @GetMapping("/hourly-average")
    @ResponseStatus(value = HttpStatus.OK)
    public SuccessMessage<Float> calculateAverageTemp(@RequestParam("device-name") String deviceName, @RequestParam("date-and-hour") String time) {
        return new SuccessMessage<Float>(HttpStatus.OK.value(), new Timestamp(System.currentTimeMillis()), deviceService.deviceHourlyAverage(deviceName, UtilityMethods.convertStringToTimeStamp(time)), Constants.SUCCESS);
    }

}
