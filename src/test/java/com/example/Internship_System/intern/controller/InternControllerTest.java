package com.example.Internship_System.intern.controller;

import com.example.Internship_System.intern.dto.InternProfileDTO;
import com.example.Internship_System.intern.dto.InternProfileWithPhoneDTO;
import com.example.Internship_System.intern.entity.InternProfile;
import com.example.Internship_System.intern.service.InternService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class InternControllerTest {

    private MockMvc mockMvc;

    @Mock
    private InternService internService;

    @InjectMocks
    private InternController internController;

    private ObjectMapper objectMapper;
    private InternProfile internProfile1;
    private InternProfile internProfile2;
    private InternProfileDTO internProfileDTO1;
    private InternProfileDTO internProfileDTO2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(internController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        internProfile1 = new InternProfile();
        internProfile1.setInternId(1);
        internProfile1.setUserId(1);
        internProfile1.setSchool("Đại học Bách Khoa");
        internProfile1.setMajor("Công nghệ thông tin");
        internProfile1.setStatus("APPROVED");
        internProfile1.setGender("MALE");
        internProfile1.setGpa(3.5);
        internProfile1.setDob(LocalDate.of(2003, 5, 15));
        internProfile1.setAddress("123 Đường ABC, Hà Nội");

        internProfile2 = new InternProfile();
        internProfile2.setInternId(2);
        internProfile2.setUserId(2);
        internProfile2.setSchool("Đại học Kinh tế");
        internProfile2.setMajor("Kinh tế");
        internProfile2.setStatus("PENDING");
        internProfile2.setGender("FEMALE");
        internProfile2.setGpa(3.8);
        internProfile2.setDob(LocalDate.of(2002, 8, 20));
        internProfile2.setAddress("456 Đường XYZ, TP HCM");

        internProfileDTO1 = new InternProfileDTO(
                1, 1, "Nguyễn Văn A", "nguyenvana@example.com",
                "Đại học Bách Khoa", "Công nghệ thông tin", "APPROVED",
                "MALE", "0123456789", LocalDateTime.now(), null
        );

        internProfileDTO2 = new InternProfileDTO(
                2, 2, "Trần Thị B", "tranthib@example.com",
                "Đại học Kinh tế", "Kinh tế", "PENDING",
                "FEMALE", "0987654321", LocalDateTime.now(), null
        );
    }

    @Test
    public void testCreateInternProfile() throws Exception {
        when(internService.save(any(InternProfile.class))).thenReturn(internProfile1);

        mockMvc.perform(post("/api/interns")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(internProfile1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.internId").value(1))
                .andExpect(jsonPath("$.major").value("Công nghệ thông tin"));

        verify(internService, times(1)).save(any(InternProfile.class));
    }

    @Test
    public void testGetAllInternProfiles() throws Exception {
        List<InternProfileDTO> profiles = Arrays.asList(internProfileDTO1, internProfileDTO2);
        when(internService.getAllInterns()).thenReturn(profiles);

        mockMvc.perform(get("/api/interns")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].internId").value(1))
                .andExpect(jsonPath("$[1].internId").value(2));

        verify(internService, times(1)).getAllInterns();
    }

    @Test
    public void testGetAllInternProfilesEmpty() throws Exception {
        when(internService.getAllInterns()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/interns")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(internService, times(1)).getAllInterns();
    }

    @Test
    public void testGetInternProfileById() throws Exception {
        when(internService.findById(1)).thenReturn(Optional.of(internProfile1));

        mockMvc.perform(get("/api/interns/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.internId").value(1))
                .andExpect(jsonPath("$.major").value("Công nghệ thông tin"));

        verify(internService, times(1)).findById(1);
    }

    @Test
    public void testGetInternProfileByIdNotFound() throws Exception {
        when(internService.findById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/interns/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(internService, times(1)).findById(999);
    }

    @Test
    public void testGetInternProfileByUserId() throws Exception {
        InternProfileWithPhoneDTO dto = new InternProfileWithPhoneDTO(internProfile1, "0123456789");
        when(internService.findByUserIdWithPhone(1)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/interns/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(internService, times(1)).findByUserIdWithPhone(1);
    }

    @Test
    public void testGetInternProfileByUserIdNotFound() throws Exception {
        when(internService.findByUserIdWithPhone(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/interns/user/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(internService, times(1)).findByUserIdWithPhone(999);
    }

    @Test
    public void testSearchInterns() throws Exception {
        List<InternProfileDTO> results = Arrays.asList(internProfileDTO1);
        when(internService.searchInterns("Nguyễn", "Công nghệ thông tin", "APPROVED"))
                .thenReturn(results);

        mockMvc.perform(get("/api/interns/search")
                .param("searchTerm", "Nguyễn")
                .param("major", "Công nghệ thông tin")
                .param("status", "APPROVED")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value("Nguyễn Văn A"));

        verify(internService, times(1)).searchInterns("Nguyễn", "Công nghệ thông tin", "APPROVED");
    }

    @Test
    public void testSearchInternsNoResults() throws Exception {
        when(internService.searchInterns("XYZ", null, null))
                .thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/interns/search")
                .param("searchTerm", "XYZ")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(internService, times(1)).searchInterns("XYZ", null, null);
    }

    @Test
    public void testGetMajors() throws Exception {
        List<String> majors = Arrays.asList("Công nghệ thông tin", "Kinh tế", "Quản lý");
        when(internService.getDistinctMajors()).thenReturn(majors);

        mockMvc.perform(get("/api/interns/majors")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Công nghệ thông tin"))
                .andExpect(jsonPath("$[1]").value("Kinh tế"))
                .andExpect(jsonPath("$[2]").value("Quản lý"));

        verify(internService, times(1)).getDistinctMajors();
    }

    @Test
    public void testSearchInternsWithMultipleFilters() throws Exception {
        List<InternProfileDTO> results = Arrays.asList(internProfileDTO1, internProfileDTO2);
        when(internService.searchInterns(null, "Công nghệ thông tin", null))
                .thenReturn(Arrays.asList(internProfileDTO1));

        mockMvc.perform(get("/api/interns/search")
                .param("major", "Công nghệ thông tin")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(internService, times(1)).searchInterns(null, "Công nghệ thông tin", null);
    }
}
