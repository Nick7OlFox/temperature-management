package com.exercise.tempManager.dto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

/**
 * Class to represent a record made by a IoT device
 */
@Data
@Builder
@AllArgsConstructor
// TODO Implement here a database representation
public class Record {

    private Device device;
    private Timestamp timeOfRecording;
    private float temperature;
}
