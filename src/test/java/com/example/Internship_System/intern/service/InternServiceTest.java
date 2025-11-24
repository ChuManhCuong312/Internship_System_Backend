package com.example.Internship_System.intern.service;

import com.example.Internship_System.auth.entity.User;
import com.example.Internship_System.intern.dto.InternProfileDTO;
import com.example.Internship_System.intern.entity.InternProfile;
import com.example.Internship_System.repository.InternRepository;
import com.example.Internship_System.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InternServiceTest {

    @Mock
    private InternRepository internRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private InternService internService;

    private InternProfile internProfile1;
    private InternProfile internProfile2;
    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user1 = new User();
        user1.setUserId(1);
        user1.setFullName("Nguyễn Văn A");
        user1.setEmail("nguyenvana@example.com");
        user1.setPhone("0123456789");
        user1.setCreatedAt(LocalDateTime.now());

        user2 = new User();
        user2.setUserId(2);
        user2.setFullName("Trần Thị B");
        user2.setEmail("tranthib@example.com");
        user2.setPhone("0987654321");
        user2.setCreatedAt(LocalDateTime.now());

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
        internProfile1.setCvFile("cv_path_1.pdf");
        internProfile1.setPermissionFile("permission_1.pdf");

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
        internProfile2.setCvFile("cv_path_2.pdf");
        internProfile2.setPermissionFile("permission_2.pdf");
    }

    @Test
    public void testSaveInternProfile() {
        when(internRepository.save(internProfile1)).thenReturn(internProfile1);

        InternProfile result = internService.save(internProfile1);

        assertNotNull(result);
        assertEquals(1, result.getInternId());
        assertEquals("Công nghệ thông tin", result.getMajor());
        verify(internRepository, times(1)).save(internProfile1);
    }

    @Test
    public void testFindInternById() {
        when(internRepository.findById(1)).thenReturn(Optional.of(internProfile1));

        Optional<InternProfile> result = internService.findById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getInternId());
        assertEquals("Đại học Bách Khoa", result.get().getSchool());
        verify(internRepository, times(1)).findById(1);
    }

    @Test
    public void testFindInternByIdNotFound() {
        when(internRepository.findById(999)).thenReturn(Optional.empty());

        Optional<InternProfile> result = internService.findById(999);

        assertFalse(result.isPresent());
        verify(internRepository, times(1)).findById(999);
    }

    @Test
    public void testFindByUserId() {
        when(internRepository.findByUserId(1)).thenReturn(Optional.of(internProfile1));

        Optional<InternProfile> result = internService.findByUserId(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getUserId());
        verify(internRepository, times(1)).findByUserId(1);
    }

    @Test
    public void testFindByStatus() {
        List<InternProfile> internProfiles = Arrays.asList(internProfile1);
        when(internRepository.findByStatus("APPROVED")).thenReturn(internProfiles);

        List<InternProfile> result = internService.findByStatus("APPROVED");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("APPROVED", result.get(0).getStatus());
        verify(internRepository, times(1)).findByStatus("APPROVED");
    }

    @Test
    public void testDeleteIntern() {
        internService.deleteById(1);

        verify(internRepository, times(1)).deleteById(1);
    }

    @Test
    public void testSearchInterns() {
        InternProfileDTO dto1 = new InternProfileDTO(
                1, 1, "Nguyễn Văn A", "nguyenvana@example.com",
                "Đại học Bách Khoa", "Công nghệ thông tin", "APPROVED",
                "MALE", "0123456789", LocalDateTime.now(), null
        );
        List<InternProfileDTO> results = Arrays.asList(dto1);
        when(internRepository.searchInterns("Nguyễn", "Công nghệ thông tin", "APPROVED"))
                .thenReturn(results);

        List<InternProfileDTO> result = internService.searchInterns("Nguyễn", "Công nghệ thông tin", "APPROVED");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Nguyễn Văn A", result.get(0).getFullName());
        verify(internRepository, times(1)).searchInterns("Nguyễn", "Công nghệ thông tin", "APPROVED");
    }

    @Test
    public void testGetDistinctMajors() {
        List<String> majors = Arrays.asList("Công nghệ thông tin", "Kinh tế", "Quản lý");
        when(internRepository.findDistinctMajors()).thenReturn(majors);

        List<String> result = internService.getDistinctMajors();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains("Công nghệ thông tin"));
        verify(internRepository, times(1)).findDistinctMajors();
    }

    @Test
    public void testGetAllInterns() {
        List<InternProfile> profiles = Arrays.asList(internProfile1, internProfile2);
        when(internRepository.findAll()).thenReturn(profiles);
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2)).thenReturn(Optional.of(user2));

        List<InternProfileDTO> result = internService.getAllInterns();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(internRepository, times(1)).findAll();
    }

    @Test
    public void testSaveInternProfileWithFiles() {
        InternProfile profile = new InternProfile();
        profile.setUserId(1);
        profile.setSchool("Đại học");
        profile.setMajor("CNTT");
        profile.setCvFile("cv.pdf");
        profile.setPermissionFile("permission.pdf");

        when(internRepository.save(any(InternProfile.class))).thenReturn(profile);

        InternProfile result = internService.save(profile);

        assertNotNull(result);
        assertEquals("PENDING", result.getStatus());
        verify(internRepository, times(1)).save(any(InternProfile.class));
    }

    @Test
    public void testSaveInternProfileWithoutFiles() {
        InternProfile profile = new InternProfile();
        profile.setUserId(1);
        profile.setSchool("Đại học");
        profile.setMajor("CNTT");

        when(internRepository.save(any(InternProfile.class))).thenReturn(profile);

        InternProfile result = internService.save(profile);

        assertNotNull(result);
        assertEquals("NO_FILE", result.getStatus());
        verify(internRepository, times(1)).save(any(InternProfile.class));
    }
}
