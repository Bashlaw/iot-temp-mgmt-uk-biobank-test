package uk.biobank.iottempmgmtukbiobanktest.exceptions;

/**
 * GeneralException is a custom runtime exception used to handle and propagate
 * application-specific error codes and messages.
 * <p>
 * This exception includes a response code and a response message to provide
 * additional context about the error being reported.
 * It is designed to be used in scenarios where specific error handling and response logic are required
 * in the application.
 * <p>
 * The response code is represented as an integer, while the response message
 * is passed as a string to describe the error in detail.
 * These values can be used by the consuming logic (e.g., controllers or exception handlers) to
 * generate meaningful error responses for end-users or systems.
 */
public class GeneralException extends RuntimeException {

    public GeneralException(int responseCode , String responseMessage) {
        super(String.valueOf(responseCode) , new Throwable(responseMessage));
    }

}
