package uk.biobank.iottempmgmtukbiobanktest.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.biobank.iottempmgmtukbiobanktest.entity.TemperatureRecord;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for managing TemperatureRecord entities.
 * <p>
 * Extends JpaRepository to provide basic CRUD operations and custom queries
 * for interacting with the temperature records data.
 */
public interface TemperatureRecordRepository extends JpaRepository<TemperatureRecord, Long> {

    boolean existsByDeviceNameAndTime(String deviceName , LocalDateTime time);

    List<TemperatureRecord> findByDeviceNameAndTimeBetween(String deviceName , LocalDateTime start , LocalDateTime end);

    Page<TemperatureRecord> findByDeviceName(String deviceName, Pageable pageable);

}
