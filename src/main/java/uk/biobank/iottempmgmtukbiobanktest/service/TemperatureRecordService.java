package uk.biobank.iottempmgmtukbiobanktest.service;

import uk.biobank.iottempmgmtukbiobanktest.dto.TemperatureRecordDTO;
import uk.biobank.iottempmgmtukbiobanktest.dto.TemperatureRecordListDTO;
import uk.biobank.iottempmgmtukbiobanktest.utils.dto.PageableRequestDTO;

import java.util.List;
import java.util.Map;

/**
 * Service interface for managing temperature records in the application. This service provides
 * methods for processing, retrieving, and deleting temperature records. It also includes
 * functionality to calculate the average temperature based on specific conditions.
 */
public interface TemperatureRecordService {

    Map<String, String> processRecords(List<TemperatureRecordDTO> records);

    Double calculateAverageTemperature(String deviceName , String date , int hour);

    TemperatureRecordListDTO getAllRecords(PageableRequestDTO dto);

    TemperatureRecordListDTO getAllRecordsByDeviceName(String deviceName , PageableRequestDTO dto);

    void deleteAllRecords();

}
