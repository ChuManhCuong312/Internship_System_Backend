package com.example.Internship_System.intern.service;

import com.example.Internship_System.intern.entity.Attendance;
import com.example.Internship_System.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepository repository;
    public Attendance save(Attendance attendance){return repository.save(attendance);}
    public List<Attendance> findAll(){return repository.findAll();}
    public Optional<Attendance> findById(int id){return repository.findById(id);}
    public Optional<Attendance> findByInternId(int internId){return repository.findByInternId(internId);}
    public void deleteById(int id){repository.deleteById(id);}
}
