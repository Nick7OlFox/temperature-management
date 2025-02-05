CREATE TABLE devices
(
    device_name VARCHAR(255) NOT NULL UNIQUE COMMENT 'Name of the device',
    location VARCHAR(255) COMMENT 'Location of the device',
    end_date TIMESTAMP COMMENT 'Time stamp that if not null indicates that the entry is archived',
    PRIMARY KEY(device_name)
) COMMENT = 'Table to store recognised devices';
