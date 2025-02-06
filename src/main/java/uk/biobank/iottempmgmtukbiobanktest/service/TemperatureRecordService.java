package uk.biobank.iottempmgmtukbiobanktest.service;

import uk.biobank.iottempmgmtukbiobanktest.dto.TemperatureRecordDTO;
import uk.biobank.iottempmgmtukbiobanktest.dto.TemperatureRecordListDTO;
import uk.biobank.iottempmgmtukbiobanktest.utils.dto.PageableRequestDTO;

import java.util.List;
import java.util.Map;

public interface TemperatureRecordService {

    Map<String, String> processRecords(List<TemperatureRecordDTO> records);

    Double calculateAverageTemperature(String deviceName , String date , int hour);

    TemperatureRecordListDTO getAllRecords(PageableRequestDTO dto);

    TemperatureRecordListDTO getAllRecordsByDeviceName(String deviceName , PageableRequestDTO dto);

    void deleteAllRecords();

}
