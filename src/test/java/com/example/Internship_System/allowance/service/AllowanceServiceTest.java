package com.example.Internship_System.allowance.service;

import com.example.Internship_System.allowance.entity.Allowance;
import com.example.Internship_System.repository.AllowanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AllowanceServiceTest {

    @Mock
    private AllowanceRepository allowanceRepository;

    @InjectMocks
    private AllowanceService allowanceService;

    private Allowance allowance1;
    private Allowance allowance2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        allowance1 = new Allowance(1, "Lương tháng 11", new BigDecimal("5000000"), LocalDate.of(2025, 11, 23), "Lương tháng 11/2025");
        allowance1.setAllowanceId(1);

        allowance2 = new Allowance(2, "Thưởng hiệu suất", new BigDecimal("1000000"), LocalDate.of(2025, 11, 22), "Thưởng tháng 11");
        allowance2.setAllowanceId(2);
    }

    @Test
    public void testSaveAllowance() {
        when(allowanceRepository.save(allowance1)).thenReturn(allowance1);

        Allowance result = allowanceService.save(allowance1);

        assertNotNull(result);
        assertEquals(1, result.getAllowanceId());
        assertEquals("Lương tháng 11", result.getType());
        assertEquals(new BigDecimal("5000000"), result.getAmount());
        verify(allowanceRepository, times(1)).save(allowance1);
    }

    @Test
    public void testFindAllAllowances() {
        List<Allowance> allowances = Arrays.asList(allowance1, allowance2);
        when(allowanceRepository.findAll()).thenReturn(allowances);

        List<Allowance> result = allowanceService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Lương tháng 11", result.get(0).getType());
        verify(allowanceRepository, times(1)).findAll();
    }

    @Test
    public void testFindAllowanceById() {
        when(allowanceRepository.findById(1)).thenReturn(Optional.of(allowance1));

        Optional<Allowance> result = allowanceService.findById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getAllowanceId());
        assertEquals("Lương tháng 11", result.get().getType());
        verify(allowanceRepository, times(1)).findById(1);
    }

    @Test
    public void testFindAllowanceByIdNotFound() {
        when(allowanceRepository.findById(999)).thenReturn(Optional.empty());

        Optional<Allowance> result = allowanceService.findById(999);

        assertFalse(result.isPresent());
        verify(allowanceRepository, times(1)).findById(999);
    }

    @Test
    public void testFindByInternId() {
        List<Allowance> allowances = Arrays.asList(allowance1);
        when(allowanceRepository.findByInternId(1)).thenReturn(allowances);

        List<Allowance> result = allowanceService.findByInternId(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getInternId());
        verify(allowanceRepository, times(1)).findByInternId(1);
    }

    @Test
    public void testDeleteAllowance() {
        allowanceService.deleteById(1);

        verify(allowanceRepository, times(1)).deleteById(1);
    }

    @Test
    public void testFilterAllowances() {
        List<Allowance> allowances = Arrays.asList(allowance1);
        when(allowanceRepository.filterAllowances(
                1, "Lương tháng 11", new BigDecimal("4000000"), new BigDecimal("6000000"),
                LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30)
        )).thenReturn(allowances);

        List<Allowance> result = allowanceService.filterAllowances(
                1, "Lương tháng 11", new BigDecimal("4000000"), new BigDecimal("6000000"),
                LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30)
        );

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Lương tháng 11", result.get(0).getType());
        verify(allowanceRepository, times(1)).filterAllowances(
                1, "Lương tháng 11", new BigDecimal("4000000"), new BigDecimal("6000000"),
                LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30)
        );
    }

    @Test
    public void testFilterAllowancesWithNullFilters() {
        List<Allowance> allowances = Arrays.asList(allowance1, allowance2);
        when(allowanceRepository.filterAllowances(null, null, null, null, null, null))
                .thenReturn(allowances);

        List<Allowance> result = allowanceService.filterAllowances(null, null, null, null, null, null);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(allowanceRepository, times(1)).filterAllowances(null, null, null, null, null, null);
    }

    @Test
    public void testFindAllPaginatedNoSort() {
        List<Allowance> allowances = Arrays.asList(allowance1, allowance2);
        Page<Allowance> page = new PageImpl<>(allowances, PageRequest.of(0, 10), 2);
        when(allowanceRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Allowance> result = allowanceService.findAllPaginatedNoSort(0, 10);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(0, result.getNumber());
        verify(allowanceRepository, times(1)).findAll(any(Pageable.class));
    }
}
