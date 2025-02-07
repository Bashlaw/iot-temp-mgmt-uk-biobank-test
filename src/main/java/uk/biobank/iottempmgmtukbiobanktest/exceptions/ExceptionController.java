package uk.biobank.iottempmgmtukbiobanktest.exceptions;

import jakarta.validation.UnexpectedTypeException;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import uk.biobank.iottempmgmtukbiobanktest.utils.dto.Response;
import uk.biobank.iottempmgmtukbiobanktest.utils.enums.ResponseCodeAndMessage;
import uk.biobank.iottempmgmtukbiobanktest.utils.service.GeneralService;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * ExceptionController is responsible for handling exceptions globally across the
 * application.
 * It provides custom logic to generate appropriate responses for various
 * exceptions encountered during request processing.
 * <p>
 * This class leverages Spring's exception handling mechanisms using
 * {@link ControllerAdvice} and {@link ExceptionHandler} annotations, and centralizes
 * exception handling to enhance consistency and maintainability of error responses.
 * <p>
 * It uses the {@link GeneralService} to prepare response objects for failure scenarios
 * with standardized messages and response codes.
 */
@Slf4j
@ControllerAdvice
public class ExceptionController {

    private final GeneralService generalService;

    public ExceptionController(GeneralService generalService) {
        this.generalService = generalService;
    }

    /**
     * Retrieves the first error message from a map of error entries.
     *
     * @param errors a map where keys represent error fields and values represent corresponding error messages
     * @return the first error message in the map, or null if the map is empty or the first value is not valid
     */
    private static String getFirstErrorMessage(Map<String, String> errors) {
        String firstErrorMessage;
        if (errors.isEmpty()) {
            firstErrorMessage = null;
        } else {
            if (!errors.containsValue(errors.values().iterator().next())) firstErrorMessage = null;
            else firstErrorMessage = errors.values().iterator().next();
        }
        return firstErrorMessage;
    }

    /**
     * Handles exceptions and returns a standardized error response.
     * This method is specifically configured to handle {@link GeneralException}
     * and other exceptions through conditional logic.
     * It prepares a detailed error response using the {@link GeneralService}.
     *
     * @param ex the exception thrown during application execution
     * @return a {@link ResponseEntity} containing a {@link Response} object with appropriate
     * response code, response message, and HTTP status code
     */
    @ExceptionHandler({GeneralException.class})
    public final ResponseEntity<Response> handleException(Exception ex) {
        log.info("Error occurred, error message is {}" , ex.getMessage());

        if (ex instanceof GeneralException) {
            Response response = generalService.prepareFailedResponse(Integer.parseInt(ex.getMessage()) , ex.getCause().getMessage());
            log.info("Response code is {}, response message is {}" , response.getResponseCode() , response.getResponseMessage());
            return new ResponseEntity<>(response , HttpStatusCode.valueOf(response.getResponseCode()));
        } else {
            Response response = generalService.prepareFailedResponse(ResponseCodeAndMessage.ERROR_PROCESSING.responseCode , ex.getMessage());
            return new ResponseEntity<>(response , HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Handles exceptions triggered by validation errors in method arguments.
     * This method captures validation errors wrapped in a {@link MethodArgumentNotValidException},
     * extracts the field-specific error details, and prepares a standardized error response.
     * The response is generated with a specific response code, message, and relevant HTTP status.
     *
     * @param ex the exception object containing validation error details
     * @return a {@link ResponseEntity} containing a {@link Response} object with
     * standardized error details and a BAD_REQUEST HTTP status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName , errorMessage);
        });
        String firstErrorMessage = getFirstErrorMessage(errors);
        Response response = generalService.prepareFailedResponse(ResponseCodeAndMessage.BAD_REQUEST.responseCode , "Validation failed for request.");
        log.error("Validation error: {}" , firstErrorMessage , ex);
        return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles exceptions caused by unreadable HTTP messages.
     * This method is specifically designed to handle {@link HttpMessageNotReadableException},
     * which is thrown when the request body is invalid or cannot be parsed.
     * It generates a standardized error response indicating the bad request,
     * along with an appropriate error message for the client.
     *
     * @param ex the exception object representing the HTTP message not readable error
     * @return a {@link ResponseEntity} containing a {@link Response} object with a
     * response code, a descriptive error message, and a BAD_REQUEST HTTP status
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String errorMessage = "Invalid request body. Please check your request data.";
        Response response = generalService.prepareFailedResponse(ResponseCodeAndMessage.BAD_REQUEST.responseCode , errorMessage);
        log.error("HttpMessageNotReadable error: {}" , ex.getMessage() , ex);
        return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles exceptions triggered by PostgreSQL database errors.
     * This method captures {@link PSQLException}, generates a standardized error response
     * indicating a database conflict, and logs the error for debugging purposes.
     *
     * @param ex the {@link PSQLException} instance representing the database error
     * @return a {@link ResponseEntity} containing a {@link Response} object with a conflict
     * response code, standardized error message, and an HTTP status of CONFLICT
     */
    @ExceptionHandler(PSQLException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Response> handlePSQLException(PSQLException ex) {
        String errorMessage = "Database error occurred.";
        Response response = generalService.prepareFailedResponse(ResponseCodeAndMessage.ALREADY_EXIST.responseCode , errorMessage);
        log.error("PSQL error: {}" , ex.getMessage() , ex);
        return new ResponseEntity<>(response , HttpStatus.CONFLICT);
    }

    /**
     * Handles {@link IllegalArgumentException} thrown within the application.
     * Converts the exception into a standardized failed response and logs the error details.
     *
     * @param ex the instance of {@code IllegalArgumentException} that was thrown
     * @return a {@code ResponseEntity} containing a custom {@code Response} object with an error message
     *         and an HTTP status of {@code BAD_REQUEST}
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response> handleIllegalArgumentException(IllegalArgumentException ex) {
        String errorMessage = "Illegal argument provided.";
        Response response = generalService.prepareFailedResponse(ResponseCodeAndMessage.BAD_REQUEST.responseCode , errorMessage);
        log.error("IllegalArgumentException: {}" , ex.getMessage() , ex);
        return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles `ParseException` exceptions raised during request processing.
     * Constructs a failed response with an appropriate error message and HTTP status code.
     * Log the details of the exception for debugging purposes.
     *
     * @param ex the `ParseException` that was caught during execution
     * @return a `ResponseEntity` containing the error response and an HTTP status of BAD_REQUEST
     */
    @ExceptionHandler(ParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response> handleParseException(ParseException ex) {
        String errorMessage = "Failed to parse the data.";
        Response response = generalService.prepareFailedResponse(ResponseCodeAndMessage.BAD_REQUEST.responseCode , errorMessage);
        log.error("ParseException: {}" , ex.getMessage() , ex);
        return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link NullPointerException} by returning a standardized error response.
     *
     * @param ex the {@code NullPointerException} instance caught during program execution
     * @return a {@link ResponseEntity} containing the error response with HTTP status 400 (Bad Request)
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response> handleNullException(NullPointerException ex) {
        String errorMessage = "An unexpected error occurred.";
        Response response = generalService.prepareFailedResponse(ResponseCodeAndMessage.BAD_REQUEST.responseCode , errorMessage);
        log.error("NullPointerException: {}" , ex.getMessage() , ex);
        return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles exceptions of type {@code UnexpectedTypeException} and returns a standardized error response.
     * <p>
     * The method intercepts {@code UnexpectedTypeException} that occur in the application
     * and prepares a {@code ResponseEntity} containing an error message and response code.
     * It also logs the exception details for debugging purposes.
     *
     * @param ex the {@code UnexpectedTypeException} to be handled
     * @return a {@code ResponseEntity<Response>} containing the error code and description
     */
    @ExceptionHandler(UnexpectedTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response> handleUnexpectedTypeException(UnexpectedTypeException ex) {
        String errorMessage = "An unexpected error occurred.";
        Response response = generalService.prepareFailedResponse(ResponseCodeAndMessage.BAD_REQUEST.responseCode , errorMessage);
        log.error("UnexpectedTypeException: {}" , ex.getMessage() , ex);
        return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
    }

}
