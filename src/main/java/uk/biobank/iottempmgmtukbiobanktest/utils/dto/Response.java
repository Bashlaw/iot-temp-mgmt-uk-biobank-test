package uk.biobank.iottempmgmtukbiobanktest.utils.dto;

import lombok.Data;

/**
 * Represents a standard response structure used in API responses.
 * This class is designed to encapsulate the essential parts of
 * a response including a response code, a human-readable message, and
 * optional additional data.
 * <p>
 * Fields:
 * - `responseCode`: An integer representing the status of the response.
 *   Typically used to indicate success, failure, or specific error codes.
 * - `responseMessage`: A string message providing context or details about
 *   the response, such as success confirmation or error descriptions.
 * - `Data`: An object that optionally carries additional information or
 *   the result of the operation, if applicable.
 */
@Data
public class Response {

    private int responseCode;

    private String responseMessage;

    private Object data;

}
