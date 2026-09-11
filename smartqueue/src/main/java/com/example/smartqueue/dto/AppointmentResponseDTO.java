package com.example.smartqueue.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class AppointmentResponseDTO {

    private Long id;

    private UserResponseDTO patient;

    private DoctorResponseDTO doctor;

    private AvailabilityResponseDTO availability;

    private LocalDate appointmentDate;

    private LocalTime appointmentTime;

    private String reason;

    private String status;
}