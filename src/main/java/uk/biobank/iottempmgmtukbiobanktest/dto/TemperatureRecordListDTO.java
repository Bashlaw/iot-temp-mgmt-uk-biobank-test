package uk.biobank.iottempmgmtukbiobanktest.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.biobank.iottempmgmtukbiobanktest.utils.dto.PageableResponseDTO;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class TemperatureRecordListDTO extends PageableResponseDTO {

    private List<TemperatureRecordDTO> temperatureRecords;

}
