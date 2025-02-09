ALTER TABLE temperature_records
    ADD CONSTRAINT uc_temperaturerecord_device_name_time UNIQUE (device_name, time);
