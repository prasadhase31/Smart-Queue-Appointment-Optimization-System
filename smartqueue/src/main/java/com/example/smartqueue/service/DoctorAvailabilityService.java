package com.example.smartqueue.service;

import com.example.smartqueue.entity.DoctorAvailability;
import com.example.smartqueue.repository.DoctorAvailabilityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorAvailabilityService {

    private final DoctorAvailabilityRepository doctorAvailabilityRepository;

    public DoctorAvailabilityService(DoctorAvailabilityRepository doctorAvailabilityRepository) {
        this.doctorAvailabilityRepository = doctorAvailabilityRepository;
    }


    public DoctorAvailability createAvailability(DoctorAvailability availability) {
        return doctorAvailabilityRepository.save(availability);
    }


    public List<DoctorAvailability> getAllAvailability() {
        return doctorAvailabilityRepository.findAll();
    }

    public List<DoctorAvailability> getAvailabilityByDoctorId(Long doctorId) {
        return doctorAvailabilityRepository.findByDoctorId(doctorId);
    }
}