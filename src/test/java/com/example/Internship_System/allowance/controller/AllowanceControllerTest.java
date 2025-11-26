package com.example.Internship_System.allowance.controller;

import com.example.Internship_System.allowance.dto.AllowanceDTO;
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

    private Allowance allowanceEntity;
    private AllowanceDTO allowanceDTO1;
    private AllowanceDTO allowanceDTO2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(allowanceController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // ENTITY for create/update
        allowanceEntity = new Allowance(1, "Lương tháng 11", new BigDecimal("5000000"),
                LocalDate.of(2025, 11, 23), "Lương tháng 11/2025");
        allowanceEntity.setAllowanceId(1);

        // DTO for GET operations
        allowanceDTO1 = new AllowanceDTO();
        allowanceDTO1.setAllowanceId(1);
        allowanceDTO1.setInternId(1);
        allowanceDTO1.setInternName("Intern A");
        allowanceDTO1.setType("Lương tháng 11");
        allowanceDTO1.setAmount(new BigDecimal("5000000"));
        allowanceDTO1.setDateApplied(LocalDate.of(2025, 11, 23));

        allowanceDTO2 = new AllowanceDTO();
        allowanceDTO2.setAllowanceId(2);
        allowanceDTO2.setInternId(2);
        allowanceDTO2.setInternName("Intern B");
        allowanceDTO2.setType("Thưởng");
        allowanceDTO2.setAmount(new BigDecimal("1000000"));
        allowanceDTO2.setDateApplied(LocalDate.of(2025, 11, 22));
    }

    @Test
    public void testCreateAllowance() throws Exception {
        when(allowanceService.save(any(Allowance.class))).thenReturn(allowanceEntity);
        doNothing().when(notificationService).createAllowanceNotification(anyInt(), anyString(), anyString());

        mockMvc.perform(post("/api/allowances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(allowanceEntity)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.allowanceId").value(1))
                .andExpect(jsonPath("$.type").value("Lương tháng 11"))
                .andExpect(jsonPath("$.amount").value(5000000));

        verify(allowanceService).save(any(Allowance.class));
        verify(notificationService).createAllowanceNotification(anyInt(), anyString(), anyString());
    }

    @Test
    public void testGetAllAllowances() throws Exception {
        List<AllowanceDTO> list = Arrays.asList(allowanceDTO1, allowanceDTO2);
        when(allowanceService.findAllWithInternNames()).thenReturn(list);

        mockMvc.perform(get("/api/allowances"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].allowanceId").value(1))
                .andExpect(jsonPath("$[1].allowanceId").value(2));

        verify(allowanceService).findAllWithInternNames();
    }

    @Test
    public void testGetAllowanceById() throws Exception {
        when(allowanceService.findById(1)).thenReturn(Optional.of(allowanceEntity));

        mockMvc.perform(get("/api/allowances/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.allowanceId").value(1));

        verify(allowanceService).findById(1);
    }

    @Test
    public void testGetAllowanceByIdNotFound() throws Exception {
        when(allowanceService.findById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/allowances/999"))
                .andExpect(status().isNotFound());

        verify(allowanceService).findById(999);
    }

    @Test
    public void testGetAllowancesByInternId() throws Exception {
        List<AllowanceDTO> list = Arrays.asList(allowanceDTO1);
        when(allowanceService.findByInternIdWithInternName(1)).thenReturn(list);

        mockMvc.perform(get("/api/allowances/intern/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].internId").value(1));

        verify(allowanceService).findByInternIdWithInternName(1);
    }

    @Test
    public void testUpdateAllowance() throws Exception {
        when(allowanceService.findById(1)).thenReturn(Optional.of(allowanceEntity));
        when(allowanceService.save(any(Allowance.class))).thenReturn(allowanceEntity);

        mockMvc.perform(put("/api/allowances/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(allowanceEntity)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.allowanceId").value(1));

        verify(allowanceService).findById(1);
        verify(allowanceService).save(any(Allowance.class));
    }

    @Test
    public void testDeleteAllowance() throws Exception {
        when(allowanceService.findById(1)).thenReturn(Optional.of(allowanceEntity));
        doNothing().when(allowanceService).deleteById(1);

        mockMvc.perform(delete("/api/allowances/1"))
                .andExpect(status().isNoContent());

        verify(allowanceService).findById(1);
        verify(allowanceService).deleteById(1);
    }

    @Test
    public void testFilterAllowances() throws Exception {
        List<AllowanceDTO> list = Arrays.asList(allowanceDTO1);

        when(allowanceService.filterAllowancesWithInternNames(
                eq(1), eq("Lương tháng 11"),
                eq(new BigDecimal("4000000")),
                eq(new BigDecimal("6000000")),
                eq(LocalDate.of(2025, 11, 01)),
                eq(LocalDate.of(2025, 11, 30))
        )).thenReturn(list);

        mockMvc.perform(get("/api/allowances/filter/search")
                        .param("internId", "1")
                        .param("type", "Lương tháng 11")
                        .param("minAmount", "4000000")
                        .param("maxAmount", "6000000")
                        .param("startDate", "2025-11-01")
                        .param("endDate", "2025-11-30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("Lương tháng 11"));

        verify(allowanceService).filterAllowancesWithInternNames(
                1, "Lương tháng 11",
                new BigDecimal("4000000"),
                new BigDecimal("6000000"),
                LocalDate.of(2025, 11, 01),
                LocalDate.of(2025, 11, 30)
        );
    }

    @Test
    public void testFilterAllowancesWithPagination() throws Exception {
        List<AllowanceDTO> list = Arrays.asList(allowanceDTO1);
        when(allowanceService.filterAllowancesWithInternNames(null, null, null, null, null, null))
                .thenReturn(list);

        mockMvc.perform(get("/api/allowances/filter/search")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(allowanceService).filterAllowancesWithInternNames(null, null, null, null, null, null);
    }
}
