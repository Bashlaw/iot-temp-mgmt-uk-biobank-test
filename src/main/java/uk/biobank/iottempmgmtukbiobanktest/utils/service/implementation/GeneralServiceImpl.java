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

@Slf4j
@Service
public class GeneralServiceImpl implements GeneralService {

    @Value("${max-pull-size:100}")
    private int maxPullSize;

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

    //used to format failed response body
    @Override
    public Response prepareFailedResponse(int code , String message) {
        Response response = new Response();
        response.setResponseCode(code);
        response.setResponseMessage(message);

        log.info("ResponseCode => {} and message => {}" , code , message);

        return response;
    }

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
