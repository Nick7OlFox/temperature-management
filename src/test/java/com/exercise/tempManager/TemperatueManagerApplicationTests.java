package com.exercise.tempManager;

import com.exercise.tempManager.dto.Device;
import com.exercise.tempManager.dto.Record;
import com.exercise.tempManager.service.DeviceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.sql.Timestamp;
import java.util.ArrayList;

@SpringBootTest
class TemperatueManagerApplicationTests {

	@Autowired
	CacheManager cacheManager;

	@Autowired
	DeviceService deviceService;

	@Test
	void calculateAverageDeviceTemperature(){
		Cache cache = cacheManager.getCache("deviceAtCertainTime");

		Device device = new Device("Test Device", "Test Location");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		String cacheName = device.getDeviceName() + "@" + timestamp.toString();

		// Ensure cache is empty
		Assertions.assertNull(cache.get(cacheName));

		// Invalid Device name
//		Assertions.assertThrows(IllegalAccessException.class, () -> {
//			deviceService.deviceHourlyAverage("THIS DEVICE DOES NOT EXIST", new Timestamp(System.currentTimeMillis()));
//		});

		Record rec1 = new Record(device.getDeviceName(), timestamp, 12.5f);
		Record rec2 = new Record(device.getDeviceName(), timestamp,  15f);
		Record rec3 = new Record(device.getDeviceName(), timestamp,  10f);

		ArrayList<Record> listOfRecordsForDevice = new ArrayList<Record>();
		listOfRecordsForDevice.add(rec1);
		listOfRecordsForDevice.add(rec2);
		listOfRecordsForDevice.add(rec3);

		Assertions.assertEquals(12.5f, deviceService.deviceHourlyAverage(device.getDeviceName(), timestamp, listOfRecordsForDevice));
		Assertions.assertNotNull(cache.get(cacheName));
	}
}
