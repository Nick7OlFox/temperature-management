package com.exercise.tempManager.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * Class to represent an IoT Device
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "devices")
public class Device {

    @Id
    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "location")
    private String location;

    @Column(name = "end_date")
    private Timestamp endDate;
}
