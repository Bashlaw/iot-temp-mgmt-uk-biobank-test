package uk.biobank.iottempmgmtukbiobanktest.utils.dto;

import lombok.Data;

@Data
public class PageableResponseDTO {

    private boolean hasNextRecord;

    private int totalCount;

    private int size;

    private int page;

}
