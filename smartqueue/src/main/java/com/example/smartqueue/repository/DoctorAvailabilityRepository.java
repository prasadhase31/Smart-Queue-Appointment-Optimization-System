package com.example.smartqueue.repository;

import com.example.smartqueue.entity.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface DoctorAvailabilityRepository
        extends JpaRepository<DoctorAvailability, Long> {

    List<DoctorAvailability> findByDoctorId(Long doctorId);

    boolean existsByDoctorIdAndAvailableDateAndStartTimeAndEndTime(
            Long doctorId,
            LocalDate availableDate,
            LocalTime startTime,
            LocalTime endTime
    );
}