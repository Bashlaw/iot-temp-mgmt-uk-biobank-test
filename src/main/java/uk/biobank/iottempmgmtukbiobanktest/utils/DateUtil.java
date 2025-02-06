package uk.biobank.iottempmgmtukbiobanktest.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

@Slf4j
public class DateUtil {

    private DateUtil() {
    } // Prevent instantiation

    public static String localDateTimeToString(LocalDateTime localDateTime) {

        // ISO_LOCAL_DATE_TIME handles "yyyy-MM-dd'T'HH:mm:ss"
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        return localDateTime.format(formatter);
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant() , ZoneId.systemDefault());
    }

    public static LocalDateTime stringToLocalDateTime(String dateTimeStr) {
        try {
            // ISO_LOCAL_DATE_TIME handles "yyyy-MM-dd'T'HH:mm:ss"
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            return LocalDateTime.parse(dateTimeStr , formatter);
        } catch (DateTimeParseException e) {
            log.error("Invalid date-time format: " + dateTimeStr);
            return null;
        }
    }

}
