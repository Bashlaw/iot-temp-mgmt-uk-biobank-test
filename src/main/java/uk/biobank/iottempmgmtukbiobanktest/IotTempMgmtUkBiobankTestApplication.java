package uk.biobank.iottempmgmtukbiobanktest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Main application class for the IoT Temperature Management UK Biobank Test Application.
 * This class serves as the entry point to the Spring Boot application.
 * It also enables caching support within the application context.
 * <p>
 * Annotations:
 * - @SpringBootApplication: Marks this class as the primary configuration class and entry point for the application.
 * - @EnableCaching: Enables caching functionality for performance optimization.
 */
@EnableCaching
@SpringBootApplication
public class IotTempMgmtUkBiobankTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(IotTempMgmtUkBiobankTestApplication.class , args);
    }

}
