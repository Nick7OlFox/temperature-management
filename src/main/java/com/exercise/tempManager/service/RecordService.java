package com.exercise.tempManager.service;

import com.exercise.tempManager.common.Constants;
import com.exercise.tempManager.common.UtilityMethods;
import com.exercise.tempManager.dto.Device;
import com.exercise.tempManager.dto.Record;
import com.exercise.tempManager.exceptions.BulkProcessingFailureException;
import com.exercise.tempManager.exceptions.IncompleteProcessException;
import com.exercise.tempManager.repository.RecordRepository;
import com.exercise.tempManager.request.RecordRequest;
import com.exercise.tempManager.response.PartialSuccessMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
        // Input safety
        if(temperature == null)
            throw new IllegalArgumentException("No temperature was passed for(" + device.getDeviceName() + " @ " + timeOfRecord + ")");

        // Save to DB
        Record newRecord = new Record(device.getDeviceName(), timeOfRecord, temperature);
        log.info("Saving new record for device: " + device.getDeviceName() + "(" + temperature + "C°, " + timeOfRecord + ")");
        newRecord = recordRepository.save(newRecord);

        // Make sure we invalidate the cache for the hour we just registered
        String dateAndHour = UtilityMethods.convertTimeToString(timeOfRecord);
        log.debug("Evicting cache: deviceAtCertainTime" + device.getDeviceName() + "@" + dateAndHour);

        return newRecord;
    }


    /**
     * This method is responsible to handle a request with multiple records
     * @param listOfRequest An array of the records passed by the request
     * @return A list containing the processing result of all the threads
     */
    public List<Record> recordMultiple(RecordRequest[] listOfRequest){
        // Prepare executor
        log.info("Multiple records: " + listOfRequest.length + " records to process");
        ExecutorService executor = Executors.newFixedThreadPool(20);

        // Start thread for each record
        List<Future<Record>> futures = new ArrayList<>();
        for (RecordRequest request : listOfRequest) {
            Device device = deviceService.prepareDeviceForRecord(request);
            log.debug("Submiting request " + request.getDeviceName() + request.getTime() + " to executor");
            Future<Record> future = executor.submit(() -> recordTemperature(device, request.getTime(), request.getTemperature()));
            futures.add(future);
        }

        // Retrieve results from threads
        List<Record> processedRecord = new ArrayList<>();
        List<String> unprocessedRecords = new ArrayList<>();
        for (Future<Record> future : futures) {
            try {
                Record record = future.get();
                log.debug("Record " + record.getDeviceName() + record.getTimeOfRecording() + " processed");
                processedRecord.add(record);
            } catch (Exception e) {
                unprocessedRecords.add(e.getMessage());
                log.warn(e.getMessage());
            }
        }
        executor.shutdown();

        // All records failed
        if(processedRecord.isEmpty()){
            log.warn("All records failed to be saved");
            throw new BulkProcessingFailureException("No records saved", unprocessedRecords);
        }
        // Some unprocessed entries
        if(!unprocessedRecords.isEmpty()){
            log.warn("Not all records were successfully processed");
            PartialSuccessMessage<Record> response = new PartialSuccessMessage<>(HttpStatus.MULTI_STATUS.value(),new Timestamp(System.currentTimeMillis()), processedRecord, unprocessedRecords);
            throw new IncompleteProcessException("Partial success when processing entries", response);
        }

        return processedRecord;
    }
}
