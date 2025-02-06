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

    @Test
    public void testProcessBatch_InvalidRequestBody() throws Exception {
        String invalidJson = "{ \"deviceName\": \"AB123\", \"location\": \"A1\" "; // Missing closing bracket

        mockMvc.perform(post("/api/v1/temperatureRecord/processRecords")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

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
