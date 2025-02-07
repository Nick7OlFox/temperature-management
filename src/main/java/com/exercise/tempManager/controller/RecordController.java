package com.exercise.tempManager.controller;

import com.exercise.tempManager.common.Constants;
import com.exercise.tempManager.dto.Record;
import com.exercise.tempManager.request.RecordRequest;
import com.exercise.tempManager.response.SuccessMessage;
import com.exercise.tempManager.service.DeviceService;
import com.exercise.tempManager.service.RecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/record")
@Slf4j
public class RecordController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private DeviceService deviceService;

    @PostMapping()
    @ResponseStatus(value = HttpStatus.CREATED)
    public SuccessMessage<Record> recordSingleTemperature(@RequestBody RecordRequest request) {
        return new SuccessMessage<>(HttpStatus.CREATED.value(), new Timestamp(System.currentTimeMillis()), recordService.recordTemperature(deviceService.prepareDeviceForRecord(request), request.getTime(), request.getTemperature()), Constants.SUCCESS);
    }

    @PostMapping("/bulk")
    @ResponseStatus(value = HttpStatus.CREATED)
    public SuccessMessage<List<Record>> recordBulkTemperature(@RequestBody RecordRequest[] request) {
        return new SuccessMessage<>(HttpStatus.CREATED.value(), new Timestamp(System.currentTimeMillis()), recordService.recordMultiple(request), Constants.SUCCESS);
    }
}
