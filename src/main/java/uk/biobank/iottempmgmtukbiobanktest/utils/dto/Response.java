package uk.biobank.iottempmgmtukbiobanktest.utils.dto;

import lombok.Data;

@Data
public class Response {

    private int responseCode;

    private String responseMessage;

    private Object data;

}
