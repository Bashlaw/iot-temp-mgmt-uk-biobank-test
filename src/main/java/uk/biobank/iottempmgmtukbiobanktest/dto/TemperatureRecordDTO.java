package uk.biobank.iottempmgmtukbiobanktest.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TemperatureRecordDTO {

    private String deviceName;

    private String location;

    private Double temperature;

    private String time;

    private LocalDateTime actualTime;

}
