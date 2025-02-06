package uk.biobank.iottempmgmtukbiobanktest.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.biobank.iottempmgmtukbiobanktest.entity.TemperatureRecord;

import java.time.LocalDateTime;
import java.util.List;

public interface TemperatureRecordRepository extends JpaRepository<TemperatureRecord, Long> {

    List<TemperatureRecord> findByDeviceNameAndTimeBetween(String deviceName , LocalDateTime start , LocalDateTime end);

    Page<TemperatureRecord> findByDeviceName(String deviceName, Pageable pageable);

}
