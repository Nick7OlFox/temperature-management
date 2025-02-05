package com.exercise.tempManager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Class to represent an IoT Device
 */
@Data
@Builder
@AllArgsConstructor
// TODO Implement db table here
public class Device {

    private String deviceName;
    private String location;
}
