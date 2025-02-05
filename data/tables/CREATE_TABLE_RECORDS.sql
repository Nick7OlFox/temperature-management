CREATE TABLE device_records (
    device VARCHAR(255) NOT NULL COMMENT 'Device that made the record',
    time_of_record TIMESTAMP NOT NULL COMMENT 'Timestamp of when the record was made',
    temperature FLOAT NOT NULL COMMENT 'Temperature recorded in Celsius',
    PRIMARY KEY (device, time_of_record),
    FOREIGN KEY (device) REFERENCES devices(device_name)
) COMMENT = 'Table to record device records';
