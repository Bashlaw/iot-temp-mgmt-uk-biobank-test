package uk.biobank.iottempmgmtukbiobanktest.utils.enums;

import org.springframework.http.HttpStatus;

/**
 * ResponseCodeAndMessage is an enumeration that provides a standardized set of response codes
 * and corresponding response messages to be used throughout the application for consistent
 * error handling and status reporting.
 * <p>
 * Each enumeration value is associated with an HTTP status code and its respective status message.
 * These response codes and messages can be utilized to represent the outcome of operations
 * such as success, client errors (e.g., bad request, unauthorized), or server errors.
 * <p>
 * The enum is particularly useful for mapping common HTTP responses to meaningful representations
 * in the application's context, improving maintainability and readability of the code.
 * <p>
 * Enum Constants:
 * - SUCCESSFUL: Represents a successful operation (HTTP 200 OK).
 * - ERROR_PROCESSING: Represents a generic server error (HTTP 500 Internal Server Error).
 * - AUTHENTICATION_ERROR: Indicates an authentication failure (HTTP 401 Unauthorized).
 * - BAD_REQUEST: Represents a client error due to invalid input (HTTP 400 Bad Request).
 * - ALREADY_EXIST: Indicates a conflict due to resource duplication (HTTP 409 Conflict).
 * - RECORD_NOT_FOUND: Represents a resource not found condition (HTTP 404 Not Found).
 * - TIMEOUT_ERROR: Indicates a gateway timeout error (HTTP 504 Gateway Timeout).
 * - CLIENT_NOT_ALLOWED: Represents a conflict or unauthorized client action (HTTP 409 Conflict).
 * - ALREADY_DISABLED: Indicates a forbidden operation due to resource state (HTTP 403 Forbidden).
 * - CHANGE_USER_PASSWORD: Suggests a user password modification is required (HTTP 226 IM Used with mismatched name).
 * - INVALID_OPERATION: Represents a forbidden action due to operation constraints (HTTP 403 Forbidden).
 * <p>
 * Each constant in this enum contains:
 * - responseCode: The HTTP status code as an integer.
 * - responseMessage: The HTTP status message as a string.
 */
public enum ResponseCodeAndMessage {

    SUCCESSFUL(HttpStatus.OK.value() , HttpStatus.OK.name()),
    ERROR_PROCESSING(HttpStatus.INTERNAL_SERVER_ERROR.value() , HttpStatus.INTERNAL_SERVER_ERROR.name()),
    AUTHENTICATION_ERROR(HttpStatus.UNAUTHORIZED.value() , HttpStatus.UNAUTHORIZED.name()),
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value() , HttpStatus.BAD_REQUEST.name()),
    ALREADY_EXIST(HttpStatus.CONFLICT.value() , HttpStatus.CONFLICT.name()),
    RECORD_NOT_FOUND(HttpStatus.NOT_FOUND.value() , HttpStatus.NOT_FOUND.name()),
    TIMEOUT_ERROR(HttpStatus.GATEWAY_TIMEOUT.value() , HttpStatus.GATEWAY_TIMEOUT.name()),
    CLIENT_NOT_ALLOWED(HttpStatus.CONFLICT.value() , HttpStatus.CONFLICT.name()),
    ALREADY_DISABLED(HttpStatus.FORBIDDEN.value() , HttpStatus.FORBIDDEN.name()),
    CHANGE_USER_PASSWORD(HttpStatus.IM_USED.value() , HttpStatus.RESET_CONTENT.name()),
    INVALID_OPERATION(HttpStatus.FORBIDDEN.value() , HttpStatus.FORBIDDEN.name());

    public final int responseCode;
    public final String responseMessage;

    ResponseCodeAndMessage(int responseCode , String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

}
