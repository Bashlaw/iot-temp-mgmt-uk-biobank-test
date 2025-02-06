package uk.biobank.iottempmgmtukbiobanktest.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class GeneralUtil {

    public static boolean stringIsNullOrEmpty(String value) {
        return Objects.isNull(value) || value.isEmpty();
    }

}
