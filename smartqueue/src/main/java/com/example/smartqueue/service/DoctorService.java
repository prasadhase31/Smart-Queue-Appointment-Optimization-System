package com.example.smartqueue.service;

import com.example.smartqueue.dto.DoctorRequest;
import com.example.smartqueue.dto.DoctorResponseDTO;
import com.example.smartqueue.entity.Doctor;
import com.example.smartqueue.repository.DoctorRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import com.example.smartqueue.exception.ResourceNotFoundException;
import com.example.smartqueue.exception.BadRequestException;

import java.time.LocalDateTime;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public DoctorResponseDTO createDoctor(DoctorRequest request) {

        if (doctorRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException(
                    "Doctor with this email already exists"
            );
        }

        Doctor doctor = new Doctor();

        doctor.setName(request.getName());
        doctor.setEmail(request.getEmail());
        doctor.setPhone(request.getPhone());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setConsultationFee(request.getConsultationFee());

        doctor.setStatus("ACTIVE");
        doctor.setCreatedAt(LocalDateTime.now());

        Doctor savedDoctor = doctorRepository.save(doctor);

        return mapToResponseDTO(savedDoctor);
    }

    public List<DoctorResponseDTO> getAllDoctors() {

        return doctorRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    public DoctorResponseDTO getDoctorById(Long id) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with id: " + id
                        ));

        return mapToResponseDTO(doctor);
    }
    private DoctorResponseDTO mapToResponseDTO(Doctor doctor) {

        DoctorResponseDTO response = new DoctorResponseDTO();

        response.setId(doctor.getId());
        response.setName(doctor.getName());
        response.setEmail(doctor.getEmail());
        response.setPhone(doctor.getPhone());
        response.setSpecialization(doctor.getSpecialization());
        response.setConsultationFee(doctor.getConsultationFee());
        response.setStatus(doctor.getStatus());
        response.setCreatedAt(doctor.getCreatedAt());

        return response;
    }
}
