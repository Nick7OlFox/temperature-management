package com.exercise.tempManager.repository;

import com.exercise.tempManager.dto.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

import java.sql.Timestamp;
import java.util.ArrayList;

public interface DeviceRepository extends JpaRepository<Device, String> {

    @Procedure("find_device_records")
    ArrayList<Float> getRecordsForTime(String device_name, Timestamp date_and_time);
}
