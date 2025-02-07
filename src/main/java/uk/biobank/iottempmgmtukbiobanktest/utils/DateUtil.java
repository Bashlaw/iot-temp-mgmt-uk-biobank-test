package uk.biobank.iottempmgmtukbiobanktest.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * Utility class for working with dates and times, providing conversion methods
 * between different date/time representations such as LocalDateTime, Date, and String.
 */
@Slf4j
public class DateUtil {

    private DateUtil() {
    } // Prevent instantiation

    /**
     * Converts a {@link LocalDateTime} object into its ISO-8601 formatted string representation.
     *
     * @param localDateTime the LocalDateTime instance to be converted, must not be null
     * @return the ISO-8601 formatted string representation of the given LocalDateTime
     */
    public static String localDateTimeToString(LocalDateTime localDateTime) {

        // ISO_LOCAL_DATE_TIME handles "yyyy-MM-dd'T'HH:mm:ss"
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        return localDateTime.format(formatter);
    }

    /**
     * Converts a {@link Date} object to a {@link LocalDateTime} object.
     *
     * @param date the Date instance to be converted, must not be null
     * @return the converted LocalDateTime instance, based on the system's default time zone
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant() , ZoneId.systemDefault());
    }

    /**
     * Converts an ISO-8601 formatted date-time string into a {@link LocalDateTime} object.
     * The method expects the input string to be in the format "yyyy-MM-dd'T'HH:mm:ss".
     * If the input string is not in the correct format, the method will handle the
     * exception and return null.
     *
     * @param dateTimeStr the ISO-8601 formatted date-time string to be converted, must not be null
     * @return the corresponding {@link LocalDateTime} object, or null if the input format is invalid
     */
    public static LocalDateTime stringToLocalDateTime(String dateTimeStr) {
        try {
            // ISO_LOCAL_DATE_TIME handles "yyyy-MM-dd'T'HH:mm:ss"
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            return LocalDateTime.parse(dateTimeStr , formatter);
        } catch (DateTimeParseException e) {
            log.error("Invalid date-time format: {}" , dateTimeStr);
            return null;
        }
    }

}
