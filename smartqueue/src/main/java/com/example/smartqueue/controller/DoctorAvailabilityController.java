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
}