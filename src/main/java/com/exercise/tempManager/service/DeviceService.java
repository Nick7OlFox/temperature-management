package com.exercise.tempManager.service;

import com.exercise.tempManager.common.Constants;
import com.exercise.tempManager.common.UtilityMethods;
import com.exercise.tempManager.dto.Device;
import com.exercise.tempManager.exceptions.DeviceNotFoundException;
import com.exercise.tempManager.exceptions.RecordNotFoundException;
import com.exercise.tempManager.repository.DeviceRepository;
import com.exercise.tempManager.request.RecordRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
public class DeviceService {

    @Autowired
    DeviceRepository deviceRepository;

    /**
     * This method will be used when recording a temperature. It will either get the existing device from the db or it
     * if that is not found it will create a new device
     *
     * @param request The request where the device information is present
     * @return The device that made the request
     */
    public Device prepareDeviceForRecord(RecordRequest request) {
        Device device;
        try {
            device = findDevice(request.getDeviceName());
        } catch (DeviceNotFoundException ex) {
            log.warn("Registering a new device: " + request.getDeviceName());
            device = registerDevice(request.getDeviceName(), request.getLocation());
        }
        return device;
    }

    /**
     * This method will return the desired device from the database
     *
     * @param deviceName The name of the device
     * @return A device object populated with the data from the database
     * @throws DeviceNotFoundException It will be thrown if the method cannot find a device with the passed name
     */
    @Cacheable(value = Constants.DEVICE, key = "#deviceName")
    public Device findDevice(String deviceName) throws DeviceNotFoundException {
        log.info("Looking for device: " + deviceName);
        Optional<Device> response = deviceRepository.findById(deviceName);

        // If it doesn't exist then we throw exception so we can register the device
        if (!response.isPresent()) {
            String message = "Device with name: " + deviceName + " was not found";
            log.warn(message);
            throw new DeviceNotFoundException(message);
        }

        log.info("Device found");
        return response.get();
    }

    /**
     * This method will register a new device
     *
     * @param deviceName The name of the device
     * @param location   The location of the device
     * @return A Device object as saved in the database
     */
    @CachePut(value = Constants.DEVICE, key = "#deviceName")
    public Device registerDevice(String deviceName, String location) {
        // If this is being called we already tried to find the device name so we can safely create it
        log.debug("Registering device: " + deviceName);
        Device deviceToCreate = new Device(deviceName, location);
        deviceToCreate = deviceRepository.save(deviceToCreate);

        log.debug("Device created");
        return deviceToCreate;
    }

    /**
     * This method will retrieve an average of the temperatures registered by a given device in a given 1 hour
     * interval
     *
     * @param deviceName      The name of the device
     * @param dateAndHour The date and hour for which the calculation needs to be performed
     * @return The value of the calculated average
     */
    @Cacheable(value = Constants.DEVICE_AT_CERTAIN_TIME, key = "#deviceName + '*' + T(com.exercise.tempManager.common.UtilityMethods).convertTimeToString(#dateAndHour)")    @Transactional
    public float deviceHourlyAverage(String deviceName, Timestamp dateAndHour) {
        // Retrieve records
        log.info("Calculating average temperature for device " + deviceName + " at " + dateAndHour);
        ArrayList<Float> list = deviceRepository.getRecordsForTime(deviceName, dateAndHour);
        if (list.isEmpty()) {
            String message = "No records found for device " + deviceName + " at " + dateAndHour;
            log.warn(message);
            throw new RecordNotFoundException(message);
        }

        // Save cache
//        String cacheTime = UtilityMethods.convertTimeToString(dateAndHour);

        // Return average of results fetched
        return calculateAverage(list);
    }

    /**
     * This method will take a list of temperatures and calculate the average
     *
     * @param listOfRecordsForDevice An array with the recorded temperatures
     * @return The average of all the temperatures
     */
    private float calculateAverage(ArrayList<Float> listOfRecordsForDevice) {
        float tempSum = 0;

        for (Float r : listOfRecordsForDevice)
            tempSum += r;

        float average = tempSum / listOfRecordsForDevice.size();
        return average;
    }
}
