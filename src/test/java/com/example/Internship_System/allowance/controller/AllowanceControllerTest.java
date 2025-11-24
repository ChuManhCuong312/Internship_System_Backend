package com.example.Internship_System.allowance.controller;

import com.example.Internship_System.allowance.entity.Allowance;
import com.example.Internship_System.allowance.service.AllowanceService;
import com.example.Internship_System.notification.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AllowanceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AllowanceService allowanceService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private AllowanceController allowanceController;

    private ObjectMapper objectMapper;
    private Allowance allowance1;
    private Allowance allowance2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(allowanceController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        allowance1 = new Allowance(1, "Lương tháng 11", new BigDecimal("5000000"), LocalDate.of(2025, 11, 23), "Lương tháng 11/2025");
        allowance1.setAllowanceId(1);

        allowance2 = new Allowance(2, "Thưởng hiệu suất", new BigDecimal("1000000"), LocalDate.of(2025, 11, 22), "Thưởng tháng 11");
        allowance2.setAllowanceId(2);
    }

    @Test
    public void testCreateAllowance() throws Exception {
        when(allowanceService.save(any(Allowance.class))).thenReturn(allowance1);
        doNothing().when(notificationService).createAllowanceNotification(anyInt(), anyString(), anyString());

        mockMvc.perform(post("/api/allowances")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(allowance1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.allowanceId").value(1))
                .andExpect(jsonPath("$.type").value("Lương tháng 11"))
                .andExpect(jsonPath("$.amount").value(5000000));

        verify(allowanceService, times(1)).save(any(Allowance.class));
        verify(notificationService, times(1)).createAllowanceNotification(anyInt(), anyString(), anyString());
    }

    @Test
    public void testGetAllAllowances() throws Exception {
        List<Allowance> allowances = Arrays.asList(allowance1, allowance2);
        when(allowanceService.findAll()).thenReturn(allowances);

        mockMvc.perform(get("/api/allowances")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].allowanceId").value(1))
                .andExpect(jsonPath("$[1].allowanceId").value(2));

        verify(allowanceService, times(1)).findAll();
    }

    @Test
    public void testGetAllowanceById() throws Exception {
        when(allowanceService.findById(1)).thenReturn(Optional.of(allowance1));

        mockMvc.perform(get("/api/allowances/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.allowanceId").value(1))
                .andExpect(jsonPath("$.type").value("Lương tháng 11"));

        verify(allowanceService, times(1)).findById(1);
    }

    @Test
    public void testGetAllowanceByIdNotFound() throws Exception {
        when(allowanceService.findById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/allowances/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(allowanceService, times(1)).findById(999);
    }

    @Test
    public void testGetAllowancesByInternId() throws Exception {
        List<Allowance> allowances = Arrays.asList(allowance1);
        when(allowanceService.findByInternId(1)).thenReturn(allowances);

        mockMvc.perform(get("/api/allowances/intern/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].internId").value(1));

        verify(allowanceService, times(1)).findByInternId(1);
    }

    @Test
    public void testUpdateAllowance() throws Exception {
        Allowance updatedAllowance = new Allowance(1, "Lương tháng 12", new BigDecimal("5500000"), LocalDate.of(2025, 12, 23), "Lương tháng 12/2025");
        updatedAllowance.setAllowanceId(1);

        when(allowanceService.findById(1)).thenReturn(Optional.of(allowance1));
        when(allowanceService.save(any(Allowance.class))).thenReturn(updatedAllowance);

        mockMvc.perform(put("/api/allowances/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedAllowance)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("Lương tháng 12"));

        verify(allowanceService, times(1)).findById(1);
        verify(allowanceService, times(1)).save(any(Allowance.class));
    }

    @Test
    public void testDeleteAllowance() throws Exception {
        when(allowanceService.findById(1)).thenReturn(Optional.of(allowance1));
        doNothing().when(allowanceService).deleteById(1);

        mockMvc.perform(delete("/api/allowances/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(allowanceService, times(1)).findById(1);
        verify(allowanceService, times(1)).deleteById(1);
    }

    @Test
    public void testFilterAllowances() throws Exception {
        List<Allowance> allowances = Arrays.asList(allowance1);
        when(allowanceService.filterAllowances(
                1, "Lương tháng 11", new BigDecimal("4000000"), new BigDecimal("6000000"),
                LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30)
        )).thenReturn(allowances);

        mockMvc.perform(get("/api/allowances/filter/search")
                .param("internId", "1")
                .param("type", "Lương tháng 11")
                .param("minAmount", "4000000")
                .param("maxAmount", "6000000")
                .param("startDate", "2025-11-01")
                .param("endDate", "2025-11-30")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("Lương tháng 11"));

        verify(allowanceService, times(1)).filterAllowances(
                1, "Lương tháng 11", new BigDecimal("4000000"), new BigDecimal("6000000"),
                LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30)
        );
    }

    @Test
    public void testFilterAllowancesWithPagination() throws Exception {
        List<Allowance> allowances = Arrays.asList(allowance1);
        when(allowanceService.filterAllowances(null, null, null, null, null, null))
                .thenReturn(allowances);

        mockMvc.perform(get("/api/allowances/filter/search")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(allowanceService, times(1)).filterAllowances(null, null, null, null, null, null);
    }
}
