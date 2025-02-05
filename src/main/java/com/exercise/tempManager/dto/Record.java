package com.exercise.tempManager.dto;

import com.exercise.tempManager.dto.compk.RecordId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * Class to represent a record made by a IoT device
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "device_records")
@IdClass(RecordId.class)
public class Record {

    @Id
    @Column(name = "device")
    private String deviceName;

    @Id
    @Column(name = "time_of_record")
    private Timestamp timeOfRecording;

    @Column(name = "temperature")
    private float temperature;
}
