CREATE PROCEDURE find_device_records (IN device_name VARCHAR(255),IN date_and_time TIMESTAMP)
BEGIN
   SELECT
       dr.temperature
    FROM
        device_records dr
    WHERE
        dr.device = device_name
        AND dr.time_of_record >= date_and_time
        AND dr.time_of_record <= DATE_ADD(date_and_time, INTERVAL 1 HOUR);
END;

