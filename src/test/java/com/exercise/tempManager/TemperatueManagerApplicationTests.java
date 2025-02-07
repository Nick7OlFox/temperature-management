package com.exercise.tempManager;

import com.exercise.tempManager.common.Constants;
import com.exercise.tempManager.common.UtilityMethods;
import com.exercise.tempManager.dto.Device;
import com.exercise.tempManager.dto.Record;
import com.exercise.tempManager.exceptions.DeviceNotFoundException;
import com.exercise.tempManager.repository.DeviceRepository;
import com.exercise.tempManager.service.DeviceService;
import com.exercise.tempManager.service.RecordService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.sql.Timestamp;
import java.util.Calendar;

@SpringBootTest
class TemperatueManagerApplicationTests {

	@Autowired
	CacheManager cacheManager;

	@Autowired
	DeviceService deviceService;

	@Autowired
	RecordService recordService;

	@Autowired
	DeviceRepository deviceRepository;

	// Testing constants
	private final String DEVICE_NAME = "Test Device";
	private final String DEVICE_LOCATION = "Test Location";
	private final Device TEST_DEVICE = new Device(DEVICE_NAME, DEVICE_LOCATION);

	@Test
	void createDevice(){
		// Create a test device
		String testIdentifier = String.valueOf(Math.random());
		Device testDevice = deviceService.registerDevice(DEVICE_NAME + testIdentifier, DEVICE_LOCATION);
		deviceService.registerDevice(testDevice.getDeviceName(), testDevice.getLocation());
		Assertions.assertNotNull(deviceService.findDevice(testDevice.getDeviceName()), "The device was not found. This can mean it was not created");
	}

	@Test
	void findDevice(){
		// Create a test device
		String testIdentifier = String.valueOf(Math.random());
		Device testDevice = deviceService.registerDevice(DEVICE_NAME + testIdentifier, DEVICE_LOCATION);
		// Archive device

		Assertions.assertNotNull(deviceService.findDevice(testDevice.getDeviceName()), "The device was not found");
		Assertions.assertThrows(DeviceNotFoundException.class, () -> {
			deviceService.findDevice("INVALID CODE");
		}, "Unknown device not being flagged");
	}

	@Test
	void createRecord(){
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		Assertions.assertNotNull(recordService.recordTemperature(TEST_DEVICE, currentTime, 15F), "Record not created");
	}

	@Test
	void calculateAverageDeviceTemperature(){
		Cache cache = cacheManager.getCache(Constants.DEVICE_AT_CERTAIN_TIME);

		String testIdentifier = String.valueOf(Math.random());
		Device testDevice = deviceService.registerDevice(DEVICE_NAME + testIdentifier, DEVICE_LOCATION);

		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		String cacheName = testDevice.getDeviceName() + "*" + UtilityMethods.convertTimeToString(currentTime);

		// Create 3 records
		Record r1 = recordService.recordTemperature(testDevice, currentTime, 10f);

		// Record 2 1:30h behind
		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(currentTime.getTime());
		cal1.add(Calendar.MINUTE, +30);
		Timestamp timestamp1 = new Timestamp(cal1.getTimeInMillis());
		Record r2 = recordService.recordTemperature(testDevice, new Timestamp(cal1.getTimeInMillis()), 20f);

		// Record 2 45min ahead
		Calendar cal2 = Calendar.getInstance();
		cal2.setTimeInMillis(currentTime.getTime());
		cal2.add(Calendar.MINUTE, +45);
		Record r3 = recordService.recordTemperature(testDevice, new Timestamp(cal2.getTimeInMillis()), 15f);

		// Ensure cache is empty
		Assertions.assertNull(cache.get(cacheName), "Caching for this device was not non existent");

		Assertions.assertEquals(15f, deviceService.deviceHourlyAverage(testDevice.getDeviceName(), currentTime), "Average temperature does not match the expected result");

		// Ensure caching
		Assertions.assertNotNull(cache.get(cacheName));
	}
}
