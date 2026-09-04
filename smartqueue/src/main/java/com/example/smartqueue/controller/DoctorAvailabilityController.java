package com.example.smartqueue.controller;

import com.example.smartqueue.entity.DoctorAvailability;
import com.example.smartqueue.service.DoctorAvailabilityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availability")
public class DoctorAvailabilityController {

    private final DoctorAvailabilityService doctorAvailabilityService;

    public DoctorAvailabilityController(
            DoctorAvailabilityService doctorAvailabilityService) {
        this.doctorAvailabilityService = doctorAvailabilityService;
    }


    @PostMapping
    public ResponseEntity<DoctorAvailability> createAvailability(
            @RequestBody DoctorAvailability availability) {

        DoctorAvailability savedAvailability =
                doctorAvailabilityService.createAvailability(availability);

        return new ResponseEntity<>(
                savedAvailability,
                HttpStatus.CREATED
        );
    }


    @GetMapping
    public ResponseEntity<List<DoctorAvailability>> getAllAvailability() {

        List<DoctorAvailability> availabilityList =
                doctorAvailabilityService.getAllAvailability();

        return new ResponseEntity<>(
                availabilityList,
                HttpStatus.OK
        );
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<DoctorAvailability>> getAvailabilityByDoctorId(
            @PathVariable Long doctorId) {

        List<DoctorAvailability> availabilityList =
                doctorAvailabilityService.getAvailabilityByDoctorId(doctorId);

        return new ResponseEntity<>(
                availabilityList,
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorAvailability> updateAvailability(
            @PathVariable Long id,
            @RequestBody DoctorAvailability availability) {

        DoctorAvailability updatedAvailability =
                doctorAvailabilityService.updateAvailability(id, availability);

        return new ResponseEntity<>(
                updatedAvailability,
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAvailability(
            @PathVariable Long id) {

        doctorAvailabilityService.deleteAvailability(id);

        return ResponseEntity.ok(
                "Doctor availability deleted successfully"
        );
    }
}