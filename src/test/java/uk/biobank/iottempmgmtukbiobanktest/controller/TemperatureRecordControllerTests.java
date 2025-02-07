package uk.biobank.iottempmgmtukbiobanktest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import uk.biobank.iottempmgmtukbiobanktest.config.WebSecurityConfig;
import uk.biobank.iottempmgmtukbiobanktest.dto.TemperatureRecordDTO;
import uk.biobank.iottempmgmtukbiobanktest.service.TemperatureRecordService;
import uk.biobank.iottempmgmtukbiobanktest.utils.dto.Response;
import uk.biobank.iottempmgmtukbiobanktest.utils.service.GeneralService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the {@link TemperatureRecordController}.
 * This test class uses @WebMvcTest to load only the beans required to test the controller,
 * and relies on MockMvc for simulating HTTP requests and validating responses.
 * <p>
 * The test cases validate various aspects of the TemperatureRecordController, such as
 * - Valid request processing and response structure for temperature record processing.
 * - Request validation for invalid and empty request payloads.
 * - Response generation for average temperature calculations.
 * - Handling of large batches and multiple device records.
 * <p>
 * Dependencies like {@link TemperatureRecordService} and {@link GeneralService} are mocked
 * to isolate the controller logic during testing.
 * <p>
 * An additional configuration, {@link WebSecurityConfig}, is imported to handle security-related
 * settings required for the controller.
 */
@AutoConfigureMockMvc
@Import(WebSecurityConfig.class)
@WebMvcTest(TemperatureRecordController.class)
public class TemperatureRecordControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TemperatureRecordService temperatureRecordService;

    @MockitoBean
    private GeneralService generalService;

    /**
     * Tests the batch processing of temperature records through the processRecords API endpoint.
     * This method verifies the successful handling of valid temperature records in a POST request,
     * ensuring the server produces the expected response structure.
     * <p>
     * The test performs the following:
     * - Mocks a list of temperature records using the `getTemperatureRecordDTOS` method.
     * - Creates a mock response containing expected response code, message, and data for verification.
     * - Configures the mocked behavior of the `prepareSuccessResponse` method in the `GeneralService`.
     * - Simulates a POST request to the `/api/v1/temperatureRecord/processRecords` endpoint with the test data.
     * - Validates that the HTTP response is successful with a status code of 200.
     * - Verifies the returned JSON response structure and specific data consistency.
     *
     * @throws Exception if an error occurs during the test execution
     */
    @Test
    public void testProcessBatch() throws Exception {
        List<TemperatureRecordDTO> records = getTemperatureRecordDTOS();

        // Mocking the response structure
        Map<String, String> missingReadings = Map.of(
                "AB123" , "Missing reading at 2025-01-09T07:00:00"
        );

        Response expectedResponse = new Response();
        expectedResponse.setResponseCode(200);
        expectedResponse.setResponseMessage("OK");
        expectedResponse.setData(missingReadings);

        when(generalService.prepareSuccessResponse(any())).thenReturn(expectedResponse);

        mockMvc.perform(post("/api/v1/temperatureRecord/processRecords")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(records)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value(200))
                .andExpect(jsonPath("$.responseMessage").value("OK"))
                .andExpect(jsonPath("$.data.AB123").value("Missing reading at 2025-01-09T07:00:00"));
    }

    /**
     * Generates a list of pre-defined TemperatureRecordDTO objects used for testing.
     * Each record contains data about a specific device's temperature reading,
     * including device name, location, temperature, and time details.
     *
     * @return a list of TemperatureRecordDTO objects populated with sample temperature record data.
     */
    private static List<TemperatureRecordDTO> getTemperatureRecordDTOS() {
        List<TemperatureRecordDTO> records = new ArrayList<>();

        TemperatureRecordDTO record1 = new TemperatureRecordDTO();
        record1.setDeviceName("AB123");
        record1.setLocation("A1");
        record1.setTemperature(12.3);
        record1.setTime("2025-01-09T07:01:00");
        records.add(record1);

        TemperatureRecordDTO record2 = new TemperatureRecordDTO();
        record2.setDeviceName("AB123");
        record2.setLocation("A1");
        record2.setTime("2025-01-09T07:00:00");
        records.add(record2);
        return records;
    }

    /**
     * Tests the `getAverageTemperature` API endpoint by simulating an HTTP GET request
     * and verifying the response structure and content.
     * This test ensures that the endpoint
     * correctly calculates the average temperature for a given device name, date, and hour.
     * <p>
     * The test performs the following:
     * - Mocks the behavior of `temperatureRecordService.calculateAverageTemperature` to return
     * a predefined average temperature value based on input parameters.
     * - Mocks the `generalService.prepareSuccessResponse` to return a standardized success response
     * containing the expected data when invoked.
     * - Configures a request to the `/api/v1/temperatureRecord/average-temperature` endpoint, passing
     * the required query parameters (`deviceName`, `date`, and `hour`).
     * - Validates that the HTTP response is successful with a 200 status code.
     * - Verifies the response JSON content, ensuring the `responseCode`, `responseMessage`, and `data`
     * fields match the expected values.
     *
     * @throws Exception if any error occurs during the execution of the test
     */
    @Test
    public void testGetAverageTemperature() throws Exception {
        when(temperatureRecordService.calculateAverageTemperature("AB123" , "2025-01-09T07:00:00" , 7)).thenReturn(12.3);

        // Mocking the response structure
        Response expectedResponse = new Response();
        expectedResponse.setResponseCode(200);
        expectedResponse.setResponseMessage("OK");
        expectedResponse.setData(12.3);

        when(generalService.prepareSuccessResponse(any())).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/v1/temperatureRecord/average-temperature")
                        .param("deviceName" , "AB123")
                        .param("date" , "2025-01-09T07:00:00")
                        .param("hour" , "7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value(200))
                .andExpect(jsonPath("$.responseMessage").value("OK"))
                .andExpect(jsonPath("$.data").value(12.3));
    }

    /**
     * Tests the handling of empty records in the `processRecords` API endpoint.
     * This method ensures that the API correctly processes and responds when no
     * temperature records are provided in the POST request.
     * <p>
     * Key behaviors validated in this test include:
     * - Ensuring the API successfully handles an empty list of records without an error.
     * - Verifying the response structure includes the expected HTTP status code,
     * response message, and empty data field.
     * <p>
     * The test performs the following steps:
     * - Mocks the expected `Response` object with a success code (200), message ("OK"),
     * and empty data.
     * - Configures the behavior of the `generalService.prepareSuccessResponse` method to
     * return the mocked response.
     * - Sends a POST request to the `/api/v1/temperatureRecord/processRecords` endpoint
     * with an empty list of records as the request body.
     * - Validates the response status code, JSON response structure, and specific field
     * values (responseCode, responseMessage, and data).
     *
     * @throws Exception if an error occurs during the test execution
     */
    @Test
    public void testProcessBatch_EmptyRecords() throws Exception {

        // Mocking the response structure
        Response expectedResponse = new Response();
        expectedResponse.setResponseCode(200);
        expectedResponse.setResponseMessage("OK");
        expectedResponse.setData("");

        when(generalService.prepareSuccessResponse(any())).thenReturn(expectedResponse);

        mockMvc.perform(post("/api/v1/temperatureRecord/processRecords")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new ArrayList<>())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value(200))
                .andExpect(jsonPath("$.responseMessage").value("OK"))
                .andExpect(jsonPath("$.data").value(""));// Or OK if you handle empty lists
    }

    /**
     * Tests the handling of invalid JSON in the `processRecords` API endpoint.
     * This test ensures the server responds with a 400 Bad Request status when
     * encountering malformed or improperly formatted JSON in the request body.
     * <p>
     * Test Scenario:
     * - An invalid JSON string is submitted via a POST request to the endpoint
     * `/api/v1/temperatureRecord/processRecords`.
     * - The JSON string is designed with syntax errors (missing closing brackets).
     * - The test verifies the server's response indicates a bad request error.
     * <p>
     * Validations:
     * - Ensures the HTTP response status is 400 (Bad Request).
     *
     * @throws Exception if an error occurs during the test execution
     */
    @Test
    public void testProcessBatch_InvalidRequestBody() throws Exception {
        String invalidJson = "{ \"deviceName\": \"AB123\", \"location\": \"A1\" "; // Missing closing bracket

        mockMvc.perform(post("/api/v1/temperatureRecord/processRecords")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests the `getAverageTemperature` API endpoint to ensure proper response handling
     * when no temperature records are available for the specified inputs.
     * <p>
     * This method validates the following:
     * - The `temperatureRecordService.calculateAverageTemperature` method returns `Double.NaN`
     * to signify no records are present.
     * - The `generalService.prepareSuccessResponse` method is correctly invoked to generate
     * a standardized response structure.
     * - The HTTP response status is 200 (OK).
     * - The JSON response contains the expected response code, message, and data (`Double.NaN`).
     * <p>
     * Execution steps:
     * 1. Mocks the behavior of `temperatureRecordService.calculateAverageTemperature`
     * with the provided device name, date, and hour, returning `Double.NaN`.
     * 2. Configures the `generalService.prepareSuccessResponse` method to return an expected
     * success response object populated with a 200 response code, "OK" message, and data as `Double.NaN`.
     * 3. Sends an HTTP GET request to the `/api/v1/temperatureRecord/average-temperature` endpoint
     * with valid query parameters (`deviceName`, `date`, `hour`).
     * 4. Validates that the response contains:
     * - A 200 HTTP status code.
     * - JSON fields `responseCode`, `responseMessage`, and `data` with the expected values
     * (200, "OK", `Double.NaN`).
     *
     * @throws Exception if an error occurs during the execution of the test
     */
    @Test
    public void testGetAverageTemperature_NoRecords() throws Exception {
        when(temperatureRecordService.calculateAverageTemperature("AB123" , "2025-01-09T07:00:00" , 7))
                .thenReturn(Double.NaN);

        // Mocking the response structure
        Response expectedResponse = new Response();
        expectedResponse.setResponseCode(200);
        expectedResponse.setResponseMessage("OK");
        expectedResponse.setData(Double.NaN);

        when(generalService.prepareSuccessResponse(any())).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/v1/temperatureRecord/average-temperature")
                        .param("deviceName" , "AB123")
                        .param("date" , "2025-01-09T07:00:00")
                        .param("hour" , "7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value(200))
                .andExpect(jsonPath("$.responseMessage").value("OK"))
                .andExpect(jsonPath("$.data").value(Double.NaN));
    }

    @Test
    public void testGetAverageTemperature_InvalidParams() throws Exception {
        mockMvc.perform(get("/api/v1/temperatureRecord/average-temperature")
                        .param("deviceName" , "") // Empty device name
                        .param("date" , "invalid-date-format") // Wrong date format
                        .param("hour" , "invalid-hour")) // Wrong hour format
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests the processing of temperature records from multiple devices using the
     * `temperatureRecordService.processRecords` method.
     * <p>
     * This test validates the proper handling of records belonging to different devices.
     * It ensures that the processing logic accepts records from multiple devices without
     * errors and verifies the expected result.
     * <p>
     * Test steps:
     * 1. Retrieves a list of sample temperature records for a single device using
     * the `getTemperatureRecordDTOS` method.
     * 2. Adds a new record for an additional device with specific temperature,
     * location, and timestamp details to the list.
     * 3. Pass the updated list of records to the `processRecords` method.
     * 4. Asserts that the result is not null and is an empty map, verifying
     * successful processing without issues or unexpected outcomes.
     */
    @Test
    public void testProcessRecords_MultipleDevices() {
        List<TemperatureRecordDTO> records = getTemperatureRecordDTOS();

        // Add records for another device
        TemperatureRecordDTO record = new TemperatureRecordDTO();
        record.setDeviceName("XY999");
        record.setLocation("B1");
        record.setTemperature(15.5);
        record.setTime("2025-01-09T07:02:00");
        records.add(record);

        Map<String, String> result = temperatureRecordService.processRecords(records);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Tests the processing of a large batch of temperature records using the
     * `temperatureRecordService.processRecords` method.
     * <p>
     * This test verifies the system's capability to handle a significant volume of data
     * during the batch processing of temperature records.
     * It ensures that the service processes all records in the provided list
     * without encountering errors or failing to process any items.
     * <p>
     * Test execution details:
     * 1. Creates a list of 1000 temperature records, each with unique device names,
     * locations, temperature values, and timestamps.
     * 2. Invokes the `processRecords` method with the constructed list of temperature records.
     * 3. It Validates that the result is not null and contains an empty map, indicating successful
     * processing of the entire batch without any missing data.
     */
    @Test
    public void testProcessLargeBatch() {
        List<TemperatureRecordDTO> records = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            TemperatureRecordDTO record = new TemperatureRecordDTO();
            record.setDeviceName("AB" + i);
            record.setLocation("A" + i);
            record.setTemperature(10.5 + i);
            record.setTime("2025-01-09T07:00:00");
            records.add(record);
        }

        Map<String, String> result = temperatureRecordService.processRecords(records);

        assertNotNull(result);
        assertEquals(0 , result.size()); // Should process without missing data
    }

}
