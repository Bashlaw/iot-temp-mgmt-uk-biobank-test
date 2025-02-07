package uk.biobank.iottempmgmtukbiobanktest.utils.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Represents the parameters required for a pageable request.
 * This Data Transfer Object (DTO) is specifically used for handling
 * pagination and sorting in API requests.
 * <p>
 * Fields:
 * - `size`: Specifies the number of records to retrieve in a single page.
 *   It has a default value of 10 and*/
@Data
public class PageableRequestDTO {

    @NotNull(message = "Size must be provided, maximum is 100")
    private int size = 10;

    @NotNull(message = "Page must be provided, minimum is 1")
    private int page = 1;

    private String sortBy = "createdAt";

    private String sortDirection = "desc";

}
