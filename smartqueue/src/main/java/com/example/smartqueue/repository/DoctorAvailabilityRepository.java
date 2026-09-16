package com.example.smartqueue.repository;

import com.example.smartqueue.entity.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;

public interface DoctorAvailabilityRepository
        extends JpaRepository<DoctorAvailability, Long> {

    List<DoctorAvailability> findByDoctorId(Long doctorId);

    boolean existsByDoctorIdAndAvailableDateAndStartTimeAndEndTime(
            Long doctorId,
            LocalDate availableDate,
            LocalTime startTime,
            LocalTime endTime
    );

    @Query("""
    SELECT COUNT(a) > 0
    FROM DoctorAvailability a
    WHERE a.doctor.id = :doctorId
    AND a.availableDate = :availableDate
    AND a.startTime < :endTime
    AND a.endTime > :startTime
""")
    boolean existsOverlappingAvailability(
            @Param("doctorId") Long doctorId,
            @Param("availableDate") LocalDate availableDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}