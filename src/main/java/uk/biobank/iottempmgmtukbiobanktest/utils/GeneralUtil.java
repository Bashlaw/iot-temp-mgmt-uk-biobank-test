package uk.biobank.iottempmgmtukbiobanktest.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Utility class for general helper methods that facilitate common operations,
 * such as checking for null or empty strings.
 */
@Slf4j
public class GeneralUtil {

    /**
     * Checks if the provided string is either null or empty.
     *
     * @param value the string to be checked for null or emptiness
     * @return true if the string is null or empty, false otherwise
     */
    public static boolean stringIsNullOrEmpty(String value) {
        return Objects.isNull(value) || value.isEmpty();
    }

}
