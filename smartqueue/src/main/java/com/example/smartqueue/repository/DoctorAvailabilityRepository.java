package com.example.smartqueue.repository;

import com.example.smartqueue.entity.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorAvailabilityRepository
        extends JpaRepository<DoctorAvailability, Long> {
}