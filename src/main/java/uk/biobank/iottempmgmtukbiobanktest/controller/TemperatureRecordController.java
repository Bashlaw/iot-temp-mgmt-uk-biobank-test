package uk.biobank.iottempmgmtukbiobanktest.controller;

import jakarta.validation.Valid;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import uk.biobank.iottempmgmtukbiobanktest.dto.TemperatureRecordDTO;
import uk.biobank.iottempmgmtukbiobanktest.service.TemperatureRecordService;
import uk.biobank.iottempmgmtukbiobanktest.utils.dto.PageableRequestDTO;
import uk.biobank.iottempmgmtukbiobanktest.utils.dto.Response;
import uk.biobank.iottempmgmtukbiobanktest.utils.enums.ResponseCodeAndMessage;
import uk.biobank.iottempmgmtukbiobanktest.utils.service.GeneralService;

import java.util.List;

/**
 * Controller responsible for handling temperature records related operations
 * such as ingestion, retrieval, and deletion.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/v1/temperatureRecord")
public class TemperatureRecordController {

    private final GeneralService generalService;
    private final TemperatureRecordService temperatureRecordService;

    public TemperatureRecordController(GeneralService generalService , TemperatureRecordService temperatureRecordService) {
        this.generalService = generalService;
        this.temperatureRecordService = temperatureRecordService;
    }

    @PostMapping("/processRecords")
    public Response ingestTemperatures(@RequestBody List<TemperatureRecordDTO> records) {
        return generalService.prepareSuccessResponse(temperatureRecordService.processRecords(records));
    }

    @GetMapping("/average-temperature")
    @Cacheable(value = "averageTemperature", key = "#deviceName + #date + #hour")
    public Response getAverageTemperature(@RequestParam String deviceName , @RequestParam String date , @RequestParam int hour) {
        return generalService.prepareSuccessResponse(temperatureRecordService.calculateAverageTemperature(deviceName , date , hour));
    }

    @GetMapping("/all")
    public Response getAllTemperatures(@Valid PageableRequestDTO dto) {
        return generalService.prepareSuccessResponse(temperatureRecordService.getAllRecords(dto));
    }

    @GetMapping("/deviceName")
    public Response getAllTemperatures(@RequestParam String deviceName , @Valid PageableRequestDTO dto) {
        return generalService.prepareSuccessResponse(temperatureRecordService.getAllRecordsByDeviceName(deviceName , dto));
    }

    @DeleteMapping("/all")
    public Response deleteAllTemperatures() {
        temperatureRecordService.deleteAllRecords();
        return generalService.prepareSuccessResponse(ResponseCodeAndMessage.SUCCESSFUL.responseCode + " All records deleted successfully.");
    }

}
