package uk.biobank.iottempmgmtukbiobanktest.exceptions;

public class GeneralException extends RuntimeException {

    public GeneralException(int responseCode , String responseMessage) {
        super(String.valueOf(responseCode) , new Throwable(responseMessage));
    }

}
