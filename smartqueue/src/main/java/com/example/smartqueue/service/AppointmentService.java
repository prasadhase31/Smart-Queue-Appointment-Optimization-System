package com.example.smartqueue.service;

import com.example.smartqueue.dto.AppointmentRequestDTO;
import com.example.smartqueue.entity.Appointment;
import com.example.smartqueue.entity.Doctor;
import com.example.smartqueue.entity.DoctorAvailability;
import com.example.smartqueue.entity.User;
import com.example.smartqueue.repository.AppointmentRepository;
import com.example.smartqueue.repository.DoctorAvailabilityRepository;
import com.example.smartqueue.repository.DoctorRepository;
import com.example.smartqueue.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.example.smartqueue.dto.AppointmentResponseDTO;
import com.example.smartqueue.dto.UserResponseDTO;
import com.example.smartqueue.dto.DoctorResponseDTO;
import com.example.smartqueue.dto.AvailabilityResponseDTO;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorAvailabilityRepository doctorAvailabilityRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            UserRepository userRepository,
            DoctorRepository doctorRepository,
            DoctorAvailabilityRepository doctorAvailabilityRepository) {

        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.doctorAvailabilityRepository = doctorAvailabilityRepository;
    }

    // Create Appointment
    public Appointment createAppointment(AppointmentRequestDTO request) {

        // Find Patient
        User patient = userRepository.findById(request.getPatientId())
                .orElseThrow(() ->
                        new RuntimeException("Patient not found")
                );

        // Find Doctor
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() ->
                        new RuntimeException("Doctor not found")
                );

        // Find Availability
        DoctorAvailability availability =
                doctorAvailabilityRepository.findById(
                        request.getAvailabilityId()
                ).orElseThrow(() ->
                        new RuntimeException("Availability not found")
                );

        // Doctor ↔ Availability validation
        if (!availability.getDoctor().getId().equals(doctor.getId())) {
            throw new RuntimeException(
                    "This availability does not belong to this doctor"
            );
        }

        // Date validation
        if (!request.getAppointmentDate()
                .equals(availability.getAvailableDate())) {

            throw new RuntimeException(
                    "Appointment date does not match doctor's availability date"
            );
        }

        // Time validation
        if (request.getAppointmentTime().isBefore(availability.getStartTime())
                || request.getAppointmentTime().isAfter(availability.getEndTime())) {

            throw new RuntimeException(
                    "Appointment time is outside doctor's availability time"
            );
        }

        // Duplicate booking validation
        boolean alreadyBooked =
                appointmentRepository
                        .existsByDoctorIdAndAppointmentDateAndAppointmentTime(
                                doctor.getId(),
                                request.getAppointmentDate(),
                                request.getAppointmentTime()
                        );

        if (alreadyBooked) {
            throw new RuntimeException(
                    "This doctor is already booked for this date and time"
            );
        }

        // DTO → Entity
        Appointment appointment = new Appointment();

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAvailability(availability);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setReason(request.getReason());

        // Default status
        appointment.setStatus("BOOKED");

        return appointmentRepository.save(appointment);
    }

    // Get All Appointments
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // Update Appointment

    // Update Appointment
    public Appointment updateAppointment(
            Long id,
            AppointmentRequestDTO request) {

        // Find existing appointment
        Appointment existingAppointment =
                appointmentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Appointment not found"));

        // Cancelled appointment cannot be updated
        if ("CANCELLED".equals(existingAppointment.getStatus())) {
            throw new RuntimeException(
                    "Cancelled appointment cannot be updated"
            );
        }

        // Find Patient
        User patient = userRepository.findById(request.getPatientId())
                .orElseThrow(() ->
                        new RuntimeException("Patient not found")
                );

        // Find Doctor
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() ->
                        new RuntimeException("Doctor not found")
                );

        // Find Availability
        DoctorAvailability availability =
                doctorAvailabilityRepository.findById(
                        request.getAvailabilityId()
                ).orElseThrow(() ->
                        new RuntimeException("Availability not found")
                );

        // Doctor ↔ Availability validation
        if (!availability.getDoctor().getId().equals(doctor.getId())) {
            throw new RuntimeException(
                    "This availability does not belong to this doctor"
            );
        }

        // Date validation
        if (!request.getAppointmentDate()
                .equals(availability.getAvailableDate())) {

            throw new RuntimeException(
                    "Appointment date does not match doctor's availability date"
            );
        }

        // Time validation
        if (request.getAppointmentTime().isBefore(availability.getStartTime())
                || request.getAppointmentTime().isAfter(availability.getEndTime())) {

            throw new RuntimeException(
                    "Appointment time is outside doctor's availability time"
            );
        }

        // Duplicate booking validation
        boolean alreadyBooked =
                appointmentRepository
                        .existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndIdNot(
                                doctor.getId(),
                                request.getAppointmentDate(),
                                request.getAppointmentTime(),
                                id
                        );

        if (alreadyBooked) {
            throw new RuntimeException(
                    "This doctor is already booked for this date and time"
            );
        }

        // Update appointment
        existingAppointment.setPatient(patient);
        existingAppointment.setDoctor(doctor);
        existingAppointment.setAvailability(availability);
        existingAppointment.setAppointmentDate(
                request.getAppointmentDate()
        );
        existingAppointment.setAppointmentTime(
                request.getAppointmentTime()
        );
        existingAppointment.setReason(
                request.getReason()
        );

        // Keep existing status
        // Status should be managed by confirm/cancel APIs
        // existingAppointment.setStatus(...) is intentionally removed

        return appointmentRepository.save(existingAppointment);
    }



    // Cancel Appointment
    public Appointment cancelAppointment(Long id) {

        Appointment appointment =
                appointmentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Appointment not found"));

        if ("CANCELLED".equals(appointment.getStatus())) {
            throw new RuntimeException(
                    "Appointment is already cancelled"
            );
        }

        appointment.setStatus("CANCELLED");

        return appointmentRepository.save(appointment);
    }

    // Confirm Appointment
    public Appointment confirmAppointment(Long id) {

        Appointment appointment =
                appointmentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Appointment not found"));

        if ("CANCELLED".equals(appointment.getStatus())) {
            throw new RuntimeException(
                    "Cancelled appointment cannot be confirmed"
            );
        }

        if ("CONFIRMED".equals(appointment.getStatus())) {
            throw new RuntimeException(
                    "Appointment is already confirmed"
            );
        }

        appointment.setStatus("CONFIRMED");

        return appointmentRepository.save(appointment);
    }

    // Delete Appointment
    public void deleteAppointment(Long id) {

        if (!appointmentRepository.existsById(id)) {
            throw new RuntimeException("Appointment not found");
        }

        appointmentRepository.deleteById(id);
    }
}