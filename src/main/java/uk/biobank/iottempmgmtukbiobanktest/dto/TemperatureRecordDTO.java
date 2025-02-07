package uk.biobank.iottempmgmtukbiobanktest.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing a temperature record collected from a device.
 * This class encapsulates information regarding a specific temperature measurement,
 * including the device name, the location where the measurement was taken, the temperature value,
 * and both the reported time and the actual timestamp of the record.
 * <p>
 * It is primarily used to exchange temperature data between layers of the application,
 * such as the service layer and the controller layer, and to represent individual temperature records
 * in various operations like ingestion, retrieval, and deletion.
 */
@Data
public class TemperatureRecordDTO {

    private String deviceName;

    private String location;

    private Double temperature;

    private String time;

    private LocalDateTime actualTime;

}
