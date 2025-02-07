package uk.biobank.iottempmgmtukbiobanktest.utils.service;

import org.springframework.data.domain.Pageable;
import uk.biobank.iottempmgmtukbiobanktest.utils.dto.PageableRequestDTO;
import uk.biobank.iottempmgmtukbiobanktest.utils.dto.Response;

/**
 * GeneralService serves as an interface for general utilities and standardized response handling in a service layer.
 * It includes methods for pagination object creation, as well as response formatting for both
 * success and failure scenarios.
 */
public interface GeneralService {

    Pageable getPageableObject(PageableRequestDTO dto);

    //used to format failure response body
    Response prepareFailedResponse(int code , String message);

    //used to format success response body
    Response prepareSuccessResponse(Object data);

}
