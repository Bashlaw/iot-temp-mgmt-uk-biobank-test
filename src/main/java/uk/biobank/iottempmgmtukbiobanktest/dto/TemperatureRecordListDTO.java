package uk.biobank.iottempmgmtukbiobanktest.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.biobank.iottempmgmtukbiobanktest.utils.dto.PageableResponseDTO;

import java.util.List;

/**
 * Data Transfer Object (DTO) for representing a paginated list of temperature records.
 * This class extends {@link PageableResponseDTO} to include pagination metadata and
 * contains a collection of {@link TemperatureRecordDTO} representing individual
 * temperature records.
 * <p>
 * It is primarily used to encapsulate a response structure that comprises a list of temperature
 * records alongside metadata such as total record count, current page, and page size. This
 * structure is used in API responses to facilitate client handling of paginated data
 * related to temperature measurements.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TemperatureRecordListDTO extends PageableResponseDTO {

    private List<TemperatureRecordDTO> temperatureRecords;

}
