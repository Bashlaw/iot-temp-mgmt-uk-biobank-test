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

/**
 * Represents a record of a temperature measurement obtained from a device.
 * This entity is mapped to the "temperature_records" table in the database.
 * <p>
 * The class includes details such as the device name, location, recorded temperature,
 * the time of measurement, and metadata like the creation timestamp.
 * <p>
 * The entity also ensures proper indexing for efficient querying based on device name and location.
 * <p>
 * Attributes:
 * - id: Unique identifier for the record, auto-generated.
 * - deviceName: Name of the device that recorded the temperature.
 * - location: Location where the temperature was recorded.
 * - temperature: Measured temperature value.
 * - time: The time when the temperature was recorded.
 * - createdAt: The timestamp when the record was created.
 * <p>
 * The class provides a utility method to transform a TemperatureRecord entity
 * into a corresponding data transfer object (TemperatureRecordDTO).
 */
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
