package uk.biobank.iottempmgmtukbiobanktest.utils.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uk.biobank.iottempmgmtukbiobanktest.exceptions.GeneralException;
import uk.biobank.iottempmgmtukbiobanktest.utils.GeneralUtil;
import uk.biobank.iottempmgmtukbiobanktest.utils.dto.PageableRequestDTO;
import uk.biobank.iottempmgmtukbiobanktest.utils.dto.Response;
import uk.biobank.iottempmgmtukbiobanktest.utils.enums.ResponseCodeAndMessage;
import uk.biobank.iottempmgmtukbiobanktest.utils.service.GeneralService;

/**
 * Implementation of the GeneralService interface providing utility methods for handling
 * pagination, and preparing standard response objects for success and failure cases.
 * It leverages Spring Data Pageable and supports sorting and direction settings.
 */
@Slf4j
@Service
public class GeneralServiceImpl implements GeneralService {

    @Value("${max-pull-size:100}")
    private int maxPullSize;

    /**
     * Constructs a Pageable object based on the provided PageableRequestDTO.
     * This method validates the page, size, sortBy, and sortDirection parameters,
     * ensuring that they meet required constraints such as minimum values or default values.
     * It accommodates sorting based on specified fields and directions,
     * while enforcing a maximum pull size.
     *
     * @param dto the PageableRequestDTO containing pagination information including page, size, sortBy, and sortDirection
     * @return a Pageable object configured with the specified or default pagination and sorting parameters
     * @throws GeneralException if the page number is less than 1 or size is less than or equal to 0
     */
    @Override
    public Pageable getPageableObject(PageableRequestDTO dto) {
        log.info("Getting pageable object, initial size => {} and page {}" , dto.getSize() , dto.getSize());

        Pageable paged;

        int page = dto.getPage() - 1;
        int size = dto.getSize();
        String sortBy = dto.getSortBy();
        String sortDirection = dto.getSortDirection();

        if (page < 0) {
            throw new GeneralException(ResponseCodeAndMessage.BAD_REQUEST.responseCode , "Page minimum is 1");
        }

        if (size <= 0) {
            throw new GeneralException(ResponseCodeAndMessage.BAD_REQUEST.responseCode , "Size minimum is 1");
        }

        if (size > maxPullSize) {
            log.info("{} greater than max size of {}, defaulting to max" , size , maxPullSize);

            size = maxPullSize;
        }

        //check sort by
        if (GeneralUtil.stringIsNullOrEmpty(sortBy)) {
            sortBy = "createdAt";
        }

        //check the sort direction
        if (GeneralUtil.stringIsNullOrEmpty(sortDirection)) {
            sortDirection = "desc";
        }

        Sort sort;
        if (sortDirection.equalsIgnoreCase("asc")) {
            sort = Sort.by(Sort.Direction.ASC , sortBy);
        } else {
            sort = Sort.by(Sort.Direction.DESC , sortBy);
        }

        log.info("Page => {} , Size => {} , SortBy => {} , SortDirection => {}" , page , size , sortBy , sortDirection);
        paged = PageRequest.of(page , size , sort);

        return paged;
    }

    /**
     * Prepares a standardized failed response object with a specific response code
     * and message, and logs the response details for debugging purposes.
     *
     * @param code the response code indicating the nature of the failure
     * @param message the response message providing additional details about the failure
     * @return a Response object containing the provided failure code and message
     */
    //used to format failed response body
    @Override
    public Response prepareFailedResponse(int code , String message) {
        Response response = new Response();
        response.setResponseCode(code);
        response.setResponseMessage(message);

        log.info("ResponseCode => {} and message => {}" , code , message);

        return response;
    }

    /**
     * Prepares a standardized success response object containing the provided data.
     * It sets predefined response code and message for a successful operation
     * and logs the response details.
     *
     * @param data the data to be included in the success response
     * @return a Response object containing the success response code, message, and the provided data
     */
    @Override
    public Response prepareSuccessResponse(Object data) {
        Response response = new Response();

        response.setResponseCode(ResponseCodeAndMessage.SUCCESSFUL.responseCode);
        response.setResponseMessage(ResponseCodeAndMessage.SUCCESSFUL.responseMessage);
        response.setData(data);

        log.info("Successful ResponseCode => {}" , data);

        return response;
    }

}
