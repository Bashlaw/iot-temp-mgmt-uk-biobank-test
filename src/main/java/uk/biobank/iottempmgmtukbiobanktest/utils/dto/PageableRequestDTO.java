package uk.biobank.iottempmgmtukbiobanktest.utils.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PageableRequestDTO {

    @NotNull(message = "Size must be provided, maximum is 100")
    private int size = 10;

    @NotNull(message = "Page must be provided, minimum is 1")
    private int page = 1;

    private String sortBy = "createdAt";

    private String sortDirection = "desc";

}
