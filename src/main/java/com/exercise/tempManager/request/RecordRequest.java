package com.exercise.tempManager.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordRequest {
    private String deviceName;
    private String location;
    private Float temperature;
    private Timestamp time;
}
