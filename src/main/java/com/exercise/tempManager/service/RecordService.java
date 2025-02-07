package com.exercise.tempManager.service;

import com.exercise.tempManager.common.Constants;
import com.exercise.tempManager.common.UtilityMethods;
import com.exercise.tempManager.dto.Device;
import com.exercise.tempManager.dto.Record;
import com.exercise.tempManager.repository.RecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class RecordService {

    @Autowired
    DeviceService deviceService;

    @Autowired
    RecordRepository recordRepository;

    /**
     * This method will register the temperature record for the passed device
     * @param device The device that registered the temperature
     * @param timeOfRecord Timestampt at which the record was done
     * @param temperature Temperature recorded
     * @return The recorded entry as saved in the database
     */
    @CacheEvict(value = Constants.DEVICE_AT_CERTAIN_TIME, key = "#device.deviceName + '@' + #dateAndHour")
    public Record recordTemperature(Device device, Timestamp timeOfRecord, Float temperature){
        // Save to DB
        Record newRecord = new Record(device.getDeviceName(), timeOfRecord, temperature);
        log.info("Saving new record for device: " + device.getDeviceName() + "(" + temperature + "C°, " + timeOfRecord + ")");
        newRecord = recordRepository.save(newRecord);

        // Make sure we invalidate the cache for the hour we just registered
        String dateAndHour = UtilityMethods.convertTimeToString(timeOfRecord);
        log.debug("Evicting cache: deviceAtCertainTime" + device.getDeviceName() + "@" + dateAndHour);

        return newRecord;
    }
}
