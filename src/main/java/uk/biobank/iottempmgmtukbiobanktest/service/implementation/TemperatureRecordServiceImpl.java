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

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    @Override
    public Map<String, String> processRecords(List<TemperatureRecordDTO> records) {
        Map<String, String> missingReadings = new ConcurrentHashMap<>();

        records.parallelStream().forEach(record -> {
            if (record.getTemperature() == null) {
                missingReadings.put(record.getDeviceName() , "Missing reading at " + record.getTime());
            } else {
                executor.execute(() -> temperatureRecordRepository.save(convertToTemperatureRecord(record)));
            }
        });

        return missingReadings;
    }

    @Override
    public Double calculateAverageTemperature(String deviceName , String date , int hour) {
        Calendar start = Calendar.getInstance();
        start.set(Calendar.YEAR , Integer.parseInt(date.substring(0 , 4)));
        start.set(Calendar.MONTH , Integer.parseInt(date.substring(5 , 7)) - 1);
        start.set(Calendar.DAY_OF_MONTH , Integer.parseInt(date.substring(8 , 10)));
        start.set(Calendar.HOUR_OF_DAY , hour);
        start.set(Calendar.MINUTE , 0);
        start.set(Calendar.SECOND , 0);

        Calendar end = (Calendar) start.clone();
        end.add(Calendar.HOUR_OF_DAY , 1);

        LocalDateTime startTime = DateUtil.dateToLocalDateTime(start.getTime()).withNano(0);
        LocalDateTime endTime = DateUtil.dateToLocalDateTime(end.getTime()).withNano(0);

        log.info("Start => {} and end => {}" , startTime , endTime);

        List<TemperatureRecord> records = temperatureRecordRepository.findByDeviceNameAndTimeBetween(deviceName , startTime , endTime);

        return records.stream().mapToDouble(TemperatureRecord::getTemperature).average().orElse(Double.NaN);
    }

    @Override
    public TemperatureRecordListDTO getAllRecords(PageableRequestDTO dto) {

        Pageable pageable = generalService.getPageableObject(dto);

        Page<TemperatureRecord> temperatureRecordPage = temperatureRecordRepository.findAll(pageable);

        return getTemperatureRecordListDTO(temperatureRecordPage);
    }

    @Override
    public TemperatureRecordListDTO getAllRecordsByDeviceName(String deviceName , PageableRequestDTO dto) {

        Pageable pageable = generalService.getPageableObject(dto);

        Page<TemperatureRecord> temperatureRecordPage = temperatureRecordRepository.findByDeviceName(deviceName , pageable);

        return getTemperatureRecordListDTO(temperatureRecordPage);
    }

    @Override
    public void deleteAllRecords() {
        temperatureRecordRepository.deleteAll();
    }

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
