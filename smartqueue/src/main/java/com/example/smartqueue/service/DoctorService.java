package com.example.smartqueue.service;

import com.example.smartqueue.dto.DoctorRequest;
import com.example.smartqueue.entity.Doctor;
import com.example.smartqueue.repository.DoctorRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import com.example.smartqueue.exception.ResourceNotFoundException;

import java.time.LocalDateTime;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public Doctor createDoctor(DoctorRequest request) {

        Doctor doctor = new Doctor();

        doctor.setName(request.getName());
        doctor.setEmail(request.getEmail());
        doctor.setPhone(request.getPhone());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setConsultationFee(request.getConsultationFee());

        doctor.setStatus("ACTIVE");
        doctor.setCreatedAt(LocalDateTime.now());

        return doctorRepository.save(doctor);
    }

    public List<Doctor> getAllDoctors() {

        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long id) {

        return doctorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with id: " + id
                        ));
    }
}