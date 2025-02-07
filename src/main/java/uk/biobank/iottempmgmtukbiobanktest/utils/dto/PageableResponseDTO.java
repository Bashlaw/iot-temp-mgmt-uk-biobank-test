package uk.biobank.iottempmgmtukbiobanktest.utils.dto;

import lombok.Data;

/**
 * Represents a Data Transfer Object (DTO) for paginated responses.
 * This class encapsulates pagination metadata, including information
 * about the total number of records, the current page, the number of
 * records per page, and whether additional records are available.
 * <p>
 * It is commonly used as a base class for more specific DTOs that
 * require pagination details in their response structures.
 * <p>
 * Fields:
 * - `hasNextRecord`: Indicates whether there are more records in later pages.
 * - `totalCount`: The total number of records available for the query.
 * - `size`: The number of records returned per page.
 * - `page`: The current page number.
 */
@Data
public class PageableResponseDTO {

    private boolean hasNextRecord;

    private int totalCount;

    private int size;

    private int page;

}
