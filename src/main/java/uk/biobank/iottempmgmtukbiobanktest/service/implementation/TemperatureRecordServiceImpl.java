package uk.biobank.iottempmgmtukbiobanktest.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.biobank.iottempmgmtukbiobanktest.dto.TemperatureRecordDTO;
import uk.biobank.iottempmgmtukbiobanktest.dto.TemperatureRecordListDTO;
import uk.biobank.iottempmgmtukbiobanktest.entity.TemperatureRecord;
import uk.biobank.iottempmgmtukbiobanktest.repository.TemperatureRecordRepository;
import uk.biobank.iottempmgmtukbiobanktest.service.TemperatureRecordService;
import uk.biobank.iottempmgmtukbiobanktest.utils.DateUtil;
import uk.biobank.iottempmgmtukbiobanktest.utils.dto.PageableRequestDTO;
import uk.biobank.iottempmgmtukbiobanktest.utils.service.GeneralService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Implementation of the TemperatureRecordService interface providing functionality
 * for managing and processing temperature records.
 */
@Slf4j
@Service
public class TemperatureRecordServiceImpl implements TemperatureRecordService {

    private final GeneralService generalService;
    private final TemperatureRecordRepository temperatureRecordRepository;
    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    public TemperatureRecordServiceImpl(GeneralService generalService , TemperatureRecordRepository temperatureRecordRepository) {
        this.generalService = generalService;
        this.temperatureRecordRepository = temperatureRecordRepository;
    }

    /**
     * Processes a list of temperature records,
     * saving valid records to the repository, making sure to handle duplicates,
     * and collecting records with missing temperature readings.
     *
     * @param records a list of TemperatureRecordDTO objects representing temperature readings to be processed
     * @return a map containing device names as keys and a description of the missing readings as values
     */
    @Override
    public Map<String, String> processRecords(List<TemperatureRecordDTO> records) {
        Map<String, String> missingReadings = new ConcurrentHashMap<>();

        records.parallelStream().forEach(record -> {
            if (record.getTemperature() == null) {
                String message = "Missing reading at " + record.getTime();
                missingReadings.put(record.getDeviceName(), message);
            } else {
                executor.execute(() -> {
                    // Check if a record already exists for this device and time
                    boolean exists = temperatureRecordRepository
                            .existsByDeviceNameAndTime(record.getDeviceName(), LocalDateTime.parse(record.getTime()));

                    if (!exists) {
                        temperatureRecordRepository.save(convertToTemperatureRecord(record));
                    } else {
                        log.warn("Duplicate record skipped for device: {} at time: {}", record.getDeviceName(), record.getTime());
                    }
                });
            }
        });

        return missingReadings;
    }

    /**
     * Calculates the average temperature for a specific device within a given hour of a specified date.
     *
     * @param deviceName the name of the device for which the temperature records are required
     * @param date       the date in the format "yyyy-MM-dd" for which the temperature is to be calculated
     * @param hour       the specific hour (in 24-hour format) for which the temperature is to be calculated
     * @return the average temperature as a Double for the given parameters; returns Double.NaN if no records are found
     */
    @Override
    public Double calculateAverageTemperature(String deviceName , String date , int hour) {

        LocalDateTime startDateTime = LocalDate.parse(date).atTime(hour , 0); // Combines date and hour
        LocalDateTime endDateTime = startDateTime.plusHours(1); // Adds one hour


        log.info("Start => {} and end => {}" , startDateTime , endDateTime);

        List<TemperatureRecord> records = temperatureRecordRepository.findByDeviceNameAndTimeBetween(deviceName , startDateTime , endDateTime);

        return records.stream().mapToDouble(TemperatureRecord::getTemperature).average().orElse(Double.NaN);
    }

    /**
     * Retrieves all temperature records based on the given pageable request.
     *
     * @param dto the pageable request containing pagination and sorting information
     * @return a DTO containing a list of temperature records along with pagination details
     */
    @Override
    public TemperatureRecordListDTO getAllRecords(PageableRequestDTO dto) {

        Pageable pageable = generalService.getPageableObject(dto);

        Page<TemperatureRecord> temperatureRecordPage = temperatureRecordRepository.findAll(pageable);

        return getTemperatureRecordListDTO(temperatureRecordPage);
    }

    /**
     * Retrieves all temperature records associated with a specific device name.
     *
     * @param deviceName the name of the device for which temperature records are to be retrieved
     * @param dto        the pageable request object containing pagination and sorting information
     * @return a DTO containing a list of temperature records and pagination details
     */
    @Override
    public TemperatureRecordListDTO getAllRecordsByDeviceName(String deviceName , PageableRequestDTO dto) {

        Pageable pageable = generalService.getPageableObject(dto);

        Page<TemperatureRecord> temperatureRecordPage = temperatureRecordRepository.findByDeviceName(deviceName , pageable);

        return getTemperatureRecordListDTO(temperatureRecordPage);
    }

    /**
     * Deletes all records from the temperature record repository.
     * <p>
     * This method removes all entries from the underlying database or data store
     * managed by the temperatureRecordRepository. Use this method cautiously as
     * it will result in the loss of all temperature record data.
     */
    @Override
    public void deleteAllRecords() {
        temperatureRecordRepository.deleteAll();
    }

    /**
     * Converts a TemperatureRecordDTO object to a TemperatureRecord object.
     *
     * @param record the TemperatureRecordDTO object containing the data to be converted
     * @return a TemperatureRecord object populated with data from the provided DTO
     */
    private TemperatureRecord convertToTemperatureRecord(TemperatureRecordDTO record) {
        TemperatureRecord temperatureRecord = new TemperatureRecord();
        temperatureRecord.setDeviceName(record.getDeviceName());
        temperatureRecord.setLocation(record.getLocation());
        temperatureRecord.setTemperature(record.getTemperature());

        if (record.getTime() != null) {
            temperatureRecord.setTime(DateUtil.stringToLocalDateTime(record.getTime()));
        }

        return temperatureRecord;
    }

    /**
     * Converts a paginated list of TemperatureRecord entities into a TemperatureRecordListDTO object.
     *
     * @param temperatureRecords a paginated list of TemperatureRecord entities to be converted
     * @return a TemperatureRecordListDTO object containing the converted temperature record data
     */
    private TemperatureRecordListDTO getTemperatureRecordListDTO(Page<TemperatureRecord> temperatureRecords) {
        log.info("converting temperature records page to temperature record list DTO");

        TemperatureRecordListDTO temperatureRecordListDTO = new TemperatureRecordListDTO();

        List<TemperatureRecord> records = temperatureRecords.getContent();
        if (!records.isEmpty()) {
            temperatureRecordListDTO.setHasNextRecord(temperatureRecords.hasNext());
            temperatureRecordListDTO.setTotalCount((int) temperatureRecords.getTotalElements());
            temperatureRecordListDTO.setSize(records.size());
            temperatureRecordListDTO.setPage(temperatureRecords.getNumber() + 1);
        }

        List<TemperatureRecordDTO> recordDTOs = records.stream().map(TemperatureRecord::getTemperatureRecordDTO).toList();
        temperatureRecordListDTO.setTemperatureRecords(recordDTOs);

        return temperatureRecordListDTO;

    }

}
