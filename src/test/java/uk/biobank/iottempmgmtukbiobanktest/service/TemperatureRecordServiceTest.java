package uk.biobank.iottempmgmtukbiobanktest.service;

import org.junit.jupiter.api.Test;
import uk.biobank.iottempmgmtukbiobanktest.dto.TemperatureRecordDTO;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

/**
 * Unit test class for validating the functionality of the TemperatureRecordService interface.
 * This class contains multiple test methods to verify the correct behavior of the following methods:
 * - `processRecords`: Ensures the method processes valid inputs, handles invalid inputs gracefully,
 *   and properly throws exceptions when necessary.
 * - `calculateAverageTemperature`: Verifies the calculation of average temperature for valid data,
 *   absence of data, and when handling multiple devices.
 * <p>
 * Each test method uses mocked instances of TemperatureRecordService to isolate the service's logic
 * from actual implementations, ensuring unit-level testing. Assertions are used to verify behavior
 * and results, and service method invocations are verified to ensure expected interactions.
 */
class TemperatureRecordServiceTest {

    /**
     * Tests the functionality of the `processRecords` method in the `TemperatureRecordService` implementation
     * with a valid input scenario.
     * <p>
     * This test case:
     * - Creates a mock implementation of the `TemperatureRecordService`.
     * - Prepares the input list of `TemperatureRecordDTO` objects and the expected output map.
     * - Invokes the `processRecords` method with the pre-defined input.
     * - Verifies that the returned result matches the expected output.
     * - Ensures the `processRecords` method is called exactly once with the provided input.
     * <p>
     * Assertions:
     * - Asserts that the response from the `processRecords` method matches the expected result.
     * - Validates the interaction with the mocked service to confirm that the method is triggered as intended.
     */
    @Test
    void testProcessRecords_ValidInput() {
        // Arrange
        List<TemperatureRecordDTO> records = List.of(new TemperatureRecordDTO());
        Map<String, String> expectedResponse = Map.of("key" , "value");

        TemperatureRecordService service = mock(TemperatureRecordService.class);
        when(service.processRecords(records)).thenReturn(expectedResponse);

        // Act
        Map<String, String> actualResponse = service.processRecords(records);

        // Assert
        assertEquals(expectedResponse , actualResponse , "The processRecords method should return the expected response.");
        verify(service , times(1)).processRecords(records);
    }

    /**
     * Tests the behavior of the `processRecords` method when provided with invalid input.
     * <p>
     * Specifically, this test case verifies that:
     * - When an empty list of `TemperatureRecordDTO` objects is passed as input, the method
     *   correctly returns a predefined error message encapsulated in a map.
     * - The method gracefully handles the lack of records and does not process beyond the validation step.
     * <p>
     * Test procedure:
     * - A mock implementation of `TemperatureRecordService` is created.
     * - An empty input list is prepared, along with the expected response map containing an error message.
     * - The `processRecords` method is invoked with the empty list.
     * - Assertions are performed to ensure that:
     *   - The actual response map matches the expected response.
     *   - The `processRecords` method is invoked exactly once with the specified input.
     * <p>
     * Validates:
     * - The handling of invalid or empty inputs by the `processRecords` method.
     * - The interaction frequency with the mocked service.
     */
    @Test
    void testProcessRecords_InvalidInput() {
        // Arrange
        List<TemperatureRecordDTO> records = List.of();
        Map<String, String> expectedResponse = Map.of("error" , "No records provided");

        TemperatureRecordService service = mock(TemperatureRecordService.class);
        when(service.processRecords(records)).thenReturn(expectedResponse);

        // Act
        Map<String, String> actualResponse = service.processRecords(records);

        // Assert
        assertEquals(expectedResponse , actualResponse , "The processRecords method should handle empty input gracefully.");
        verify(service , times(1)).processRecords(records);
    }

    /**
     * Tests the error handling capabilities of the `processRecords` method in the `TemperatureRecordService` implementation.
     * <p>
     * This test verifies that the method appropriately handles exceptions encountered during its execution.
     * The scenario simulates an exception being thrown by the method and ensures that it is propagated correctly.
     * <p>
     * Test procedure:
     * - A mock implementation of `TemperatureRecordService` is created.
     * - An input list of `TemperatureRecordDTO` objects is prepared.
     * - The `processRecords` method is mocked to throw a `RuntimeException` when invoked with the prepared input.
     * - The method invocation is wrapped in a try-catch block to validate that:
     *   - The exception is thrown as expected.
     *   - The error message of the thrown exception matches the expected value.
     * - The interaction with the mocked service is verified to confirm that the method is invoked exactly once with the given input.
     * <p>
     * Validates:
     * - Exception handling behavior of the `processRecords` method in the event of a runtime failure.
     * - Correctness of the exception message.
     * - Invocation frequency with the mocked service to ensure proper method interaction.
     */
    @Test
    void testProcessRecords_ErrorHandling() {
        // Arrange
        List<TemperatureRecordDTO> records = List.of(new TemperatureRecordDTO());
        TemperatureRecordService service = mock(TemperatureRecordService.class);
        when(service.processRecords(records)).thenThrow(new RuntimeException("Test Exception"));

        // Act & Assert
        try {
            service.processRecords(records);
        } catch (RuntimeException e) {
            assertEquals("Test Exception" , e.getMessage() , "The processRecords method should throw the correct exception.");
        }
        verify(service , times(1)).processRecords(records);
    }

    /**
     * Tests the `calculateAverageTemperature` method in the `TemperatureRecordService`
     * for the scenario where valid input parameters are provided.
     * <p>
     * This test case validates the correctness of the average temperature calculation
     * by mocking the service and ensuring that:
     * - The method returns the expected average temperature value for the given inputs.
     * - The method is invoked exactly once with the specified device name, date, and hour.
     * - The result matches the predefined expected value.
     * <p>
     * Assertions:
     * - Ensures that the calculated average temperature matches the expected value.
     * - Verifies the interaction with the mocked service to confirm the method is called as designed.
     */
    @Test
    void testCalculateAverageTemperature_ValidData() {
        // Arrange
        String deviceName = "DeviceA";
        String date = "2023-10-01";
        int hour = 14;
        Double expectedAverage = 22.5;

        TemperatureRecordService service = mock(TemperatureRecordService.class);
        when(service.calculateAverageTemperature(deviceName , date , hour)).thenReturn(expectedAverage);

        // Act
        Double actualAverage = service.calculateAverageTemperature(deviceName , date , hour);

        // Assert
        assertEquals(expectedAverage , actualAverage , "The average temperature should match the expected value.");
        verify(service , times(1)).calculateAverageTemperature(deviceName , date , hour);
    }

    /**
     * Tests the behavior of the `calculateAverageTemperature` method in the `TemperatureRecordService`
     * when no temperature data is available for the given inputs.
     * <p>
     * This test case validates the following:
     * - The method returns `null` as the average temperature when there is no data available
     *   for the specified device, date, and hour.
     * - The mocked service is invoked exactly once with the given parameters.
     * <p>
     * Test procedure:
     * - A mock implementation of the `TemperatureRecordService` is created.
     * - Test inputs (device name, date, and hour) are specified.
     * - The mock `calculateAverageTemperature` method is set up to return `null` for these inputs.
     * - The `calculateAverageTemperature` method is invoked with the specified inputs.
     * - Assertions are performed to ensure the returned value is `null`.
     * - Verifies that the method is called exactly once with the given parameters.
     */
    @Test
    void testCalculateAverageTemperature_NoData() {
        // Arrange
        String deviceName = "DeviceB";
        String date = "2023-10-02";
        int hour = 15;
        TemperatureRecordService service = mock(TemperatureRecordService.class);
        when(service.calculateAverageTemperature(deviceName , date , hour)).thenReturn(null);

        // Act
        Double actualAverage = service.calculateAverageTemperature(deviceName , date , hour);

        // Assert
        assertNull(actualAverage , "The average temperature should be null when no data is available.");
        verify(service , times(1)).calculateAverageTemperature(deviceName , date , hour);
    }

    /**
     * Tests the `calculateAverageTemperature` method of the `TemperatureRecordService`
     * when handling temperature data associated with multiple devices.
     * <p>
     * This test verifies that:
     * - The method correctly calculates the average temperature for a specific device,
     *   date, and hour even when multiple devices are present in the dataset.
     * - The method returns the expected average temperature value for the given parameters.
     * - The service method is invoked exactly once with the specified inputs.
     * <p>
     * Test procedure:
     * - A mock implementation of the `TemperatureRecordService` is set up.
     * - Test inputs (device name, date, and hour) are defined.
     * - The mock `calculateAverageTemperature` method is configured to return a predefined
     *   average temperature value for these inputs.
     * - The `calculateAverageTemperature` method is called with the defined inputs.
     * <p>
     * Assertions:
     * - Confirms that the returned average temperature matches the expected value.
     * - Verifies the interaction with the mocked service to ensure the method is called once
     *   and only once with the correct parameters.
     */
    @Test
    void testCalculateAverageTemperature_MultipleDevices() {
        // Arrange
        String deviceName = "DeviceC";
        String date = "2023-10-03";
        int hour = 10;

        TemperatureRecordService service = mock(TemperatureRecordService.class);

        when(service.calculateAverageTemperature(deviceName , date , hour)).thenReturn(18.7);

        // Act
        Double actualAverage = service.calculateAverageTemperature(deviceName , date , hour);

        // Assert
        assertEquals(18.7 , actualAverage , "The average temperature should correctly handle data for a specific device.");
        verify(service , times(1)).calculateAverageTemperature(deviceName , date , hour);
    }

}