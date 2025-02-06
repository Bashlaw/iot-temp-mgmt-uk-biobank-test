package uk.biobank.iottempmgmtukbiobanktest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import uk.biobank.iottempmgmtukbiobanktest.dto.TemperatureRecordDTO;
import uk.biobank.iottempmgmtukbiobanktest.utils.DateUtil;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "temperature_records", indexes = {
        @Index(name = "idx_temperaturerecord_device_name", columnList = "deviceName") ,
        @Index(name = "idx_temperaturerecord_location", columnList = "location")
})
public class TemperatureRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_name")
    private String deviceName;

    private String location;

    private Double temperature;

    private LocalDateTime time;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    public static TemperatureRecordDTO getTemperatureRecordDTO(TemperatureRecord temperatureRecord) {
        TemperatureRecordDTO temperatureRecordDTO = new TemperatureRecordDTO();
        temperatureRecordDTO.setDeviceName(temperatureRecord.getDeviceName());
        temperatureRecordDTO.setLocation(temperatureRecord.getLocation());
        temperatureRecordDTO.setTemperature(temperatureRecord.getTemperature());
        temperatureRecordDTO.setTime(DateUtil.localDateTimeToString(temperatureRecord.getTime()));
        temperatureRecordDTO.setActualTime(temperatureRecord.getTime());
        return temperatureRecordDTO;
    }

}
