package com.example.smartqueue.controller;

import com.example.smartqueue.dto.DoctorRequest;
import com.example.smartqueue.dto.DoctorResponseDTO;
import com.example.smartqueue.entity.Doctor;
import com.example.smartqueue.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    public ResponseEntity<DoctorResponseDTO> createDoctor(
            @Valid @RequestBody DoctorRequest request) {

        DoctorResponseDTO doctor =
                doctorService.createDoctor(request);

        return new ResponseEntity<>(
                doctor,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<DoctorResponseDTO>> getAllDoctors() {

        List<DoctorResponseDTO> doctors =
                doctorService.getAllDoctors();

        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> getDoctorById(
            @PathVariable Long id) {

        DoctorResponseDTO doctor =
                doctorService.getDoctorById(id);

        return ResponseEntity.ok(doctor);
    }